package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.model.InputClass.HasTypeParameters;
import sk.seges.sesam.core.pap.model.TypedClass.TypeParameter;

class TypedUtils {

	static String toString(HasTypeParameters hasTypeParameters) {
		String types = "<";
		
		int i = 0;
		
		for (TypeParameter typeParameter: hasTypeParameters.getTypeParameters()) {
			if (i > 0) {
				types += ", ";
			}
			types += typeParameter.toString();
			i++;
		}
		
		types += ">";
		
		return types;
	}
}
