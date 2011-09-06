package sk.seges.sesam.pap.model.printer.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleTypeVisitor6;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class ConverterProviderPrinter {

	private final FormattedPrintWriter pw;
	private final TypeParametersSupport typeParametersSupport;
	private final NameTypesUtils nameTypesUtils;
	
	private final ParametersResolver parametersResolver;
	private final ProcessingEnvironment processingEnv;
	private final RoundEnvironment roundEnv;
	
	private Map<String, ConverterTypeElement> converterCache = new HashMap<String, ConverterTypeElement>();
	private final ConfigurationProvider[] configurationProviders;
	
	public ConverterProviderPrinter(FormattedPrintWriter pw, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ParametersResolver parametersResolver, ConfigurationProvider... configurationProviders) {
		this.pw = pw;
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.configurationProviders = configurationProviders;
		this.parametersResolver = parametersResolver;
		this.nameTypesUtils = new NameTypesUtils(processingEnv);
		
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, nameTypesUtils);
	}

	interface ParameterPrinter {
		
		void print(TypeParameterElement parameter, FormattedPrintWriter pw);

		void print(TypeParameter parameter, FormattedPrintWriter pw);
	}

	class ParameterTypesPrinter implements ParameterPrinter {

		@Override
		public void print(TypeParameterElement parameter, FormattedPrintWriter pw) {
			pw.print(nameTypesUtils.toType(parameter));
		}

		@Override
		public void print(TypeParameter parameter, FormattedPrintWriter pw) {
			pw.print(parameter);
		}		
	}
	
	class ParameterNamesPrinter implements ParameterPrinter {

		@Override
		public void print(TypeParameterElement parameter, FormattedPrintWriter pw) {
			pw.print(parameter.getSimpleName().toString());
		}

		@Override
		public void print(TypeParameter parameter, FormattedPrintWriter pw) {
			pw.print(parameter.getSimpleName());
		}
		
	}
	
	protected String getParameterName(ConverterParameter parameter) {
		if (parameter.getSameParameter() != null) {
			return parameter.getSameParameter().getName();
		}

		return parameter.getName();
	}

	protected void printTypeParameters(ConverterTypeElement converterTypeElement, ParameterPrinter parameterPrinter) {
		pw.print("<");
		int i = 0;

		if (converterTypeElement.asElement() != null) {
			for (TypeParameterElement converterTypeParameter: converterTypeElement.asElement().getTypeParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				parameterPrinter.print(converterTypeParameter, pw);
				i++;
			}
		} else {
			for (TypeParameter converterTypeParameter: converterTypeElement.getTypeParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				parameterPrinter.print(converterTypeParameter, pw);
				i++;
			}
		}
		pw.print(">");
	}

	protected TypeParameter[] toTypeParameters(ConverterTypeElement converterTypeElement) {
		
		List<TypeParameter> result = new ArrayList<TypeParameter>();

		if (converterTypeElement.asElement() != null) {
			for (TypeParameterElement converterTypeParameter: converterTypeElement.asElement().getTypeParameters()) {
				result.add(TypeParameterBuilder.get(converterTypeParameter.getSimpleName().toString()));
			}
		} else {
			for (TypeParameter converterTypeParameter: converterTypeElement.getTypeParameters()) {
				result.add(TypeParameterBuilder.get(converterTypeParameter.getSimpleName()));
			}
		}
		return result.toArray(new TypeParameter[] {});
	}

	
	public void printConverterMethods() {
		for (Entry<String, ConverterTypeElement> converterEntry: converterCache.entrySet()) {
			printConverterMethod(converterEntry.getValue(), converterEntry.getKey());
		}
	}
	
	private void printConverterMethod(ConverterTypeElement converterTypeElement, String convertMethod) {

		List<ConverterParameter> converterParameters = converterTypeElement.getConverterParameters(parametersResolver);

		pw.print("private");
		
		NamedType converterReplacedTypeParameters = converterTypeElement;

		if (typeParametersSupport.hasTypeParameters(converterTypeElement)) {
			printTypeParameters(converterTypeElement, new ParameterTypesPrinter());
			converterReplacedTypeParameters = TypedClassBuilder.get(converterTypeElement, toTypeParameters(converterTypeElement));
		}
		
		pw.print(" ", converterReplacedTypeParameters);
		
//		if (typeParametersSupport.hasTypeParameters(converterTypeElement)) {
//			printTypeParameters(converterTypeElement, new ParameterNamesPrinter());
//		}
		pw.print(" " + convertMethod + "(");

		int i = 0;
		for (ConverterParameter converterParameter: converterParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			if (converterParameter.isConverter()) {
				
				NamedType parameterReplacedTypeParameters = TypedClassBuilder.get(converterParameter.getType(), toTypeParameters(converterTypeElement));

//				pw.print(converterParameter.getType().toString(ClassSerializer.CANONICAL, false));
				//TODO, print only 2 params from specific index (or number of the parameters that are required for the converter)
//				printTypeParameters(converterTypeElement, new ParameterNamesPrinter());
				pw.print(parameterReplacedTypeParameters, " " + converterParameter.getName());
				i++;
			}
		}
		
		pw.println(") {");
		pw.print("return new ", converterReplacedTypeParameters);

//		if (typeParametersSupport.hasTypeParameters(converterTypeElement)) {
//			printTypeParameters(converterTypeElement, new ParameterNamesPrinter());
//		}
		
		pw.print("(");

		i = 0;
		for (ConverterParameter parameter : converterParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(getParameterName(parameter));
			i++;
		}

		pw.println(");");
		pw.println("}");
		pw.println();
	}
	
	protected String getConverterMethodName(ConverterTypeElement converterTypeElement) {
		if (converterTypeElement == null) {
			return null;
		}

		String convertMethod = "get" + converterTypeElement.getSimpleName();

		if (converterCache.containsKey(convertMethod)) {
			return convertMethod;
		}
		
		converterCache.put(convertMethod, converterTypeElement);
		
		return convertMethod;
	}
	
	public String getDomainConverterMethodName(ConverterTypeElement converterTypeElement, TypeMirror domainType) {
		
		String methodName = getConverterMethodName(converterTypeElement);
		
		if (methodName == null) {
			return null;
		}
		
		methodName = methodName + "(";
		
		if (domainType.getKind().equals(TypeKind.DECLARED) && typeParametersSupport.hasTypeParameters(converterTypeElement)) {
			if (((DeclaredType)domainType).getTypeArguments().size() > 0) {
				int i = 0;
				for (TypeMirror typeArgumentMirror: ((DeclaredType)domainType).getTypeArguments()) {
					String methodParameter = typeArgumentMirror.accept(new SimpleTypeVisitor6<String, Integer>(){
						@Override
						public String visitDeclared(DeclaredType t, Integer i) {
							return getConverterParameter(t, i);
						}
						
						@Override
						public String visitWildcard(WildcardType t, Integer i) {
							String result = "";
							if (t.getExtendsBound() != null) {
								result = getConverterParameter(t.getExtendsBound(), i);
							} else if (t.getSuperBound() != null) {
								result = getConverterParameter(t.getSuperBound(), i);
							}
							return result;
							
						}
						
						private String getConverterParameter(TypeMirror type, int i) {
							String result = "";

							if (i > 0) {
								result += ", ";
							}

							DomainTypeElement domainTypeElement = new DomainTypeElement(type, processingEnv, roundEnv, configurationProviders);
							if (domainTypeElement.getConfigurationTypeElement() != null) {
								result += getDomainConverterMethodName(domainTypeElement.getConfigurationTypeElement().getConverterTypeElement(), type);
							} else {
								result += "(" + DtoConverter.class.getCanonicalName() + "<" + type + ", " + type + ">)null";
							}
							
							return result;
						}
					}, i);
					
					methodName += methodParameter;
					i++;
				}
			}
		}
		
		return methodName + ")";
	}


	public String getDtoConverterMethodName(ConverterTypeElement converterTypeElement, TypeMirror dtoType) {
		
		String methodName = getConverterMethodName(converterTypeElement);
		
		if (methodName == null) {
			return null;
		}
		
		methodName = methodName + "(";
		
		if (dtoType.getKind().equals(TypeKind.DECLARED) && typeParametersSupport.hasTypeParameters(converterTypeElement)) {
			if (((DeclaredType)dtoType).getTypeArguments().size() > 0) {
				int i = 0;
				for (TypeMirror typeArgumentMirror: ((DeclaredType)dtoType).getTypeArguments()) {
					String methodParameter = typeArgumentMirror.accept(new SimpleTypeVisitor6<String, Integer>(){
						@Override
						public String visitDeclared(DeclaredType t, Integer i) {
							return getConverterParameter(t, i);
						}
						
						@Override
						public String visitWildcard(WildcardType t, Integer i) {
							String result = "";
							if (t.getExtendsBound() != null) {
								result = getConverterParameter(t.getExtendsBound(), i);
							} else if (t.getSuperBound() != null) {
								result = getConverterParameter(t.getSuperBound(), i);
							}
							return result;
							
						}
						
						private String getConverterParameter(TypeMirror type, int i) {
							String result = "";

							if (i > 0) {
								result += ", ";
							}

							DtoTypeElement dtoTypeElement = new DtoTypeElement(type, processingEnv, roundEnv, configurationProviders);
							if (dtoTypeElement.getConfiguration() != null) {
								result += getDomainConverterMethodName(dtoTypeElement.getConverter(), type);
							} else {
								result += "(" + DtoConverter.class.getCanonicalName() + "<" + type + ", " + type + ">)null";
							}
							
							return result;
						}
					}, i);
					
					methodName += methodParameter;
					i++;
				}
			}
		}
		
		return methodName + ")";
	}
}