package sk.seges.acris.core.rebind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;

/**
 * @author eldzi
 */
public class RebindUtils {

	public static class FieldDeclaration {
		public JType type;
		public Annotation[] annotations;
		public boolean isPublic = false;
		public JMethod setterMethod;
		public JMethod getterMethod;
	}

	/** Convert method getter/setter/isser to field name. */
	public static String toFieldName(String methodName) {
		int prefixLength = (methodName.startsWith("is") ? 2 : 3);
		return methodName.substring(prefixLength, prefixLength + 1).toLowerCase()
				+ methodName.substring(prefixLength + 1);
	}

	/**
	 * Example: for field street it will generate string Street used to
	 * construct getter/setter.
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getterSetterDeterminator(String fieldName) {
		if (fieldName.length() == 1) {
			return fieldName.toUpperCase();
		}
		return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
	}

	public static JMethod getGetter(JClassType beanType, String fieldName) throws NotFoundException {
		JMethod method = null;
		try {
			try {
				method = beanType.getMethod("get" + getterSetterDeterminator(fieldName), new JType[0]);
			} catch (NotFoundException e) {
				method = beanType.getMethod("is" + getterSetterDeterminator(fieldName), new JType[0]);
			}
		} catch (NotFoundException e) {
			JClassType superClass = beanType.getSuperclass();
			if (superClass != null) {
				try {
					method = getGetter(superClass, fieldName);
				} catch (NotFoundException e1) {
					JClassType[] interfaces = beanType.getImplementedInterfaces();
					if (interfaces != null && interfaces.length > 0) {
						for (JClassType intrface : interfaces) {
							method = getGetter(intrface, fieldName);
							if (method != null) {
								break;
							}
						}
					}
				}
			}
		}

		return method;
	}

	/**
	 * recursively calls {@link RebindUtils#getGetter(JClassType, String)} to
	 * handle "more-dots strings"<br />
	 * e.g.: address.street returns getAddress().getStreet
	 * 
	 * @param beanType
	 * @param fieldName
	 * @return string - chain of getter methods
	 * @throws NotFoundException
	 */
	public static String getGetterForMoreDotsInBeanTable(JClassType beanType, String fieldName)
			throws NotFoundException {
		JMethod method = null;
		int dotIndex = fieldName.indexOf(".");
		if (dotIndex == -1) {
			method = getGetter(beanType, fieldName);
			if (method == null)
				throw new NotFoundException();
			return method.getName();
		} else {
			method = getGetter(beanType, fieldName.substring(0, dotIndex));
			return method.getName()
					+ "()."
					+ getGetterForMoreDotsInBeanTable(method.getReturnType().isClass(), fieldName
							.substring(dotIndex + 1));
		}
	}

	public static FieldDeclaration getFieldDeclaration(JClassType beanType, String fieldName)
			throws NotFoundException {
		FieldDeclaration fd = new FieldDeclaration();

		Field f;

		try {
			f = Class.forName(beanType.getQualifiedBinaryName()).getDeclaredField(fieldName);
		} catch (Exception e) {
			try {
				f = Class.forName(beanType.getQualifiedBinaryName()).getField(fieldName);
			} catch (Exception e1) {
				f = null;
			}
		}

		if (f != null) {
			fd.isPublic = (f.getModifiers() & Modifier.PUBLIC) == f.getModifiers();
			fd.annotations = f.getAnnotations();
		}

		JField field = beanType.getField(fieldName);

		if (field == null) {
			throw new NotFoundException("Unable to access field " + fieldName + " in " + beanType);
		}

		fd.type = field.getType();

		if (!fd.isPublic) {
			String setterName = "set" + getterSetterDeterminator(fieldName);

			for (JMethod method : beanType.getMethods()) {
				if (method.getName().equals(setterName) && method.getParameters().length == 1) {
					fd.type = method.getParameters()[0].getType();
					fd.setterMethod = method;
					break;
				}
			}

			try {
				fd.getterMethod = getGetter(beanType, fieldName);
			} catch (NotFoundException e) {
				// No getter is defined. Nevermind, we will handle it in some
				// mystical way.
				fd.getterMethod = null;
			}
		}

		return fd;
	}

	public static JMethod getSetter(JClassType beanType, String fieldName, JType fieldType)
			throws NotFoundException {

		JMethod method = null;
		try {
			method = beanType.getMethod("set" + getterSetterDeterminator(fieldName),
					new JType[] { fieldType });
			if (method.isPrivate())
				method = null;
		} catch (NotFoundException e) {
			JClassType superClass = beanType.getSuperclass();
			if (superClass != null) {
				try {
					method = getSetter(superClass, fieldName, fieldType);
				} catch (NotFoundException e1) {
					JClassType[] interfaces = beanType.getImplementedInterfaces();
					if (interfaces != null && interfaces.length > 0) {
						for (JClassType intrface : interfaces) {
							method = getSetter(intrface, fieldName, fieldType);
							if (method != null) {
								break;
							}
						}
					}
				}
			}
		}
		return method;
	}

	/**
	 * Searches class and all its superclasses for a property.
	 * 
	 * @param classType
	 * @param property
	 *            May contain dots
	 * @return
	 * @throws NotFoundException
	 */
	private static Object getDeclaredField(JClassType classType, String property) throws NotFoundException {
		int dotIndex = property.indexOf(".");
		if (dotIndex == -1) {
			return getDeclaredDirectField(classType, property);
		} else {
			Object propertyRepresentation = getDeclaredDirectField(classType, property.substring(0, dotIndex));
			JClassType propertyType;
			if (propertyRepresentation instanceof JField) {
				JField field = (JField) propertyRepresentation;
				if (!(field.getType() instanceof JClassType)) {
					throw new RuntimeException("Not of JClassType type = " + field);
				}
				propertyType = (JClassType) field.getType();
			} else if (propertyRepresentation instanceof JMethod) {
				JMethod method = (JMethod) propertyRepresentation;
				if (!(method.getReturnType() instanceof JClassType)) {
					throw new RuntimeException("Not of JClassType type = " + method);
				}
				propertyType = (JClassType) method.getReturnType();
			} else {
				throw new NotFoundException("Unsupported property type = "
						+ propertyRepresentation.getClass().getName() + ", classType = " + classType
						+ ", property = " + property);
			}
			return getDeclaredField(propertyType, property.substring(dotIndex + 1));

		}

	}

	/**
	 * Searches class and all its superclasses for a field.
	 * 
	 * @param classType
	 * @param fieldName
	 * @return field or method containing representing the field
	 * @throws NotFoundException
	 */
	public static Object getDeclaredDirectField(JClassType classType, String fieldName)
			throws NotFoundException {
		JField field = classType.getField(fieldName);
		JClassType superClass = classType.getSuperclass();
		JMethod getter = null;
		if (field == null) {
			try {
				getter = getGetter(classType, fieldName);
			} catch (Exception e) {}
		}
		if (field == null && getter == null && superClass != null) {
			return getDeclaredDirectField(superClass, fieldName);
		}
		if (field != null) {
			return field;
		} else if (getter != null) {
			return getter;
		}
		throw new NotFoundException(
				"Unable to identify a property descriptor (field or method) for classType = " + classType
						+ ", field = " + fieldName);
	}

	/**
	 * Will it comment later :)
	 * 
	 * @throws NotFoundException
	 */
	public static JClassType getDeclaredFieldClassType(JClassType classType, String property)
			throws NotFoundException {
		JType fieldType = getDeclaredFieldType(classType, property);

		if (fieldType instanceof JClassType) {
			return ((JClassType) fieldType);
		}

		return null;
	}

	/**
	 * Will it comment it aprox. 2 minutes before getDeclaredFieldClassType
	 * method
	 * 
	 * @throws NotFoundException
	 */
	public static JType getDeclaredFieldType(JClassType classType, String property) throws NotFoundException {
		Object propertyRepresentation = RebindUtils.getDeclaredField(classType, property);

		if (propertyRepresentation == null) {
			return null;
		}

		if (propertyRepresentation instanceof JField) {
			return ((JField) propertyRepresentation).getType();
		} else if (propertyRepresentation instanceof JMethod) {
			return ((JMethod) propertyRepresentation).getReturnType();
		} else {
			throw new NotFoundException("Unsupported property type = "
					+ propertyRepresentation.getClass().getName() + ", classType = " + classType
					+ ", property = " + property);
		}
	}

	/**
	 * Tries to find parametrized type in interface hierarchy of a class.
	 * 
	 * @param classType
	 *            A class having parametrized interface.
	 * @param parametrizedType
	 *            Interface where parametrized type is present.
	 * @param position
	 *            Position in a list of parametrized types in interface.
	 * @return Generics type in interface implemented by a class type or null if
	 *         not found
	 */
	private static JClassType getGenericsFromInterfaceHierarchy(JClassType classType,
			JClassType parametrizedType, int position) {
		for (JClassType type : classType.getImplementedInterfaces()) {
			if (type instanceof JParameterizedType) {
				JParameterizedType paramType = (JParameterizedType) type;
				if (paramType.getErasedType().equals(parametrizedType.getErasedType())) {
					return paramType.getTypeArgs()[position];
				}
			}
			// seach parent interfaces
			JClassType result = getGenericsFromInterfaceHierarchy(type, parametrizedType, position);
			if (result != null)
				return result;
		}
		return null;
	}

	/**
	 * Tries to find parametrized type in interface of a class.
	 * 
	 * @param classType
	 *            A class having parametrized interface.
	 * @param parametrizedType
	 *            Interface where parametrized type is present.
	 * @param position
	 *            Position in a list of parametrized types in interface.
	 * @return Generics type in interface implemented by a class type.
	 * @throws NotFoundException
	 */
	public static JClassType getGenericsFromInterfaceType(JClassType classType, JClassType parametrizedType,
			int position) throws NotFoundException {
		JClassType type = getGenericsFromInterfaceHierarchy(classType, parametrizedType, position);
		if (type != null)
			return type;

		if (classType.getSuperclass() != null) {
			return getGenericsFromInterfaceType(classType.getSuperclass(), parametrizedType, position);
		}

		throw new NotFoundException("Unable to find generics in type " + classType
				+ ", where parametrized type is " + parametrizedType + " on position " + position);
	}

	/**
	 * Same as getGenericsFromInterfaceType but it extracts generics from
	 * superclass
	 */
	public static JClassType getGenericsFromSuperclassType(JClassType classType,
	/* JClassType[] parametrizedTypes, */int position) throws NotFoundException {
		JClassType superclassType = classType.getSuperclass();

		if (superclassType == null) {
			// String parametrizedTypesText = "[";
			//
			// int index = 0;
			// for (JClassType parametrizedType : parametrizedTypes) {
			// if (index > 0) {
			// parametrizedTypesText += ", ";
			// }
			// parametrizedTypesText +=
			// parametrizedType.getQualifiedSourceName();
			// index++;
			// }

			// parametrizedTypesText += "]";

			throw new NotFoundException("Unable to find generics in type " + classType + /*
																						 * ", where parametrized types are "
																						 * +
																						 * parametrizedTypesText
																						 * +
																						 */" on position "
					+ position);
		}

		if (superclassType instanceof JParameterizedType) {
			JParameterizedType paramType = (JParameterizedType) superclassType;
			// for (JClassType parametrizedType : parametrizedTypes) {
			// if
			// (paramType.getErasedType().equals(parametrizedType.getErasedType()))
			// {
			return paramType.getTypeArgs()[position];
			// }
			// }
		}

		return getGenericsFromSuperclassType(superclassType/*
															 * ,
															 * parametrizedTypes
															 */, position);
	}

	@Deprecated
	public static String getComparableMethodDeclaration(JMethod method) {
		StringBuilder sb = new StringBuilder();

		// ignoring modifiers and type parameters, they are not significant for
		// method signature comparision

		// ignoring also return type because there cannot co-exist two methods with different return types and same name
//		sb.append(method.getReturnType().getParameterizedQualifiedSourceName());
//		sb.append(" ");
		
		sb.append(method.getName());

		JParameter[] params = method.getParameters();

		sb.append("(");
		boolean needComma = false;
		for (int i = 0, c = params.length; i < c; ++i) {
			JParameter param = params[i];
			if (needComma) {
				sb.append(", ");
			} else {
				needComma = true;
			}
			if (method.isVarArgs() && i == c - 1) {
				JArrayType arrayType = param.getType().isArray();
				assert (arrayType != null);
				sb.append(arrayType.getComponentType().getParameterizedQualifiedSourceName());
				sb.append("...");
			} else {
				sb.append(param.getType().getParameterizedQualifiedSourceName());
			}
		}
		sb.append(")");

		if (method.getThrows() != null && method.getThrows().length > 0) {
			sb.append(" throws ");
			needComma = false;
			for (JType thrownType : method.getThrows()) {
				if (needComma) {
					sb.append(", ");
				} else {
					needComma = true;
				}
				sb.append(thrownType.getParameterizedQualifiedSourceName());
			}
		}

		return sb.toString();
	}
}
