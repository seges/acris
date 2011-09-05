package sk.seges.sesam.pap.model.printer.converter;

import java.io.PrintWriter;
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
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class ConverterProviderPrinter {

	private final PrintWriter pw;
	private final TypeParametersSupport typeParametersSupport;
	private final NameTypesUtils nameTypesUtils;
	
	private final ParametersResolver parametersResolver;
	private final ProcessingEnvironment processingEnv;
	private final RoundEnvironment roundEnv;
	
	private Map<ConverterTypeElement, String> converterCache = new HashMap<ConverterTypeElement, String>();
	private final ConfigurationProvider[] configurationProviders;
	
	public ConverterProviderPrinter(PrintWriter pw, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ParametersResolver parametersResolver, ConfigurationProvider... configurationProviders) {
		this.pw = pw;
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.configurationProviders = configurationProviders;
		this.parametersResolver = parametersResolver;
		this.nameTypesUtils = new NameTypesUtils(processingEnv);
		
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, nameTypesUtils);
	}
	
	protected String getParameterName(ConverterParameter parameter) {
		if (parameter.getSameParameter() != null) {
			return parameter.getSameParameter().getName();
		}

		return parameter.getName();
	}

	protected void printTypeParameters(ConverterTypeElement converterTypeElement, ClassSerializer serializer, boolean typed) {
		pw.print("<");
		int i = 0;

		if (converterTypeElement.asElement() != null) {
			for (TypeParameterElement converterTypeParameter: converterTypeElement.asElement().getTypeParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				if (!serializer.equals(ClassSerializer.SIMPLE)) {
					pw.print(nameTypesUtils.toType(converterTypeParameter).toString(serializer, typed));
				} else {
					pw.print(converterTypeParameter.getSimpleName().toString());
				}
				i++;
			}
		} else {
			for (TypeParameter converterTypeParameter: converterTypeElement.getTypeParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				if (!serializer.equals(ClassSerializer.SIMPLE)) {
					pw.print(converterTypeParameter.toString(serializer, typed));
				} else {
					pw.print(converterTypeParameter.getSimpleName());
				}
				i++;
			}
		}
		pw.print(">");
	}

	public void printConverterMethods() {
		for (Entry<ConverterTypeElement, String> converterEntry: converterCache.entrySet()) {
			printConverterMethod(converterEntry.getKey(), converterEntry.getValue());
		}
	}
	
	private void printConverterMethod(ConverterTypeElement converterTypeElement, String convertMethod) {

		List<ConverterParameter> converterParameters = converterTypeElement.getConverterParameters(parametersResolver);

		pw.print("private");
		
		if (typeParametersSupport.hasTypeParameters(converterTypeElement)) {
			printTypeParameters(converterTypeElement, ClassSerializer.CANONICAL, true);
		}
		
		pw.print(" " + converterTypeElement.toString(ClassSerializer.CANONICAL, false));
		if (typeParametersSupport.hasTypeParameters(converterTypeElement)) {
			printTypeParameters(converterTypeElement, ClassSerializer.SIMPLE, false);
		}
		pw.print(" " + convertMethod + "(");

		int i = 0;
		for (ConverterParameter converterParameter: converterParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			if (converterParameter.isConverter()) {
				pw.print(converterParameter.getType().toString(ClassSerializer.CANONICAL, false));
				//TODO, print only 2 params from specific index (or number of the parameters that are required for the converter)
				printTypeParameters(converterTypeElement, ClassSerializer.SIMPLE, false);
				pw.print(" " + converterParameter.getName());
				i++;
			}
		}
		
		pw.println(") {");
		pw.print("return new " + converterTypeElement.getCanonicalName());

		if (typeParametersSupport.hasTypeParameters(converterTypeElement)) {
			printTypeParameters(converterTypeElement, ClassSerializer.SIMPLE, false);
		}
		
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

		if (converterCache.containsKey(converterTypeElement)) {
			return converterCache.get(converterTypeElement);
		}
		
		String convertMethod = "get" + converterTypeElement.getSimpleName();
		converterCache.put(converterTypeElement, convertMethod);
		
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

							DomainTypeElement domainTypeElement = new DomainTypeElement(type, processingEnv, roundEnv, configurationProviders);
							if (domainTypeElement.getConfigurationTypeElement() != null) {
								if (i > 0) {
									result += ", ";
								}
								result += getDomainConverterMethodName(domainTypeElement.getConfigurationTypeElement().getConverterTypeElement(), 
										type);
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

							DtoTypeElement dtoTypeElement = new DtoTypeElement(type, processingEnv, roundEnv, configurationProviders);
							if (dtoTypeElement.getConfiguration() != null) {
								if (i > 0) {
									result += ", ";
								}
								result += getDomainConverterMethodName(dtoTypeElement.getConverter(), type);
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