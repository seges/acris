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

		RemoteServiceTypeElement remoteServiceTypeElement = new RemoteServiceTypeElement(element, processingEnv);
		
		List<ExecutableElement> methodsIn = ElementFilter.methodsIn(element.getEnclosedElements());
		
		for (ExecutableElement method: methodsIn) {

			List<MutableTypeMirror> types = new LinkedList<MutableTypeMirror>();
			
			for (VariableElement parameter: method.getParameters()) {
				types.add(processingEnv.getTypeUtils().toMutableType(parameter.asType()));
			}
			
			types.add(processingEnv.getTypeUtils().toMutableType(method.getReturnType()));

			remoteServiceTypeElement.printMethodTypeVariablesDefinition(types, pw);
			
			pw.print("void " + method.getSimpleName().toString() + "(");
			
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
			pw.println(AsyncCallback.class, "<", remoteServiceTypeElement.toReturnType(returnType), "> callback);");
			pw.println();
			//TODO add throws
		}
	}
}