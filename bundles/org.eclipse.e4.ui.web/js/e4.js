/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthew Hatem, IBM Corporation - initial API and implementation
 *     Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
window.e4 = {
	
	isInstalled : function() {
		try {
			return typeof e4RPC == "function";
		} catch(e) {
			return false;
		}
	},
	
	hasFeature : function(feature) {
		return this.isInstalled() && typeof this[feature] == "object";
	},
	
	dialogs : {
		confirm : function(msg) {
			return e4RPC("dialogs", "confirm", msg);
		},
		openFiles : function() {
			
		}
	},
	
	clipboard: {
		getContents : function() {
			return e4RPC("clipboard", "getContents");
		}
	}, 
	
	log: {
		info : function(msg) {
			return e4RPC("log", "info", msg);
		}
	}, 
	
	menus : {
		addContextMenuItem : function(label, callback) {
			return e4RPC("menus", "addContextMenuItem", label, callback);
		}
	},
	
	status : {
		setMessage : function(msg) {
			return e4RPC("status", "setMessage", msg);
		},
		_dirty : false,
		setDirty : function(dirty) {
			if (window.e4.status._dirty != dirty) {
				window.e4.status._dirty = dirty;
				return e4RPC("status", "setDirty", dirty);
			}
		}
	},
	
	saveable : {
		promptToSaveOnClose : function(callback) {
			return e4RPC("saveable", "promptToSaveOnClose", callback);
		},
		doSave : function(callback) {
			return e4RPC("saveable", "doSave", callback);
		}
	}
};
