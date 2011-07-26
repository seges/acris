package sk.seges.sesam.core.pap;

import javax.lang.model.element.TypeElement;

public class NullCheck {

	public static TypeElement checkNull(TypeElement typeElement, Class<?> nullClazz) {
		if (typeElement == null) {
			return null;
		}
		
		if (typeElement.getQualifiedName().toString().equals(nullClazz.getCanonicalName())) {
			return null;
		}

		switch (typeElement.asType().getKind()) {
		case NONE:
		case NULL:
		case OTHER:
		case VOID:
		case ERROR:
			return null;
		}
		
		return typeElement;
	}
	
	public static TypeElement checkNull(TypeElement typeElement) {
		return checkNull(typeElement, Constants.VOID);
	}
	
	public static Class<?> checkNull(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		
		if (clazz.getName().equals(Constants.VOID.getName())) {
			return null;
		}
		return clazz;
	}

	public static Integer checkNull(Integer value) {
		if (value == null || value.intValue() == 0) {
			return null;
		}
		
		return value;
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