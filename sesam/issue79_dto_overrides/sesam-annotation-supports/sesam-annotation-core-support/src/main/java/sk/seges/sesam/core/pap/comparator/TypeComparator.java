package sk.seges.sesam.core.pap.comparator;

import java.util.Comparator;

import javax.lang.model.element.TypeElement;

public class TypeComparator implements Comparator<TypeElement> {
	
	public TypeComparator() {}
	
	@Override
	public int compare(TypeElement o1, TypeElement o2) {
		return o1.getSimpleName().toString().compareTo(o2.getSimpleName().toString());
	}
}