package sk.seges.acris.binding.rebind;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;

/**
 * @author eldzi
 */
public class RebindUtils {
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
			if(superClass != null) {
				try {
					method = getGetter(superClass, fieldName);
				} catch (NotFoundException e1) {
					JClassType[] interfaces = beanType.getImplementedInterfaces();
					if(interfaces != null && interfaces.length > 0) {
						for(JClassType intrface: interfaces) {
							method = getGetter(intrface, fieldName);
							if(method != null) {
								break;
							}
						}
					}
				}
			}
		}
		
		
		return method;
	}
	
	public static JMethod getSetter(JClassType beanType, String fieldName, JType fieldType) throws NotFoundException {
		
		JMethod method = null;
		try {
			method = beanType.getMethod("set" + getterSetterDeterminator(fieldName), null);
		} catch (NotFoundException e) {
			JClassType superClass = beanType.getSuperclass();
			if(superClass != null) {
				try {
					method = getSetter(superClass, fieldName, fieldType);
				} catch (NotFoundException e1) {
					JClassType[] interfaces = beanType.getImplementedInterfaces();
					if(interfaces != null && interfaces.length > 0) {
						for(JClassType intrface: interfaces) {
							method = getSetter(intrface, fieldName, fieldType);
							if(method != null) {
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
	 * Searches class and all its superclasses for a field.
	 * 
	 * @param classType
	 * @param fieldName
	 * @return
	 */
	public static JField getDeclaredField(JClassType classType, String fieldName) {
		JField field = classType.getField(fieldName);
		JClassType superClass = classType.getSuperclass();
		if(field == null && superClass != null) {
			return getDeclaredField(superClass, fieldName);
		}
		return field;
	}
	
	/**
	 * Will it comment later :)
	 */
	public static JClassType getDeclaredFieldClassType(JClassType classType, String property) {
		JType fieldType = getDeclaredFieldType(classType, property);
		
		if (fieldType instanceof JClassType) {
			return ((JClassType)fieldType);
		}
		
		return null;
	}

	/**
	 * Will it comment it aprox. 2 minutes before getDeclaredFieldClassType method
	 */
	public static JType getDeclaredFieldType(JClassType classType, String property) {
		JField field = RebindUtils.getDeclaredField(classType, property);
		
		if (field == null) {
			return null;
		}
		
		return field.getType();
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
	public static JClassType getGenericsFromInterfaceType(JClassType classType, JClassType parametrizedType, int position) throws NotFoundException {
		for(JClassType type: classType.getImplementedInterfaces()) {
			if(type instanceof JParameterizedType) {
				JParameterizedType paramType = (JParameterizedType) type;
				if(paramType.getErasedType().equals(parametrizedType.getErasedType())) {
					return paramType.getTypeArgs()[position];
				}
			}
		}
		
		throw new NotFoundException("Unable to find generics in type " + classType + ", where parametrized type is " + parametrizedType + " on position " + position);
	}

	/**
	 * Same as getGenericsFromInterfaceType but it extracts generics from superclass
	 */
	public static JClassType getGenericsFromSuperclassType(JClassType classType, JClassType[] parametrizedTypes, int position) throws NotFoundException {
		JClassType superclassType = classType.getSuperclass();
		
		if (superclassType == null) {
			String parametrizedTypesText = "[";
			
			int index = 0;
			for (JClassType parametrizedType : parametrizedTypes) {
				if (index > 0) {
					parametrizedTypesText += ", ";
				}
				parametrizedTypesText += parametrizedType.getQualifiedSourceName();
				index++;
			}
			
			parametrizedTypesText += "]";

			throw new NotFoundException("Unable to find generics in type " + classType + ", where parametrized types are " + parametrizedTypesText + " on position " + position);
		}
		
		if(superclassType instanceof JParameterizedType) {
			JParameterizedType paramType = (JParameterizedType) superclassType;
			for (JClassType parametrizedType : parametrizedTypes) {
				if(paramType.getErasedType().equals(parametrizedType.getErasedType())) {
					return paramType.getTypeArgs()[position];
				}
			}
		}

		return getGenericsFromSuperclassType(superclassType, parametrizedTypes, position);
	}

}
