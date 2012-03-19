package sk.seges.sesam.core.pap.utils;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;

public class PAPReflectionUtils {

	public static TypeKind toPrimitive(Class<?> clazz) {
	     if (clazz.equals(Void.class)) {
	    	 return TypeKind.VOID;
	     }
	     if (clazz.equals(Boolean.class)) {
	    	 return TypeKind.BOOLEAN;
	     }
	     if (clazz.equals(Character.class)) {
	    	 return TypeKind.CHAR;
	     }
	     if (clazz.equals(Byte.class)) {
	    	 return TypeKind.BYTE;
	     }
	     if (clazz.equals(Short.class)) {
	    	 return TypeKind.SHORT;
	     }
	     if (clazz.equals(Integer.class)) {
	    	 return TypeKind.INT;
	     }
	     if (clazz.equals(Long.class)) {
	    	 return TypeKind.LONG;
	     }
	     if (clazz.equals(Float.class)) {
	    	 return TypeKind.FLOAT;
	     }
	     if (clazz.equals(Double.class)) {
	    	 return TypeKind.DOUBLE;
	     }
	     
	     return TypeKind.OTHER;
	}
	
	public static Set<Modifier> toModifiers(int modifierValue) {
		
		Set<Modifier> modifiers = new HashSet<Modifier>();
		
		if (java.lang.reflect.Modifier.isAbstract(modifierValue)) {
			modifiers.add(Modifier.ABSTRACT);
		}
		if (java.lang.reflect.Modifier.isFinal(modifierValue)) {
			modifiers.add(Modifier.FINAL);
		}
		if (java.lang.reflect.Modifier.isNative(modifierValue)) {
			modifiers.add(Modifier.NATIVE);
		}
		if (java.lang.reflect.Modifier.isPrivate(modifierValue)) {
			modifiers.add(Modifier.PRIVATE);
		}
		if (java.lang.reflect.Modifier.isProtected(modifierValue)) {
			modifiers.add(Modifier.PROTECTED);
		}
		if (java.lang.reflect.Modifier.isPublic(modifierValue)) {
			modifiers.add(Modifier.PUBLIC);
		}
		if (java.lang.reflect.Modifier.isStatic(modifierValue)) {
			modifiers.add(Modifier.STATIC);
		}
		if (java.lang.reflect.Modifier.isStrict(modifierValue)) {
			modifiers.add(Modifier.STRICTFP);
		}
		if (java.lang.reflect.Modifier.isSynchronized(modifierValue)) {
			modifiers.add(Modifier.SYNCHRONIZED);
		}
		if (java.lang.reflect.Modifier.isTransient(modifierValue)) {
			modifiers.add(Modifier.TRANSIENT);
		}
		if (java.lang.reflect.Modifier.isVolatile(modifierValue)) {
			modifiers.add(Modifier.VOLATILE);
		}
		
		return modifiers;
	}

}
