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

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
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
	private final NameTypeUtils nameTypesUtils;
	
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
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
		
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

	protected TypeParameter[] toTypeParameters(ConverterTypeElement converterTypeElement, boolean addExtends) {
		
		List<TypeParameter> result = new ArrayList<TypeParameter>();

		if (converterTypeElement.asElement() != null) {
			for (TypeParameterElement converterTypeParameter: converterTypeElement.asElement().getTypeParameters()) {
				if (addExtends) {
					result.add(TypeParameterBuilder.get("? extends " + converterTypeParameter.getSimpleName().toString()));
				} else {
					result.add(TypeParameterBuilder.get(converterTypeParameter.getSimpleName().toString()));
				}
			}
		} else {
			for (TypeParameter converterTypeParameter: converterTypeElement.getTypeParameters()) {
				if (addExtends) {
					result.add(TypeParameterBuilder.get("? extends " + converterTypeParameter.getSimpleName()));
				} else {
					result.add(TypeParameterBuilder.get(converterTypeParameter.getSimpleName()));
				}
			}
		}
		return result.toArray(new TypeParameter[] {});
	}

	
	public void printConverterMethods(boolean supportExtends) {
		for (Entry<String, ConverterTypeElement> converterEntry: converterCache.entrySet()) {
			printConverterMethod(converterEntry.getValue(), converterEntry.getKey(), supportExtends);
		}
	}
	
	private void printConverterMethod(ConverterTypeElement converterTypeElement, String convertMethod, boolean supportExtends) {

		List<ConverterParameter> converterParameters = converterTypeElement.getConverterParameters(parametersResolver);

		pw.print("private");
		
		NamedType converterReplacedTypeParameters = converterTypeElement;

		if (typeParametersSupport.hasTypeParameters(converterTypeElement)) {
			printTypeParameters(converterTypeElement, new ParameterTypesPrinter());
			converterReplacedTypeParameters = TypedClassBuilder.get(converterTypeElement, toTypeParameters(converterTypeElement, supportExtends));
		}
		
		pw.print(" ", converterReplacedTypeParameters);
		pw.print(" " + convertMethod + "(");

		int i = 0;
		for (ConverterParameter converterParameter: converterParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			if (converterParameter.isConverter()) {
				NamedType parameterReplacedTypeParameters = TypedClassBuilder.get(converterParameter.getType(), toTypeParameters(converterTypeElement, false));
				pw.print(parameterReplacedTypeParameters, " " + converterParameter.getName());
				i++;
			}
		}
		
		pw.println(") {");

		if (typeParametersSupport.hasTypeParameters(converterTypeElement)) {
			converterReplacedTypeParameters = TypedClassBuilder.get(converterTypeElement, toTypeParameters(converterTypeElement, false));
		}
		
		pw.print("return new ", converterReplacedTypeParameters);
		
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
	
	interface TomBaseElementProvider {
		ConverterTypeElement getConverter(TypeMirror type);
		DomainTypeElement getDomainType(TypeMirror type);
		DtoTypeElement getDtoType(TypeMirror type);
	}
	
	class DomainTypeElementProvider implements TomBaseElementProvider {

		@Override
		public ConverterTypeElement getConverter(TypeMirror type) {
			DomainTypeElement domainTypeElement = new DomainTypeElement(type, processingEnv, roundEnv, configurationProviders);
			if (domainTypeElement.getConfigurationTypeElement() != null) {
				return domainTypeElement.getConfigurationTypeElement().getConverterTypeElement();
			}
			return null;
		}

		@Override
		public DomainTypeElement getDomainType(TypeMirror type) {
			return new DomainTypeElement(type, processingEnv, roundEnv, configurationProviders);
		}

		@Override
		public DtoTypeElement getDtoType(TypeMirror type) {
			return new DomainTypeElement(type, processingEnv, roundEnv, configurationProviders).getDtoTypeElement();
		}
		
	}
	
	class DtoTypeElementProvider implements TomBaseElementProvider {

		@Override
		public ConverterTypeElement getConverter(TypeMirror type) {
			DtoTypeElement dtoTypeElement = new DtoTypeElement(type, processingEnv, roundEnv, configurationProviders);
			if (dtoTypeElement.getConfiguration() != null) {
				return dtoTypeElement.getConverter();
			}
			
			return null;
		}

		@Override
		public DomainTypeElement getDomainType(TypeMirror type) {
			return new DtoTypeElement(type, processingEnv, roundEnv, configurationProviders).getDomainTypeElement();
		}

		@Override
		public DtoTypeElement getDtoType(TypeMirror type) {
			return new DtoTypeElement(type, processingEnv, roundEnv, configurationProviders);
		}
	}

	public void printDtoConverterMethodName(ConverterTypeElement converterTypeElement, TypeMirror type, final FormattedPrintWriter pw) {
		printConverterMethodName(converterTypeElement, type, new DtoTypeElementProvider(), pw);
	}

	public void printDomainConverterMethodName(ConverterTypeElement converterTypeElement, TypeMirror type, final FormattedPrintWriter pw) {
		printConverterMethodName(converterTypeElement, type, new DomainTypeElementProvider(), pw);
	}

	private void printConverterMethodName(ConverterTypeElement converterTypeElement, TypeMirror type, final TomBaseElementProvider tomBaseElementProvider, final FormattedPrintWriter pw) {
		
		String methodName = getConverterMethodName(converterTypeElement);
		
		if (methodName == null) {
			return;
		}

		boolean castRequired = false;
		
		if (type.getKind().equals(TypeKind.DECLARED) && typeParametersSupport.hasTypeParameters(converterTypeElement)) {
			
			if (((DeclaredType)type).getTypeArguments().size() > 0) {
				castRequired = true;
				List<NamedType> converterArguments = new ArrayList<NamedType>();
				for (TypeMirror typeArgument: ((DeclaredType)type).getTypeArguments()) {
					DtoTypeElement dtoType = tomBaseElementProvider.getDtoType(typeArgument);
					
					if (dtoType == null) {
						//DTO does not exists
						castRequired = false;
					}
					
					converterArguments.add(dtoType);
				}
				for (TypeMirror typeArgument: ((DeclaredType)type).getTypeArguments()) {
					converterArguments.add(tomBaseElementProvider.getDomainType(typeArgument));
				}
				if (castRequired) {
					pw.print("((", TypedClassBuilder.get(converterTypeElement, converterArguments.toArray(new NamedType[] {})), ")");
				}
			}
		}
		
		pw.print(methodName + "(");
		
		if (type.getKind().equals(TypeKind.DECLARED) && typeParametersSupport.hasTypeParameters(converterTypeElement)) {
			
			if (((DeclaredType)type).getTypeArguments().size() > 0) {
				int i = 0;
				for (TypeMirror typeArgumentMirror: ((DeclaredType)type).getTypeArguments()) {
					typeArgumentMirror.accept(new SimpleTypeVisitor6<Void, Integer>(){
						@Override
						public Void visitDeclared(DeclaredType t, Integer i) {
							printConverterParameter(t, i);
							return null;
						}
						
						@Override
						public Void visitWildcard(WildcardType t, Integer i) {
							if (t.getExtendsBound() != null) {
								printConverterParameter(t.getExtendsBound(), i);
							} else if (t.getSuperBound() != null) {
								printConverterParameter(t.getSuperBound(), i);
							}
							return null;							
						}
						
						private void printConverterParameter(TypeMirror type, int i) {

							if (i > 0) {
								pw.print(", ");
							}

							ConverterTypeElement converterTypeElement = tomBaseElementProvider.getConverter(type);
							
							if (converterTypeElement != null) {
								printConverterMethodName(converterTypeElement, type, tomBaseElementProvider, pw);
							} else {
								NamedType typeParameterType = nameTypesUtils.toType(type);
								pw.print("(", TypedClassBuilder.get(DtoConverter.class, typeParameterType, typeParameterType), ")null");
							}
						}
					}, i);
					i++;
				}
			}
		}
		
		if (castRequired) {
			pw.print(")");
		}
		pw.print(")");
	}
}