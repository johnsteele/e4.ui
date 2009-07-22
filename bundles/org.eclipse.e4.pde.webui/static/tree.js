/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/

var dirty = false;

window.e4 = window.e4 || {
	status : {
		setMessage : function(msg) { alert(msg); },
		setDirty : function(d) {
			dirty = d;
			dojo.byId("dirtyIndicator").style.visibility = d ? "visible" : "hidden";
			dojo.byId("saveButton").style.visibility = d ? "visible" : "hidden";
		}
	},
	
	saveable : {
		doSave : function(callback) {}
	}
};

treeRoot = {categories:{}, children:[]};

function doSave() {
	var jsonData = {categoryDef: [], feature: []};
	for (var i in treeRoot.children) {
	  var child = treeRoot.children[i];
	  if (child.id) {
	    // child is a feature
	    jsonData.feature.push(child);
	  } else {
	    // child is a category
	    var category = { name: child.name, label: child.label, description: child.label};
	    jsonData.categoryDef.push(category);
	    for (var j in child.children) {
	      var feature = child.children[j];
	      feature.category = {name: child.name};
	      jsonData.feature.push(feature);
	    }
	  }
	}
	var jsonString = dojo.toJson(jsonData);
	//alert(jsonString);
	dojo.rawXhrPut({
        url: "/pde/site" + dojo.doc.location.hash.substring(1), 
        putData: jsonString,
        timeout: 5000,
        load: function(response, ioArgs) {
        	//alert("success: " + response + ", " + ioArgs);
			window.e4.status.setDirty(false);
        	return response;
        },
        error: function(response, ioArgs) {
        	alert("ERROR: " + response + ", " + ioArgs);
        	return response;
        }
	});
}

dojo.require("dijit.dijit");
dojo.require("dijit.Dialog");
dojo.require("dijit.Tree");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dojo.cookie");

var modelPrototype = {
	destroy: function(){
	},
	
	getRoot: function(onItem){
		onItem(this.root);
	},
	
	mayHaveChildren: function(/*dojo.data.Item*/ item){
	    var result = item.children != undefined;
		return result;
	},
	
	getChildren: function(/*dojo.data.Item*/ parentItem, /*function(items)*/ onComplete){
	  if (parentItem.children) {
	    onComplete(parentItem.children);
	  } else {
	    onComplete([]);
	  }
	},
	
	getIdentity: function(/* item */ item){
		var result = item.id;
		if (item === this.root) {
		  result = "ROOT";
		}
		if (!result) {
		  result = item.name;
		}	
		return result;
	},
	
	getLabel: function(/*dojo.data.Item*/ item){
		return item.id || item.name;
	},
	
	newItem: function(/* Object? */ args, /*Item?*/ parent){
	},
	
	pasteItem: function(/*Item*/ childItem, /*Item*/ oldParentItem, /*Item*/ newParentItem, /*Boolean*/ bCopy){
	},
		
	onChange: function(/*dojo.data.Item*/ item){
		window.e4.status.setDirty(true);
	},
	
	onChildrenChange: function(/*dojo.data.Item*/ parent, /*dojo.data.Item[]*/ newChildrenList){
		window.e4.status.setDirty(true);
	}
};

window.onbeforeunload = function (evt) {
  var message = "You have unsaved changes.";
  if (dirty) {
	  return message;
  }
};
dojo.addOnLoad(function(){
	dojo.xhrGet({
        url: "/pde/site" + dojo.doc.location.hash.substring(1), 
        handleAs: "json-comment-filtered",
        timeout: 5000,
        load: function(jsonData, ioArgs) {
          createTree(jsonData);
          return jsonData;
        },
        error: function(response, ioArgs) {
		  if (ioArgs.xhr && (ioArgs.xhr.status == 403)) {
		    dojo.doc.location.href="/pde/login?nextURL=" + encodeURIComponent(dojo.doc.location.pathname) + encodeURIComponent(dojo.doc.location.search) + encodeURIComponent(dojo.doc.location.hash);
		  }
		  console.error("HTTP status code: ", ioArgs.xhr.status);
		  return response;
        }
    });
});
		
function createTree(jsonData){
	for (var c in jsonData.categoryDef) {
	  var category = jsonData.categoryDef[c];
	  treeRoot.categories[category.name] = category;
	  treeRoot.children.push(category);
	}
	for (var f in jsonData.feature) {
	  var feature = jsonData.feature[f];
	  if (feature["category"]) {
	    var category = treeRoot.categories[feature["category"].name];
	    if (category) {
	      if (!category.children) {
	        category.children = [];
	      }
	      category.children.push(feature);
	    } else {
	      treeRoot.children.push(feature);
	    }
	  } else {
	    treeRoot.children.push(feature);
	  }
	}
	var Model = function(root) { this.root=root; };
	Model.prototype = modelPrototype;
	var model = new Model(treeRoot);
	var detailConnections = [];
	function bind(connections, inputID, domAttribute, modelObject, modelAttribute) {
		var inputElement = dojo.byId(inputID);
		inputElement[domAttribute] = modelObject[modelAttribute];
		connections.push(
			dojo.connect(inputElement, "onchange", function() {
				modelObject[modelAttribute] = inputElement[domAttribute];
				model.onChange(modelObject);
			})
		);
		connections.push(
			dojo.connect(inputElement, "onkeypress", function() {
				model.onChange(modelObject);
			})
		);
	}
	myTree = new dijit.Tree({
		id: "myTree",
		model: model,
		showRoot: false,
		getLabel: function(item) {
		    if (item.id) {
		      return item.id + (item.version?(" ("+item.version+")"):"");
		    } else {
		      return item.label;
		    }
		},
		onClick: function(item,treeNode) {
			if (item.id) {
				dojo.byId("categoryProperties").style.display="none";
				dojo.forEach(detailConnections, dojo.disconnect);
				bind(detailConnections, "featureID", "value", item, "id");
				bind(detailConnections, "featureVersion", "value", item, "version");
				bind(detailConnections, "featureURL", "value", item, "url");
				dojo.byId("featureProperties").style.display="block";
			} else {
				dojo.byId("featureProperties").style.display="none";
				dojo.forEach(detailConnections, dojo.disconnect);
				bind(detailConnections, "categoryID", "value", item, "name");
				bind(detailConnections, "categoryName", "value", item, "label");
				bind(detailConnections, "categoryDescription", "innerText", item, "description");
				dojo.byId("categoryProperties").style.display="block";
			}
		},
		getIconClass: function(/*dojo.data.Item*/ item, /*Boolean*/ opened){
			return item.id ? "featureItem" : "categoryItem";
		}
	});
	myTree.startup();
	dojo.byId("tree").appendChild(myTree.domNode);
    dojo.connect(dojo.byId('newCategory'), 'onclick', function() {
    	var newCategory = {
         "description":"",
         "label":"New Category",
         "name":"newCategory"
      	};
    	treeRoot.children.push(newCategory);
    	model.onChildrenChange(treeRoot, treeRoot.children);
    	var node = myTree._itemNodeMap["newCategory"];
    	myTree.focusNode(node);
    	myTree.onClick(newCategory, node);
    });
    dojo.connect(dojo.byId('addFeature'), 'onclick', function() {
	    var featureStore = new dojo.data.ItemFileReadStore({
	            url: "/pde/features/"
	    });
	    var featureSelectWidget = dijit.byId("featureSelect");
	    if (featureSelectWidget) {
	    	// since "featureSelectWidget.reset();" doesn't work:
	    	featureSelectWidget._hasBeenBlurred = false;
	    	featureSelectWidget.setDisplayedValue("");
	    } else {
		    featureSelectWidget = new dijit.form.FilteringSelect({
		        id: "featureSelect",
		        name: "id",
		        store: featureStore,
		        searchAttr: "id"
		    }, "featureSelect");
		}
		var dialog = dijit.byId('featureDialog');
		dojo.connect(dialog, "onExecute", function() {
	    	var newFeature = {
	         "category":null,
	         "url":""
			};
			var id = dijit.byId("featureSelect").getValue();
			var newFeature = featureSelectWidget.store._getItemByIdentity(id);
	    	treeRoot.children.push(newFeature);
    		model.onChildrenChange(treeRoot, treeRoot.children);
	    	var node = myTree._itemNodeMap[newFeature.id];
	    	myTree.focusNode(node);
	    	myTree.onClick(newFeature, node);
		});
    	dialog.show();
    });
    dojo.connect(dojo.byId('synchronize'), 'onclick', function() {
    	alert("Sorry - 'Synchronize' is not yet implemented.");
    });
    dojo.connect(dojo.byId('build'), 'onclick', function() {
    	alert("Sorry - 'Build' is not yet implemented.");
    });
    dojo.connect(dojo.byId('buildAll'), 'onclick', function() {
    	alert("Sorry - 'BuildAll' is not yet implemented.");
    });
    window.e4.saveable.doSave("doSave();");
}
