function createWeb(form) {
	var firstName = form.find(".person_name").val().toLowerCase()
	var lastName = form.find(".person_surname").val().toLowerCase();
	var password = form.find(".password").val();
	var mail = form.find(".email").val();
	var domainSuffix = form.find(".domainSuffix").val();
	var theme = form.find(".theme").val();
	var program = form.find(".program").val();
	
	var url = location.href;
	var dataString = 'domain=' + url + '&mail=' + encodeURIComponent(mail) + '&password='
			+ encodeURIComponent(password) + "&appendDefaultDomain=false" + "&domainWebId=" + window.webId
			+ "&plainFormat=false" + "&locale=" + form.find(".locale").val() + "&domainSuffix=" 
			+ domainSuffix + "&theme=" + theme + "&program=" + program + "&emailAsUsername=true&firstName=" + firstName
			+ "&lastName=" + lastName;

	dataString += "&royal-web";

	$("#loader-wrapper").css('display','block');
	$("#register_form_wrapper").css('display','none');
	
	$.ajax({
		type : "POST",
		url : "/synapso-server/synapso-rest/provision/web-simple",
		data : dataString,
		success : function(data, textStatus, jqXHR) {
			if(data.exitCode == "1" || data.exitCode == "2") {
				var errorMessage = "Neznáma chyba";
				if (data.errorMessage != null && data.errorMessage) {
					errorMessage = data.errorMessage; 
				}
				registrationErrorPanel.addErrorMessage(stringMap[data.exitCode]);
				$("#loader-wrapper").css('display','none');
				$("#register_form_wrapper").css('display','inline-block');
				$("#registration-form")[0].reset();
			} else if(data.exitCode == "0") {
				// Do not redirect to admin, but automatic login
				var shaObj = new jsSHA(password, "TEXT");
				var hashed = shaObj.getHash("SHA-1", "HEX", 1, {"outputUpper":true});
				var url = "/acris-server/acris-rest/apikey/login?userName=" + encodeURIComponent(mail) + "&password=" + hashed;
				var request = $.ajax({
					url: url,
					success: function(data) {
						if (data.apiKey == null || data.webId == null) {
							registrationErrorPanel.addErrorMessage(stringMap['wrongNamePwd']);
							$("#loader-wrapper").css('display','none');
							$("#register_form_wrapper").css('display','block');
						} else {
							document.location.href = baseAdminURL + data.webId + "?apiKey=" + data.apiKey;
						}
					},
					error: function(data) {
						registrationErrorPanel.addErrorMessage(stringMap['unknownErr']);
					}
				});
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			registrationErrorPanel.addErrorMessage(stringMap['unknownErr']);
			$("#loader-wrapper").css('display','none');
			$("#register_form_wrapper").css('display','inline-block');
		}
	});
	
	return false;
}