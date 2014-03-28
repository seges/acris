function ErrorPanel (form, errorPanel, inverse) {
    this.inputForm = form;
    this.errorPanel = errorPanel;
	this.inverse = (typeof inverse === "undefined") ? false : inverse;
}
 
ErrorPanel.prototype = {
	initialize: function(success) {
		var panelWrapper = this;
		this.errorPanel.find(".close").click(function() {
			panelWrapper.clearValidationMessages()
			panelWrapper.hideValidationMessages()
			panelWrapper.inputForm.find(".submit").parsley('destroy');
		})

		panelWrapper.inputForm.parsley({
			listeners: {
				onFieldError: function(elem, constraint) {
					panelWrapper.showValidationMessages()
				},
				onFieldSuccess: function(elem, constraint) {
					panelWrapper.clearValidationMessage()
				}
			}
		})
			
		this.inputForm.find(".submit").click(function() {
			panelWrapper.inputForm.submit();
			window.setTimeout(function() {	
				if ((panelWrapper.inputForm.find(".parsley-error").length == 0) && (typeof success != "undefined")) {
					success();
				}
			}, 100);
		})
	},
	animateErrorPanel: function(hide, complete) {

		var panelWrapper = this;

		if (hide != this.inverse) {
			if (this.inverse) {
				this.errorPanel.css("display", "inline-block")
			}

			window.setTimeout(function() {
				panelWrapper.errorPanel.animate({marginTop: "-" + panelWrapper.errorPanel.height() + "px"}, {
					complete: function() {
						if (!panelWrapper.inverse) { 
							panelWrapper.errorPanel.hide()
						}
						complete()
					}
				})
			}, 10);
		} else {
			if (!this.inverse) {
				this.errorPanel.css("display", "inline-block")
			}
			this.errorPanel.animate({marginTop:"0px"}, {
				complete: function() {
					if (panelWrapper.inverse) { 
						panelWrapper.errorPanel.hide();
					}
					complete()
				}
			})
		}
	},
	clearValidationMessage: function() {

		var panelWrapper = this;

		window.setTimeout(function() {
			if (panelWrapper.inputForm.find(".parsley-error").length == 0) {
				panelWrapper.hideValidationMessages()
			}
		}, 10)
	},
	showValidationMessages: function() {
		if (!this.errorPanel.is(":visible")) {
			var panelWrapper = this;
			this.animateErrorPanel(false, function() {
				if (panelWrapper.inputForm.find(".parsley-error").length == 0) {
					panelWrapper.hideValidationMessages()
				}
			})
		}
	},
	clearValidationMessages: function() {
		this.inputForm.parsley('destroy')
	},
	hideValidationMessages: function(complete) {
		if (this.errorPanel.is(":visible")) {
			var panelWrapper = this;
			this.animateErrorPanel(true, function() {
				if (panelWrapper.inputForm.find(".parsley-error").length > 0) {
					panelWrapper.showValidationMessages()
				} else if (typeof complete != "undefined") {
					complete();
				}
			});
		} else if (typeof complete != "undefined") {
			complete();
		}
	},
	addErrorMessage: function(message) {
		var panelWrapper = this;
		this.inputForm.parsley('destroy');
		this.inputForm.find(".submit").parsley('destroy');
		this.inputForm.find(".submit").removeClass("parsley-error");
		this.inputForm.find(".submit").parsley('manageErrorContainer')
			.parsley('addError', {error: message});
		this.inputForm.find(".submit").addClass("parsley-error");
		panelWrapper.showValidationMessages()
	}
}