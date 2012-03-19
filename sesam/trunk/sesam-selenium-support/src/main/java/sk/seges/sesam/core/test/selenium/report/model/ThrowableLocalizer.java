package sk.seges.sesam.core.test.selenium.report.model;

import org.openqa.selenium.NoSuchElementException;

public enum ThrowableLocalizer {
	
	NO_SUCH_ELEMENT {

		@Override
		public Class<? extends Throwable> getThrowableClass() {
			return NoSuchElementException.class;
		}

		@Override
		public String getKey() {
			return "exception.nosuchelement";
		}
	};
	
	public abstract Class<? extends Throwable> getThrowableClass();
	
	public abstract String getKey();
	
	public static ThrowableLocalizer get(Throwable throwable) {
		for (ThrowableLocalizer tl: ThrowableLocalizer.values()) {
			if (tl.getThrowableClass().equals(throwable.getClass())) {
				return tl;
			}
		}
		
		return null;
	}
}
