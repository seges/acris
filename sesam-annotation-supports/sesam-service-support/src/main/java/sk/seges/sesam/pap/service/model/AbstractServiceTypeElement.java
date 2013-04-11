package sk.seges.sesam.pap.service.model;

import java.util.List;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public abstract class AbstractServiceTypeElement extends DelegateMutableDeclaredType {

	public void printMethodTypeVariablesDefinition(List<MutableTypeMirror> types, FormattedPrintWriter pw) {
		
		boolean first = true;
		boolean generated = false;
				
		for (MutableTypeMirror type: types) {
			if (type.getKind().equals(MutableTypeKind.TYPEVAR)) {
				MutableTypeVariable typeVariable = (MutableTypeVariable)type;
				
				boolean found = false;
				for (MutableTypeVariable parameterElement: getTypeVariables()) {
					if (parameterElement.getVariable().toString().equals(typeVariable.getVariable())) {
						found = true;
						break;
					}
				}
				
				if (!found) {
					generated = true;
					if (first) {
						pw.print("<");
						first = false;
					} else {
						pw.print(", ");
					}
					
					pw.print(getTypeVariable(typeVariable));
				}
			}
		}
		
		if (generated) {
			pw.print("> ");
		}
	}
	
	protected MutableTypeVariable getTypeVariable(MutableTypeVariable variable) {
		return variable;
	}
}