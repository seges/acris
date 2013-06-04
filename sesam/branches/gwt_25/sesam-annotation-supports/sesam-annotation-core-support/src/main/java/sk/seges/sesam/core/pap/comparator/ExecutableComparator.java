package sk.seges.sesam.core.pap.comparator;

import java.util.Comparator;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class ExecutableComparator implements Comparator<ExecutableElement> {
	
	private final boolean ignoreParameters;
	
	public ExecutableComparator() {
		this.ignoreParameters = true;
	}

	public ExecutableComparator(boolean ignoreParameters) {
		this.ignoreParameters = ignoreParameters;
	}

	@Override
	public int compare(ExecutableElement o1, ExecutableElement o2) {
		int result = o1.getSimpleName().toString().compareTo(o2.getSimpleName().toString());
		if (ignoreParameters || result != 0) {
			return result;
		}

		if (result == 0) {
			result = -1;
		}
		
		//processingEnv.getTypeUtils().isAssignable(t1, t2)
		if (/*!o1.getReturnType().toString().equals(o2.getReturnType().toString()) || */o1.getParameters().size() != o2.getParameters().size()) {
			return result;
		}
		
		int i = 0;
		for (VariableElement parameter: o1.getParameters()) {
			if (!parameter.asType().equals(o2.getParameters().get(i).asType())) {
				return result;
			}
			i++;
		}

		return 0;
	}
}