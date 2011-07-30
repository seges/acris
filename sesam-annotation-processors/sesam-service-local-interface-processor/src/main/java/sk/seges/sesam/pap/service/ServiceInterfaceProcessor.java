package sk.seges.sesam.pap.service;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;
import sk.seges.sesam.pap.model.utils.TransferObjectConfiguration.DtoMappingType;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceInterfaceProcessor extends AbstractConfigurableProcessor {
	
	public static final String REMOTE_SUFFIX = "Remote";
	public static final String LOCAL_SUFFIX = "Local";
	
	private TransferObjectHelper toHelper;
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ServiceInterfaceProcessorConfigurer();
	}
	
	public static ImmutableType getOutputClass(ImmutableType mutableType) {	
		String simpleName = mutableType.getSimpleName();
		if (simpleName.endsWith(REMOTE_SUFFIX)) {
			simpleName = simpleName.substring(0, simpleName.length() - REMOTE_SUFFIX.length());
		}
		return mutableType.setName(simpleName + LOCAL_SUFFIX);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		toHelper = new TransferObjectHelper(nameTypesUtils, processingEnv, roundEnv);
		return super.process(annotations, roundEnv);
	}
	
	@Override
	protected ElementKind getElementKind() {
		return ElementKind.INTERFACE;
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] { getOutputClass(mutableType) };
	}

	@Override
	protected void writeClassAnnotations(Element el, NamedType outputName, PrintWriter pw) {
		pw.println("@" + LocalServiceDefinition.class.getSimpleName() + "(remoteService = " + el.toString() + ".class)");
	}
	
	@Override
	protected Type[] getImports(TypeElement typeElement) {

		List<ExecutableElement> methodsIn = ElementFilter.methodsIn(typeElement.getEnclosedElements());
		
		List<Type> imports = new ArrayList<Type>();
		
		for (ExecutableElement method: methodsIn) {
			imports.add(toHelper.getDtoMappingClass(method.getReturnType(), typeElement, DtoMappingType.DOMAIN));
			for (VariableElement parameter: method.getParameters()) {
				imports.add(toHelper.getDtoMappingClass(parameter.asType(), typeElement, DtoMappingType.DOMAIN));
			}
		}

		imports.add(LocalServiceDefinition.class);
		
		return imports.toArray(new Type[] {});
	}

	@Override
	protected void processElement(TypeElement typeElement, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {

		List<ExecutableElement> methods = ElementFilter.methodsIn(typeElement.getEnclosedElements());
		
		for (ExecutableElement method: methods) {
			pw.print(toHelper.getDtoMappingClass(method.getReturnType(), typeElement, DtoMappingType.DOMAIN).toString(null, ClassSerializer.SIMPLE, true) + " " + method.getSimpleName().toString() + "(");
			
			int i = 0;
			for (VariableElement parameter: method.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(toHelper.getDtoMappingClass(parameter.asType(), typeElement, DtoMappingType.DOMAIN).toString(null, ClassSerializer.SIMPLE, true) + " " + parameter.getSimpleName().toString());
				i++;
			}
			
			pw.println(");");
			pw.println();
		}
	}
}