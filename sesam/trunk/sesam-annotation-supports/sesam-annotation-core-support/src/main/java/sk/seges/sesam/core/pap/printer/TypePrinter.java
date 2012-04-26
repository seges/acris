package sk.seges.sesam.core.pap.printer;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class TypePrinter {

	private final FormattedPrintWriter pw;
	private final MutableProcessingEnvironment processingEnv;
	
	public TypePrinter(MutableProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		this.pw = pw;
		this.processingEnv = processingEnv;
	}

	public void printTypeDefinition(MutableDeclaredType type) {
		printTypeDefinition(Modifier.PUBLIC, type);
	}
	
	public void printTypeDefinition(Modifier modifier, MutableDeclaredType type) {
		if (modifier != null) {
			pw.print(modifier.name().toLowerCase() + " ");
		}
		pw.print(type.getKind().toString() + " " + type.toString(ClassSerializer.SIMPLE, false));
		
		MutableDeclaredType superClassType = type.getSuperClass();
		
		if (type.getTypeVariables().size() > 0) {
			pw.print("<");

			int i = 0;

			for (MutableTypeVariable typeParameter : type.getTypeVariables()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(typeParameter);
				i++;
			}

			pw.print(">");
		}
		
		if (superClassType != null && !superClassType.toString(ClassSerializer.CANONICAL).equals(Object.class.getCanonicalName()) && !type.getKind().equals(MutableTypeKind.INTERFACE)) {
			pw.print(" extends ", superClassType);
		}

		if (type.getInterfaces() != null && type.getInterfaces().size() > 0) {

			boolean supportedType = false;
			
			if (type.getKind().equals(MutableTypeKind.CLASS)) {
				pw.print(" implements ");
				supportedType = true;
			} else 	if (type.getKind().equals(MutableTypeKind.INTERFACE)) {
				pw.print(" extends ");
				supportedType = true;
			}

			if (supportedType) {
				int i = 0;

				if (superClassType != null && !superClassType.toString(ClassSerializer.CANONICAL).equals(Object.class.getCanonicalName()) && 
						type.getKind().equals(MutableTypeKind.INTERFACE)) {
					pw.print(toPrintableType(superClassType));
					i++;
				}
				
				for (MutableTypeMirror interfaceType : type.getInterfaces()) {
					if (i > 0) {
						pw.print(", ");
					}
					pw.print(toPrintableType(interfaceType));
					i++;
				}
			}
		} else if (superClassType != null && !superClassType.toString(ClassSerializer.CANONICAL).equals(Object.class.getCanonicalName()) && 
						type.getKind().equals(MutableTypeKind.INTERFACE)) {
			pw.print(" extends ");
			pw.print(superClassType);
		}
	}
	
	protected MutableTypeMirror toPrintableType(MutableTypeMirror mutableType) {
		if (mutableType.getKind().isDeclared()) {
			MutableDeclaredType declaredType = (MutableDeclaredType)mutableType;
			List<? extends MutableTypeVariable> typeVariables = declaredType.getTypeVariables();
			List<MutableTypeVariable> strippedTypeVariables = new ArrayList<MutableTypeVariable>();
			for (MutableTypeVariable typeVariable: typeVariables) {
				if (typeVariable.getVariable() != null) {
					strippedTypeVariables.add(processingEnv.getTypeUtils().getTypeVariable(typeVariable.getVariable()));
				} else {
					strippedTypeVariables.add(typeVariable);
				}
			}
			return declaredType.clone().setTypeVariables(strippedTypeVariables.toArray(new MutableTypeVariable[] {}));
		}
		
		return mutableType;
	}

}
