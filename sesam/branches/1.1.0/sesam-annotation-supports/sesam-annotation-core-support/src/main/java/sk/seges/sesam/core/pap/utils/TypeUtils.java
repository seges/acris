package sk.seges.sesam.core.pap.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class TypeUtils {

	public static MutableDeclaredType[] mergeClassArray(Type[] classes, Set<MutableDeclaredType> classNames, MutableProcessingEnvironment processingEnv) {
		List<MutableDeclaredType> result = new ArrayList<MutableDeclaredType>();

		ListUtils.addUnique(result, TypeUtils.toTypes(classes, processingEnv));
		ListUtils.addUnique(result, classNames);
		
		return result.toArray(new MutableDeclaredType[] {});		
	}

	public static MutableDeclaredType[] mergeClassArray(Type[] originClasses, Type[] newClasses, MutableProcessingEnvironment processingEnv) {
		return mergeClassArray(originClasses, TypeUtils.toTypes(newClasses, processingEnv), processingEnv);
	}

	public static Set<MutableDeclaredType> toTypes(Type[] javaTypes, MutableProcessingEnvironment processingEnv) {
		Set<MutableDeclaredType> result = new HashSet<MutableDeclaredType>();
		for (Type javaType: javaTypes) {
			result.add((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(javaType));
		}
		return result;
	}
}