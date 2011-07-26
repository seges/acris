package sk.seges.sesam.pap.service;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.utils.TransferObjectConfiguration;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceInterfaceProcessor extends AbstractConfigurableProcessor {
	
	public static final String REMOTE_SUFFIX = "Remote";
	public static final String LOCAL_SUFFIX = "Local";
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ServiceInterfaceProcessorConfigurer();
	}
	
//	@Override
//	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
//		switch (type) {
//		case OUTPUT_INTERFACES:
//			return new Type[] {
//					RemoteService.class
//			};
//		}
//		return super.getOutputDefinition(type, typeElement);
//	}
	
	public static ImmutableType getOutputClass(ImmutableType mutableType) {	
		String simpleName = mutableType.getSimpleName();
		if (simpleName.endsWith(REMOTE_SUFFIX)) {
			simpleName = simpleName.substring(0, simpleName.length() - REMOTE_SUFFIX.length());
		}
		return mutableType.setName(simpleName + LOCAL_SUFFIX);
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
	protected Type[] getImports(TypeElement typeElement) {

		List<ExecutableElement> methodsIn = ElementFilter.methodsIn(typeElement.getEnclosedElements());
		
		List<Type> imports = new ArrayList<Type>();
		
		for (ExecutableElement method: methodsIn) {
			imports.add(getDomainClass(method.getReturnType(), typeElement));
			for (VariableElement parameter: method.getParameters()) {
				imports.add(getDomainClass(parameter.asType(), typeElement));
			}
		}

		return imports.toArray(new Type[] {});
	}

	@Override
	protected void processElement(TypeElement typeElement, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {

		List<ExecutableElement> methods = ElementFilter.methodsIn(typeElement.getEnclosedElements());
		
		for (ExecutableElement method: methods) {
			pw.print(getDomainClass(method.getReturnType(), typeElement).toString(null, ClassSerializer.SIMPLE, true) + " " + method.getSimpleName().toString() + "(");
			
			int i = 0;
			for (VariableElement parameter: method.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(getDomainClass(parameter.asType(), typeElement).toString(null, ClassSerializer.SIMPLE, true) + " " + parameter.getSimpleName().toString());
				i++;
			}
			
			pw.println(");");
			pw.println();
		}
	}
	
	protected NamedType getDomainClass(TypeMirror type, TypeElement typeElement) {
		switch (type.getKind()) {
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
		case VOID:
			return getNameTypes().toType(type);
		case ERROR:
		case EXECUTABLE:
		case NONE:
		case NULL:
		case OTHER:
		case PACKAGE:
		case WILDCARD:
			processingEnv.getMessager().printMessage(Kind.ERROR, " [ERROR] Unsupported type kind - " + type.getKind());
			return null;
		case DECLARED:
			Element element = ((DeclaredType)type).asElement();
			TypeElement domainClass = new TransferObjectConfiguration(element, processingEnv).getDomainType();
			if (domainClass != null) {
				return convertTypeParameters(getNameTypes().toType(domainClass.asType()), typeElement);
			}
			domainClass = new TransferObjectConfiguration(typeElement, processingEnv).getDomainType((TypeElement)element);
			if (domainClass != null) {
				return convertTypeParameters(getNameTypes().toType(domainClass.asType()), typeElement);
			}
			return convertTypeParameters(getNameTypes().toType(type), typeElement);
		case ARRAY:
			return new ArrayNamedType(getDomainClass(((ArrayType)type).getComponentType(), typeElement));
		case TYPEVAR:
			return getNameTypes().toType(ProcessorUtils.erasure(typeElement, ((TypeVariable)type).asElement().getSimpleName().toString()));
		}

		return getNameTypes().toType(type);
	}

	protected NamedType convertTypeParameters(NamedType type, TypeElement typeElement) {
		if (type instanceof HasTypeParameters) {
			
			List<TypeParameter> domainParameters = new ArrayList<TypeParameter>();
			HasTypeParameters paramsType = ((HasTypeParameters)type);
			
			for (TypeParameter typeParameter: paramsType.getTypeParameters()) {
				for (sk.seges.sesam.core.pap.model.api.TypeVariable bound: typeParameter.getBounds()) {
					domainParameters.add(
							TypeParameterBuilder.get(typeParameter.getVariable(), 
								getDomainClass(processingEnv.getElementUtils().getTypeElement(
										getNameTypes().toType(bound.getUpperBound()).getCanonicalName()).asType(), typeElement)));
				}
			}
			
			return TypedClassBuilder.get(type, domainParameters.toArray(new TypeParameter[] {}));
		}
		
		return type;
	}
}