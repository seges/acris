package sk.seges.sesam.core.pap.comparator;

import java.util.Comparator;

import javax.lang.model.element.ExecutableElement;

public class ExecutableComparator implements Comparator<ExecutableElement> {
	
	public ExecutableComparator() {}
	
	@Override
	public int compare(ExecutableElement o1, ExecutableElement o2) {
		return o1.getSimpleName().toString().compareTo(o2.getSimpleName().toString());
	}
}