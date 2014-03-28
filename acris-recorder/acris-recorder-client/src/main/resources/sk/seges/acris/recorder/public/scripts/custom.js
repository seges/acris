var baseAdminURL = "http://admin.synapso.sk/"
var loopLimit = 18;
var registrationErrorPanel;
var loginErrorPanel;
var loginHoverLock = false;
var loginHovered = false;
var autologinTime = 3000;
	
$(function() {
	loginErrorPanel = new ErrorPanel($("#name_pwd_signin"), $("#login_form_error_wrapper"), false);
	loginErrorPanel.initialize(function() {
		var shaObj = new jsSHA($("#password").val(), "TEXT");
		var hashed = shaObj.getHash("SHA-1", "HEX", 1, {"outputUpper":true});
		var userName = $("#username").val();
		ga('send', 'event', 'button_click', 'login', 'login_form');
		doLogin(userName, hashed, true);
	})

	$("#login_button").hover(function() {
		$("#login_form_wrapper").show()
	})
	
	$("#login_form_wrapper input").focus(function() {
		loginHoverLock = true;
	})

	$("#login_form_wrapper input").blur(function() {
		loginHoverLock = false;
		if (!loginHovered) {
			$("#login_form_wrapper").hide()
		}
	})
	
	$("#login_form_wrapper .submit").click(function() {
		loginHoverLock = true;
	})
	
	$("#login_form_wrapper").hover(
		function() {
			loginHovered = true;
		},
		function() {
			loginHovered = false;
			if (!loginHoverLock) {
				$("#login_form_wrapper").hide()
			}
		}
	)
	
	$("#login_form_wrapper").keypress(function(e) {
		if(e.which == 13) {
			$("#login_form_wrapper .submit").trigger("click")
		}
	});
});
	
function doLogin(userName, pwd, addToCookie) {
	var url = "/acris-server/acris-rest/apikey/login?userName=" + encodeURIComponent(userName) + "&password=" + pwd;
	var request = $.ajax({
		url: url,
		success: function(data) {
			if (data.apiKey == null || data.webId == null) {
				loginHoverLock = true;
				loginErrorPanel.addErrorMessage(stringMap['wrongNamePwd']);
				$("#login_form_wrapper").show();
			} else {
				loginHoverLock = false;
				document.location.href = baseAdminURL + data.webId + "?apiKey=" + data.apiKey;
				if (addToCookie) {
					storeCookie('u', userName);
					storeCookie('p', pwd);
				}
			}
		},
		error: function(data) {
			alert("ERROR")
		}
	});
}

function storeCookie(cookieName, value) {
	var date = new Date();
	var minutes = 5;
	date.setTime(date.getTime() + (minutes * 60 * 1000));
	$.cookie(cookieName, value, { expires: date});
}

$(function() {
	var contactErrorPanel = new ErrorPanel($("#contact_form"), $("#contact_form_error_wrapper"), true)
	contactErrorPanel.initialize(function () {
		contactForm.css("display", "none");
		contactForm.find('input:text, input:password, input:file, select, textarea').val('')
	})

	$("#up").click(function(){
		$("html, body").animate({ scrollTop: "0px" }, 1000);
	})
	
	var contactForm = $("#contact_form");

	$("#contact_us").click(function(){
		contactForm.css("display", "block");
		contactForm.animate({height:"293px",marginTop:"-269px", backgroundPositionY:"0px"});
	})

	$("#close_button").click(function() {
		contactErrorPanel.clearValidationMessages()
		contactErrorPanel.hideValidationMessages(function() {
			contactForm.animate({height:"0px", marginTop:"20px", backgroundPositionY:"-293px"}, {
				complete: function() {
					contactForm.css("display", "none");
					contactForm.find('input:text, input:password, input:file, select, textarea').val('')
				}
			});
		})
	})
});

function initSlider(sliderId) {
	//AutoSlideTransTimer - 750 is default
	initSlider(sliderId, true, true, 750);
}

function initSlider(sliderId, clickDrag, clickDrag, transTimer) {
	$('#' + sliderId).iosSlider({
		desktopClickDrag: clickDrag,
		snapToChildren: true,
		keyboardControls: clickDrag,
		navSlideSelector: '.sliderContainer .slideSelectors .slide',
		onSlideStart: slideStart,
		autoSlide: true,
		scrollbar: true,
		scrollbarContainer: '.sliderContainer .scrollbarContainer',
		scrollbarMargin: '0',
		scrollbarBorderRadius: '0',
		infiniteSlider: true,
		autoSlideTransTimer: transTimer,
		navNextSelector:$(".next"),
		navPrevSelector:$(".prev")
	});
}

function slideStart(args) {
	$(".right_hand").animate({
		opacity: 1,
		left: "-=400",
		bottom: "-60",
		height: "slow"
	}, 550, function() {
		$(".right_hand").animate({
			opacity: 0,
			bottom: "-=300",
			height: "toggle"
		}, 200, function() {
			$(".right_hand").animate({
			opacity: 1,
			left: "+=400",
			height: "slow"
			}, 200, function() {
				$(".right_hand").animate({
				opacity: 1,
				bottom: "+=360",
				height: "toggle"
				}, 200, function() {
				});
			});
		});
	});
}

$(window).blur(function(e) {
	$(".right_hand").stop(true, true);
});

$(function() {
	registrationErrorPanel = new ErrorPanel($("#register_form"), $("#register_form_error_wrapper"), false);
	registrationErrorPanel.initialize(function() {
		ga('send', 'event', 'button_click', 'register', 'register_form');
		createWeb($("#register_form"));
	})
	
	$("#register_form input").focus(function() {
		$('.iosSlider').iosSlider('lock');
	});
	
	$("#register_form input").blur(function() {
		$('.iosSlider').iosSlider('unlock');
	});

	$("#register_form").keypress(function(e) {
		if(e.which == 13) {
			$("#register_form .submit").trigger("click")
		}
	});
});

$(function() {
	window.onload = function() {
		var creating = $.urlParam('creating'); 
		var error = $.urlParam('error');
		var registrationError = $.urlParam('registrationError');
		var source = $.urlParam('source');
		if (creating && creating == 'true') {
			$("#loader-wrapper").css('display','block');
			$("#register_form_wrapper").css('display','none');
			var webId = $.urlParam('webId'); 
			var provider = $.urlParam('provider'); 
			
			var url = "/synapso-server/synapso-rest/provision/check/" + webId;
			var i = 1;
			var interval = setInterval(function() {
				if (i <= loopLimit) {
					var request = $.ajax({
						url: url,
						success: function(data) {
							var exitCode = data.exitCode;
							if (typeof exitCode != "undefined" && exitCode == 0) {
								window.clearInterval(interval);
								storeCookie(provider, 'true');
								ga('send', 'event', 'button_click', 'register', 'register_' + provider);
								$("#" + provider + "_signin").submit();
								$("#success_msg").css('display','inline-block');
								$("#creating_msg").css('display','none');
								$("#circle_loader").css('display','none');
								cleanup(5000);
							} else if (typeof exitCode != "undefined" && (exitCode == 1 || exitCode == 2)) {
								registrationErrorPanel.addErrorMessage(stringMap[exitCode]);
								window.clearInterval(interval);
								cleanup(1);
							}
							i = i+1;
						},
						error: function(data) {
							window.clearInterval(interval);
							registrationErrorPanel.addErrorMessage(stringMap[1]);
							window.clearInterval(interval);
							cleanup(1);
						}
					});
				} else {
					registrationErrorPanel.addErrorMessage(stringMap[1]);
					window.clearInterval(interval);
					cleanup(1);
					i = 0;
				}
			}, 10000);
		} else if (error) {
			loginErrorPanel.addErrorMessage("Login failed - " + error);
			loginHoverLock = true;
			$("#login_form_wrapper").show();
		} else if (registrationError) {
			registrationErrorPanel.addErrorMessage(registrationError);
		} else if (typeof source != "undefined") {
			$("#" + source + "_register").find('.source').val(source);
			$("#" + source + "_register").submit();
		}
		$("#fb_register").find('.domain').val(location.href);
		
		if (typeof $.cookie('u') != "undefined") {
			window.setTimeout(function() {	
				doLogin($.cookie('u'), $.cookie('p'), true);
			}, autologinTime);
		}
			
		if (typeof $.cookie('fb') != "undefined") {
			window.setTimeout(function() {	
				$("#fb_signin").trigger("click");
			}, autologinTime);	
		}
			
		if (typeof $.cookie('tw') != "undefined") {
			window.setTimeout(function() {	
				$("#tw_signin").trigger("click");
			}, autologinTime);
		}
		
		if( /Android|MSIE |webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
			$("#joinUp").attr('value', stringMap['createFree']);
			$("#joinUp").click(function(){
				$("html, body").animate({ scrollTop: "0px" }, 1000);
			});
		} else {
			$("#joinUp").attr('value', stringMap['tryFree']);
			$("#joinUp").click(function(){
				var shaObj = new jsSHA("demo", "TEXT");
				var hashed = shaObj.getHash("SHA-1", "HEX", 1, {"outputUpper":true});
				var userName = stringMap['demoUser'];
				ga('send', 'event', 'button_click', 'try', 'free');
				doLogin(userName, hashed, false);
			});
		}
	};
	
	$.urlParam = function(name){
		var results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(window.location.href);
		if (results) {
			return decodeURI(results[1] || 0);
		}
		return null;
	}
	
	function cleanup(milis) {
		window.setTimeout(function() {	
			$("#loader-wrapper").css('display','none');
			$("#success_msg").css('display','none');
			$("#error_msg").css('display','none');
			$("#register_form_wrapper").css('display','inline-block');
		}, milis)
	}
});

$(function() {
	if ($('#home_slider').attr('loaded') != 'true') {
		$('#home_slider').attr('loaded', 'true');
		$('#home_slider').slides({
			effect : 'slide',
			fadeSpeed : 400,
			crossfade : true,
			generatePagination : true,
			play : 5000,
			pause : 0,
			hoverPause : false
		});
	}

	document.showPreview = function(sliderId) {
		var popup = $(".preview_popup");
		
		var left = ($(window).width()) / 2;
		var top = ($(window).height()) / 2 + $(window).scrollTop();

		var targetLeft = ($(window).width() - popup.width()) / 2;
		var targetTop = ($(window).height() - popup.height()) / 2 + $(window).scrollTop();

		popup.css("top", top + "px");
		popup.css("left", left + "px");
		popup.css("width", "0px");
		popup.css("height", "0px");

		popup.animate({height:"542px", width:"900px",
			left: targetLeft + "px", top: targetTop + "px"});

		initSlider("person_card_preview"); 

		var person_card_preview_slides = $("#" + sliderId);
		person_card_preview_slides.css("display", "block");
		$("#preview_content").append(person_card_preview_slides);

		//disable slider
		var tabletSlider = $('#tablet_slider');
		tabletSlider.iosSlider('autoSlidePause');
		tabletSlider.iosSlider('lock');

		popup.css("display", "block"); 
		$(".overlay_wrapper").css("display", "block");
	};

	$('.popup_close_button').click(function() {
		var left = ($(window).width()) / 2;
		var top = ($(window).height()) / 2 + $(window).scrollTop();
		var popup = $(".preview_popup");
		popup.animate({height:"0px", width:"0px",
			left: left + "px", top: top + "px"}, 
			{
				complete: function() {
					popup.css("width", "542px");
					popup.css("height", "900px");

					popup.attr("style", "display: none;");	

					$("#person_card_preview_slides.iosSlider").iosSlider('destroy');

					//enable slider again
					var tabletSlider = $('#tablet_slider');
					tabletSlider.iosSlider('autoSlidePlay');
					tabletSlider.iosSlider('unlock');

			}});

		$(".overlay_wrapper").attr("style", "display: none");
	});
});