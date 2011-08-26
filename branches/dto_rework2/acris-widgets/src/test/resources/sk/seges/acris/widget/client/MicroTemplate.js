var str="<% for ( var i = 0; i < items.length; i++ ) { %>"+
"<div class=\"olea-OrderSummaryPanel-label\"><%=items[i].label%></div><div class=\"olea-OrderSummaryPanel-value\"><%=items[i].value%></div><div class=\"acris-cleaner\"></div>"+
"<% } %>";
//var str = "<div class=\"olea-OrderSummaryPanel-label\"/>";
var data={'items':[{'label':'i18n.summary_priceIncVAT()','value':'priceIncVATStr'},{'label':'12i18n.summary_priceIncVAT()','value':'12priceIncVATStr'}]};
alert(data.items.length);
var fn = 
	      // Generate a reusable function that will serve as a template
	      // generator (and which will be cached).
	      new Function("obj",
	        "var p=[],print=function(){p.push.apply(p,arguments);};" +
	        
	        // Introduce the data as local variables using with(){}
	        "with(obj){p.push('" +
	        
	        // Convert the template into pure JavaScript
	        str
	          .replace(/[\r\t\n]/g, " ")
	          .split("<%").join("\t")
	          .replace(/((^|%>)[^\t]*)'/g, "$1\r")
	          .replace(/\t=(.*?)%>/g, "',$1,'")
	          .split("\t").join("');")
	          .split("%>").join("p.push('")
	          .split("\r").join("\\'")
	      + "');}return p.join('');");
	    
	    // Provide some basic currying to the user
	    alert('daaa1 ' + fn(data));

