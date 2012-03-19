package sk.seges.sesam.core.pap.utils;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.PathResolver;

public class MethodHelper {

	public static final String SETTER_PREFIX = "set";
	public static final String GETTER_PREFIX = "get";
	public static final String GETTER_IS_PREFIX = "is";

	public static String toMethod(String name) {

		if (name.length() < 2) {
			return name.toUpperCase();
		}

		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public static String toMethod(String prefix, String fieldName) {
		PathResolver pathResolver = new PathResolver(fieldName);

		if (pathResolver.isNested()) {

			int i = 0;

			String result = "";

			while (pathResolver.hasNext()) {
				if (i > 0) {
					result += ".";
				}
				String path = pathResolver.next();

				if (pathResolver.hasNext()) {
					result += toGetter(path);
				} else {
					result += toMethod(prefix, path);
				}
				i++;
			}

			return result;
		}

		return prefix + toMethod(fieldName);
	}

	public static String toSetter(ExecutableElement method) {
		String simpleName = method.getSimpleName().toString();
		
		if (simpleName.startsWith(GETTER_PREFIX)) {
			return toSetter(simpleName.substring(GETTER_PREFIX.length()));
		}
		
		if (simpleName.startsWith(GETTER_IS_PREFIX)) {
			return toSetter(simpleName.substring(GETTER_IS_PREFIX.length()));
		}
		
		return toSetter(simpleName);
	}

	public static  String toField(String fieldName) {
		String[] pathParts = fieldName.split("\\.");
		String result = "";

		for (String path : pathParts) {
			result += toMethod(path);
		}

		if (result.length() < 2) {
			return result.toLowerCase();
		}

		return result.substring(0, 1).toLowerCase() + result.substring(1);
	}

	public static String toGetter(String fieldName) {
		return toMethod(GETTER_PREFIX, fieldName) + "()";
	}

	public static String toGetter(ExecutableElement method) {
		return toGetter(method.getSimpleName().toString());
	}

	public static String toSetter(String fieldName) {
		return toMethod(SETTER_PREFIX, fieldName);
	}

	public static String toField(ExecutableElement getterMethod) {

		String result = "";

		String simpleName = getterMethod.getSimpleName().toString();
		
		if (simpleName.startsWith(GETTER_PREFIX)) {
			result = simpleName.substring(GETTER_PREFIX.length());
		} else if (simpleName.startsWith(GETTER_IS_PREFIX)) {
			result = simpleName.substring(GETTER_IS_PREFIX.length());
		} else {
			result = simpleName;
		}

		if (result.length() < 2) {
			return result.toLowerCase();
		}

		return result.substring(0, 1).toLowerCase() + result.substring(1);
	}

	public static Element getField(ExecutableElement method) {
		return getField(method.getEnclosingElement(), toField(method));
	}
	
	public static VariableElement getField(Element element, String name) {
		List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());
		
		for (VariableElement field: fields) {
			if (field.getSimpleName().toString().equals(name)) {
				return field;
			}
		}
		
		if (element.getKind().equals(ElementKind.CLASS)) {
			TypeMirror superclass = ((TypeElement)element).getSuperclass();
			if (superclass.getKind().equals(TypeKind.DECLARED)) {
				return getField(((DeclaredType)superclass).asElement(), name);
			}
		}
		return null;
	}
	
	public static boolean isGetterMethod(ExecutableElement method) {
		String simpleName = method.getSimpleName().toString();
		return simpleName.startsWith(GETTER_PREFIX) || simpleName.startsWith(GETTER_IS_PREFIX);
	}

}