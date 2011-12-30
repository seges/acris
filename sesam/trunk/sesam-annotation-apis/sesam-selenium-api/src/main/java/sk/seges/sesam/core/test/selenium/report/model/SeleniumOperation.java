package sk.seges.sesam.core.test.selenium.report.model;


public enum SeleniumOperation {
	
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
			return "Finding by";
		}
	}, CLICK_ON {
		@Override
		public String getDescription() {
			return "Clicking on";
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
	};

	public abstract String getDescription();
}