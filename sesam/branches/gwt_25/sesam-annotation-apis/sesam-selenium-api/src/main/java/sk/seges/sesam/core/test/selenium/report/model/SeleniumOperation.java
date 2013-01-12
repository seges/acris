package sk.seges.sesam.core.test.selenium.report.model;


public enum SeleniumOperation {
	
	ALL {
		@Override
		public String getDescription() {
			return "All operations";
		}		
	},
	NAVIGATE_TO {
		@Override
		public String getDescription() {
			return "Navigating to";
		}		
	}, NAVIGATE_BACK {
		@Override
		public String getDescription() {
			return "Navigating back";
		}
	}, NAVIGATE_FORWARD {
		@Override
		public String getDescription() {
			return "Navigating forward";
		}
	}, FIND_BY {
		@Override
		public String getDescription() {
			return "Finding";
		}
	}, KEY_DOWN {
		@Override
		public String getDescription() {
			return "Pressing key down";
		}
	}, SEND_KEYS {
		@Override
		public String getDescription() {
			return "Sending keys";
		}
	}, KEY_UP {
		@Override
		public String getDescription() {
			return "Pushing key up";
		}
	}, CLICK_ON {
		@Override
		public String getDescription() {
			return "Clicking on";
		}
	}, DOUBLE_CLICK_ON {
		@Override
		public String getDescription() {
			return "Double clicking on";
		}
	}, CLICK_AND_HOLD {
		@Override
		public String getDescription() {
			return "Clicking on holding";
		}
	}, CHANGE_VALUE {
		@Override
		public String getDescription() {
			return "Changing value of";
		}
	}, RUN_SCRIPT {
		@Override
		public String getDescription() {
			return "Running script";
		}
	}, ASSERTION {
		@Override
		public String getDescription() {
			return "Assert that ";
		}
	}, FAIL {
		@Override
		public String getDescription() {
			return "Fail";
		}
	}, BUTTON_RELEASE {
		@Override
		public String getDescription() {
			return "Releasing button ";
		}
	}, MOUSE_MOVE {
		@Override
		public String getDescription() {
			return "Moving mouse ";
		}
	}, MOVE_TO_OFFSET {
		@Override
		public String getDescription() {
			return "Moving to offset ";
		}
	}, CONTEXT_CLICK {
		@Override
		public String getDescription() {
			return "Invoking context menu ";
		}
	};

	public abstract String getDescription();
}