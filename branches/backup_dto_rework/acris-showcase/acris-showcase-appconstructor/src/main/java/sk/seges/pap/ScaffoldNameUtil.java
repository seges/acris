package sk.seges.pap;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

public class ScaffoldNameUtil {
	public static void prefixIfEnclosed(MutableDeclaredType inputType) {
		if(inputType.getEnclosedClass() != null) {
			inputType.addClassPrefix(inputType.getEnclosedClass().getSimpleName() + "_");
		}
	}
}
