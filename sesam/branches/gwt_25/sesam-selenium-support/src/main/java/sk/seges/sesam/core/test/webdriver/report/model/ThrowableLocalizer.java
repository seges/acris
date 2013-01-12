package sk.seges.sesam.core.test.webdriver.report.model;

import java.util.ResourceBundle;

import org.openqa.selenium.NoSuchElementException;

public enum ThrowableLocalizer {
	
	NO_SUCH_ELEMENT {

		@Override
		public Class<? extends Throwable> getThrowableClass() {
			return NoSuchElementException.class;
		}

		@Override
		public String getMessage(ResourceBundle bundle, Throwable throwable) {
			return bundle.getString("exception.nosuchelement");
		}
	};
	
	public abstract Class<? extends Throwable> getThrowableClass();
	
	public abstract String getMessage(ResourceBundle bundle, Throwable throwable);
	
	public static ThrowableLocalizer get(Throwable throwable) {
		for (ThrowableLocalizer tl: ThrowableLocalizer.values()) {
			if (tl.getThrowableClass().equals(throwable.getClass())) {
				return tl;
			}
		}
		
		return null;
	}
}
