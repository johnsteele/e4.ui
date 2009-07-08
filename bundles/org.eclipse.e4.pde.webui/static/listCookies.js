var cookies = document.cookie.split("; ");
//alert("cookies");
for (var i = 0; i < cookies.length; i++) {
	//alert(cookies[i]);
	document.write(cookies[i]);
	document.write("<br/>");
}