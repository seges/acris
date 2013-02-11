package sk.seges.sesam.core.pareflection.comparator;

import java.lang.reflect.Method;
import java.util.Comparator;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;

public class MethodComparator implements Comparator<Method> {

	public MethodComparator() {}

	@Override
	public int compare(Method o1, Method o2) {
		return o1.getName().compareTo(o2.getName());
	}	
}