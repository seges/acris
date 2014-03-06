package sk.seges.acris.pap.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Generated;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import com.google.gwt.user.client.rpc.RemoteService;
import sk.seges.acris.core.client.annotation.RemoteServicePath;
import sk.seges.acris.pap.service.configurer.AsyncServiceProcessorConfigurer;
import sk.seges.acris.pap.service.model.AsyncRemoteServiceType;
import sk.seges.sesam.core.pap.comparator.ExecutableComparator;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;
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

	protected boolean isRemoteService(Element element) {

		if (!element.getKind().equals(ElementKind.INTERFACE)) {
			//it is not an interface, remote service should be an interface
			return false;
		}

		if (!ProcessorUtils.implementsType(element.asType(), processingEnv.getElementUtils().getTypeElement(RemoteService.class.getCanonicalName()).asType())) {
		     //it does not implements RemoteService interface
			 return false;
		}

		TypeElement typeElement = (TypeElement)element;

		AsyncRemoteServiceType asyncServiceType = new AsyncRemoteServiceType(new RemoteServiceTypeElement(typeElement, processingEnv));
		TypeElement asyncInterfaceElement = processingEnv.getElementUtils().getTypeElement(asyncServiceType.toString(ClassSerializer.CANONICAL));

		if (asyncInterfaceElement == null) {
			//Async interface does not exists, but maybe it will be generated
			return element.getAnnotation(RemoteServiceDefinition.class) != null || element.getAnnotation(RemoteServicePath.class) != null;
		}

		return true;
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
	
	private MutableTypeVariable toTypeVariable(MutableTypeMirror mutableType) {
		if (mutableType.getKind().isDeclared() ||
			mutableType.getKind().equals(MutableTypeKind.ARRAY)) {
			return processingEnv.getTypeUtils().getTypeVariable(null, mutableType);
		}

		if (mutableType.getKind().equals(MutableTypeKind.TYPEVAR)) {
			return (MutableTypeVariable)mutableType;
		}
		
		throw new RuntimeException("Unsupported return type " + mutableType.getKind() + ". " + mutableType.toString());
	}
	
	@Override
	protected void processElement(ProcessorContext context) {
		
		TypeElement element = context.getTypeElement();

		for (TypeMirror interfaceType: element.getInterfaces()) {
			if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
				Element interfaceElement = ((DeclaredType) interfaceType).asElement();
				if (isRemoteService(interfaceElement)) {
					context.getOutputType().addInterface(new AsyncRemoteServiceType(new RemoteServiceTypeElement((TypeElement)interfaceElement, processingEnv)));
				}
			}
		}

		processMethods(element, context.getPrintWriter(), false);
	}

	protected void processMethods(TypeElement element, FormattedPrintWriter pw, boolean nested) {

		RemoteServiceTypeElement remoteServiceTypeElement = new RemoteServiceTypeElement(element, processingEnv);

		MutableTypes typeUtils = processingEnv.getTypeUtils();
		MutableDeclaredType asyncCallbackMutableType = typeUtils.toMutableType(AsyncCallback.class);

		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		Collections.sort(methods, new ExecutableComparator());

		for (ExecutableElement method: methods) {

			List<MutableTypeMirror> types = new LinkedList<MutableTypeMirror>();

			for (VariableElement parameter: method.getParameters()) {
				types.add(processingEnv.getTypeUtils().toMutableType(parameter.asType()));
			}

			types.add(processingEnv.getTypeUtils().toMutableType(method.getReturnType()));

			remoteServiceTypeElement.printMethodTypeVariablesDefinition(types, pw);

			pw.print("void " + method.getSimpleName().toString() + "(");

			if (method.toString().contains("...")) {
				//there is an vararg used
				processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "There is a vararg parameter used in the remote method " + method.getSimpleName() + " in " +
					element.getQualifiedName().toString() + ". Varargs are forbidden in remote methods because they cannot be used in Async version of the method - cause they are " +
					"the last parameters of the method.");
			}

			int i = 0;
			for (VariableElement parameter: method.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(remoteServiceTypeElement.toParamType(parameter.asType()), " " + parameter.getSimpleName().toString());
				i++;
			}

			TypeMirror returnType = method.getReturnType();

			if (i > 0) {
				pw.print(", ");
			}

			MutableTypeMirror mutableReturnType = remoteServiceTypeElement.toReturnType(returnType);

			pw.print(typeUtils.getDeclaredType(asyncCallbackMutableType, toTypeVariable(mutableReturnType)), " callback) ");
			if (method.getThrownTypes() != null && !method.getThrownTypes().isEmpty()) {
				pw.print("throws ");
				i = 0;
				for (TypeMirror throwsException : method.getThrownTypes()) {
					if (i > 0) {
						pw.print(", ");
					}
					pw.print(throwsException);
					i++;
				}
			}
			pw.print(";");
			pw.println();
			pw.println();
		}

		for (TypeMirror interfaceType: element.getInterfaces()) {
			if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
				Element interfaceElement = ((DeclaredType) interfaceType).asElement();
				if (nested || !isRemoteService(interfaceElement)) {
					processMethods((TypeElement)interfaceElement, pw, true);
				}
			}
		}

	}
}