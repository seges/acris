package sk.seges.acris.pap.service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Generated;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.acris.core.client.annotation.RemoteServicePath;
import sk.seges.acris.pap.service.configurer.AsyncServiceProcessorConfigurer;
import sk.seges.acris.pap.service.model.AsyncRemoteServiceType;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class AsyncServiceProcessor extends MutableAnnotationProcessor {

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new AsyncRemoteServiceType(new RemoteServiceTypeElement(context.getTypeElement(), processingEnv))
		};
	}
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new AsyncServiceProcessorConfigurer();
	}	
	
	@Override
	protected void printAnnotations(ProcessorContext context) {
		FormattedPrintWriter pw = context.getPrintWriter();
		TypeElement el = context.getTypeElement();
		RemoteServiceRelativePath remoteServiceRelativePath = el.getAnnotation(RemoteServiceRelativePath.class);
		if (remoteServiceRelativePath != null) {
			pw.println("@", RemoteServiceRelativePath.class, "(\"" + remoteServiceRelativePath.value() + "\")");
		}
		RemoteServicePath remoteServicePath = el.getAnnotation(RemoteServicePath.class);
		if (remoteServicePath != null) {
			pw.println("@", RemoteServicePath.class, "(\"" + remoteServicePath.value() + "\")");
		}
		super.printAnnotations(context);
	}
	
	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		return context.getTypeElement().getAnnotation(Generated.class) == null;
	}
	
	@Override
	protected void processElement(ProcessorContext context) {
		
		FormattedPrintWriter pw = context.getPrintWriter();
		TypeElement element = context.getTypeElement();
		
		List<ExecutableElement> methodsIn = ElementFilter.methodsIn(element.getEnclosedElements());
		
		for (ExecutableElement method: methodsIn) {
			
			printMethodTypeVariablesDefinition(element, method, pw);
			
			pw.print("void " + method.getSimpleName().toString() + "(");
			
			int i = 0;
			for (VariableElement parameter: method.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(toMutableParam(parameter.asType()), " " + parameter.getSimpleName().toString());
				i++;
			}
			
			TypeMirror returnType = method.getReturnType();

			if (i > 0) {
				pw.print(", ");
			}
			pw.println(AsyncCallback.class, "<", unBoxType(returnType), "> callback);");
			pw.println();
			//TODO add throws
		}
	}

	protected void printMethodTypeVariablesDefinition(TypeElement remoteElement, ExecutableElement method, FormattedPrintWriter pw) {
		
		boolean first = true;
		boolean generated = false;
		
		List<TypeMirror> types = new LinkedList<TypeMirror>();
		
		for (VariableElement parameter: method.getParameters()) {
			types.add(parameter.asType());
		}
		
		types.add(method.getReturnType());
		
		for (TypeMirror type: types) {
			if (type.getKind().equals(TypeKind.TYPEVAR)) {
				TypeVariable typeVariable = (TypeVariable)type;
				
				boolean found = false;
				for (TypeParameterElement parameterElement: remoteElement.getTypeParameters()) {
					if (parameterElement.getSimpleName().toString().equals(typeVariable.asElement().getSimpleName().toString())) {
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
	
	protected MutableTypeMirror toMutableParam(TypeMirror type) {
		switch (type.getKind()) {
		case TYPEVAR:
			return processingEnv.getTypeUtils().getTypeVariable(((TypeVariable)type).asElement().getSimpleName().toString());
		case ARRAY:
			return processingEnv.getTypeUtils().getArrayType(toMutableParam(((ArrayType)type).getComponentType()));
		}
		
		return processingEnv.getTypeUtils().toMutableType(type);
	}
	
	protected MutableTypeMirror unBoxType(TypeMirror type) {
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
			return processingEnv.getTypeUtils().getArrayType(unBoxType(((ArrayType)type).getComponentType()));
		case TYPEVAR:
			return processingEnv.getTypeUtils().getTypeVariable(((TypeVariable)type).asElement().getSimpleName().toString());
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

	private MutableDeclaredType stripVariableTypeVariables(MutableDeclaredType type) {
		if (type != null) {
			return type.stripVariableTypeVariables();
		}
		return type;
	}
}
