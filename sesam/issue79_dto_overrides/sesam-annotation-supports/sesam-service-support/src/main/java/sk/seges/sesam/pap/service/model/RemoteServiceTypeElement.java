package sk.seges.sesam.pap.service.model;

import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class RemoteServiceTypeElement extends DelegateMutableDeclaredType {

	private final MutableProcessingEnvironment processingEnv;
	private final TypeElement remoteServiceElement;
	private final LocalServiceTypeElement localServiceElement;
	
	public RemoteServiceTypeElement(TypeElement remoteServiceElement, MutableProcessingEnvironment processingEnv) {
		this.remoteServiceElement = remoteServiceElement;
		this.processingEnv = processingEnv;
		this.localServiceElement = new LocalServiceTypeElement(this, processingEnv);
	}

	RemoteServiceTypeElement(TypeElement remoteServiceElement, LocalServiceTypeElement localServiceElement, MutableProcessingEnvironment processingEnv) {
		this.remoteServiceElement = remoteServiceElement;
		this.processingEnv = processingEnv;
		this.localServiceElement = localServiceElement;
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return (MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(remoteServiceElement.asType());
	}
	
	public TypeElement asElement() {
		return remoteServiceElement;
	}
	
	public LocalServiceTypeElement getLocalServiceElement() {
		return localServiceElement;
	}

	public MutableTypeMirror toReturnType(TypeMirror type) {
		switch (type.getKind()) {
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case INT:
		case LONG:
		case DOUBLE:
		case FLOAT:
		case SHORT:
			return processingEnv.getTypeUtils().toMutableType(processingEnv.getTypeUtils().boxedClass((PrimitiveType)type).asType());
		case VOID:
			return processingEnv.getTypeUtils().toMutableType(Void.class);
		case ARRAY:
		case TYPEVAR:
			return toParamType(type);
		case ERROR:
		case NULL:
		case NONE:
		case OTHER:
		case EXECUTABLE:
			processingEnv.getMessager().printMessage(Kind.ERROR, " [ERROR] Cannot unbox type " + type.getKind() + " - unsupported type!");
			return null;
		}

		return stripVariableTypeVariables((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(type));
	}

	public MutableTypeMirror toReturnType(MutableTypeMirror type) {
		switch (type.getKind()) {
		case PRIMITIVE:
			return type;
		case VOID:
			return toReturnType(((MutableDeclaredType)type).asType());
		case ARRAY:
		case TYPEVAR:
			return toParamType(type);
		}

		return stripVariableTypeVariables((MutableDeclaredType)type);
	}

	private MutableDeclaredType stripVariableTypeVariables(MutableDeclaredType type) {
		if (type != null) {
			return type.stripVariableTypeVariables();
		}
		return type;
	}
	
	public MutableTypeMirror toParamType(TypeMirror type) {
		switch (type.getKind()) {
		case TYPEVAR:
			return processingEnv.getTypeUtils().getTypeVariable(((TypeVariable)type).asElement().getSimpleName().toString());
		case ARRAY:
			return processingEnv.getTypeUtils().getArrayType(toParamType(((ArrayType)type).getComponentType()));
		}
		
		return processingEnv.getTypeUtils().toMutableType(type);
	}

	public MutableTypeMirror toParamType(MutableTypeMirror type) {
		switch (type.getKind()) {
		case TYPEVAR:
			return processingEnv.getTypeUtils().getTypeVariable(((MutableTypeVariable)type).getVariable().toString());
		case ARRAY:
			return processingEnv.getTypeUtils().getArrayType(toParamType(((MutableArrayType)type).getComponentType()));
		}
		
		return processingEnv.getTypeUtils().toMutableType(type);
	}

	public void printMethodTypeVariablesDefinition(List<MutableTypeMirror> types, FormattedPrintWriter pw) {
		
		boolean first = true;
		boolean generated = false;
				
		for (MutableTypeMirror type: types) {
			if (type.getKind().equals(MutableTypeKind.TYPEVAR)) {
				MutableTypeVariable typeVariable = (MutableTypeVariable)type;
				
				boolean found = false;
				for (MutableTypeVariable parameterElement: localServiceElement.getTypeVariables()) {
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
					
					pw.print(typeVariable);
				}
			}
		}
		
		if (generated) {
			pw.print("> ");
		}
	}	
}