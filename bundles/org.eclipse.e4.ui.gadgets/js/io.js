var gadgets = gadgets || {};

gadgets.io = function() {
	return {
		makeRequest : function(url, callback, params) {
			// at the moment we only support a very tiny subset of
			// what is allowed in "params"
			// alert("makeRequest: " + url);
			var xhr = null;
			if (window.XMLHttpRequest) {
				xhr = new window.XMLHttpRequest();
			} else if (window.ActiveXObject) {
				xhr = new ActiveXObject("Msxml2.XMLHTTP");
				if (!xhr)
					xhr = new ActiveXObject("Microsoft.XMLHTTP");
			}
			var method = params[gadgets.io.RequestParameters.METHOD] || "GET";
			var proxiedUrl = "%%%PROXY_URL%%%/openSocialProxy?" + url;
			// alert(proxiedUrl) ;
			try {
				xhr.open(method, proxiedUrl, true);
				xhr.onreadystatechange = function() {
					if (xhr.readyState == 4 && xhr.status == 200) {
						callback( {
							data : eval("(" + xhr.responseText + ")")
						});
					}
				};
				xhr.send();
			} catch (e) {
				alert(e.description);
			}
		}
	}
}();

gadgets.io.RequestParameters = gadgets.util.makeEnum( [ "METHOD",
		"CONTENT_TYPE", "POST_DATA", "HEADERS", "AUTHORIZATION", "NUM_ENTRIES",
		"GET_SUMMARIES", "REFRESH_INTERVAL", "OAUTH_SERVICE_NAME",
		"OAUTH_USE_TOKEN", "OAUTH_TOKEN_NAME", "OAUTH_REQUEST_TOKEN",
		"OAUTH_REQUEST_TOKEN_SECRET" ]);

gadgets.io.MethodType = gadgets.util.makeEnum( [ "GET", "POST", "PUT",
		"DELETE", "HEAD" ]);

gadgets.io.ContentType = gadgets.util
		.makeEnum( [ "TEXT", "DOM", "JSON", "FEED" ]);

gadgets.io.AuthorizationType = gadgets.util.makeEnum( [ "NONE", "SIGNED",
		"OAUTH" ]);
