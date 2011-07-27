package sk.seges.sesam.core.pap.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.model.api.NamedType;

public class TypeUtils {

	public static NamedType[] mergeClassArray(Type[] classes, Set<NamedType> classNames, NameTypes nameTypes) {
		List<NamedType> result = new ArrayList<NamedType>();

		ListUtils.addUnique(result, TypeUtils.toTypes(classes, nameTypes));
		ListUtils.addUnique(result, classNames);
		
		return result.toArray(new NamedType[] {});		
	}

	public static NamedType[] mergeClassArray(Type[] originClasses, Type[] newClasses, NameTypes nameTypes) {
		return mergeClassArray(originClasses, TypeUtils.toTypes(newClasses, nameTypes), nameTypes);
	}

	public static Set<NamedType> toTypes(Type[] javaTypes, NameTypes nameTypes) {
		Set<NamedType> result = new HashSet<NamedType>();
		for (Type javaType: javaTypes) {
			result.add(nameTypes.toType(javaType));
		}
		return result;
	}

}
