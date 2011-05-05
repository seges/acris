package sk.seges.sesam.pap.test.selenium.processor;

public class NullCheck {

	public static Class<?> checkNull(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		
		if (clazz.getName().equals(Constants.VOID.getName())) {
			return null;
		}
		return clazz;
	}
	
	public static String checkNull(String value) {
		if (value == null) {
			return null;
		}
		if (value.equals(Constants.NULL)) {
			return null;
		}
		
		return value;
	}	
}