var gadgets = gadgets || {};

gadgets.io = function() {
	return {
		makeRequest : function(url, callback, opt_params) {
			alert("makeRequest: " + url);
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

