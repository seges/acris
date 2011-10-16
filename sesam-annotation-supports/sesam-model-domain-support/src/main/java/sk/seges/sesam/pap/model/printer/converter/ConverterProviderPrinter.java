package sk.seges.sesam.pap.model.printer.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeParameterElement;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class ConverterProviderPrinter {

	protected final FormattedPrintWriter pw;
	
	protected final ParametersResolver parametersResolver;
	protected final TransferObjectProcessingEnvironment processingEnv;
	
	private Map<String, ConverterTypeElement> converterCache = new HashMap<String, ConverterTypeElement>();
	
	public ConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv, ParametersResolver parametersResolver) {
		this.pw = pw;
		this.processingEnv = processingEnv;
		this.parametersResolver = parametersResolver;
	}

	interface ParameterPrinter {
		
		void print(TypeParameterElement parameter, FormattedPrintWriter pw);

		void print(MutableTypeVariable parameter, FormattedPrintWriter pw);
	}

	class ParameterTypesPrinter implements ParameterPrinter {

		@Override
		public void print(TypeParameterElement parameter, FormattedPrintWriter pw) {
			pw.print(processingEnv.getTypeUtils().toMutableType(parameter.asType()));
		}

		@Override
		public void print(MutableTypeVariable parameter, FormattedPrintWriter pw) {
			pw.print(parameter);
		}
	}
	
	class ParameterNamesPrinter implements ParameterPrinter {

		@Override
		public void print(TypeParameterElement parameter, FormattedPrintWriter pw) {
			pw.print(parameter.getSimpleName().toString());
		}

		@Override
		public void print(MutableTypeVariable parameter, FormattedPrintWriter pw) {
			pw.print(parameter.getVariable());
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
			for (MutableTypeVariable converterTypeParameter: converterTypeElement.getTypeVariables()) {
				if (i > 0) {
					pw.print(", ");
				}
				parameterPrinter.print(converterTypeParameter, pw);
				i++;
			}
		}
		pw.print(">");
	}

	protected MutableTypeVariable[] toTypeParameters(ConverterTypeElement converterTypeElement, boolean addExtends) {
		
		List<MutableTypeVariable> result = new ArrayList<MutableTypeVariable>();

		if (converterTypeElement.asElement() != null) {
			for (TypeParameterElement converterTypeParameter: converterTypeElement.asElement().getTypeParameters()) {
				if (addExtends) {
					result.add(processingEnv.getTypeUtils().getWildcardType(processingEnv.getTypeUtils().getTypeVariable(converterTypeParameter.getSimpleName().toString()), null));
				} else {
					result.add(processingEnv.getTypeUtils().getTypeVariable(converterTypeParameter.getSimpleName().toString()));
				}
			}
		} else {
			for (MutableTypeVariable converterTypeParameter: converterTypeElement.getTypeVariables()) {
				if (addExtends) {
					result.add(processingEnv.getTypeUtils().getWildcardType(processingEnv.getTypeUtils().getTypeVariable(converterTypeParameter.getVariable()), null));
				} else {
					result.add(processingEnv.getTypeUtils().getTypeVariable(converterTypeParameter.getVariable()));
				}
			}
		}
		return result.toArray(new MutableTypeVariable[] {});
	}

	public void printConverterMethods(boolean supportExtends, int constructorIndex) {
		for (Entry<String, ConverterTypeElement> converterEntry: converterCache.entrySet()) {
			printConverterMethod(converterEntry.getValue(), converterEntry.getKey(), supportExtends, constructorIndex);
		}
	}
	
	public List<ConverterParameter> getConverterParametersDefinition(ConverterTypeElement converterTypeElement, int constructorIndex) {
		return converterTypeElement.getConverterParameters(parametersResolver, constructorIndex);
	}
	
	private void printConverterMethod(ConverterTypeElement converterTypeElement, String convertMethod, boolean supportExtends, int constructorIndex) {

		List<ConverterParameter> converterParameters = getConverterParametersDefinition(converterTypeElement, constructorIndex);

		pw.print("private");
		
		MutableDeclaredType converterReplacedTypeParameters = converterTypeElement;

		if (converterTypeElement.hasTypeParameters()) {
			printTypeParameters(converterTypeElement, new ParameterTypesPrinter());
			converterReplacedTypeParameters = converterTypeElement.clone().setTypeVariables(toTypeParameters(converterTypeElement, supportExtends));
		}
		
		pw.print(" ", converterReplacedTypeParameters);
		pw.print(" " + convertMethod + "(");

		int i = 0;
		for (ConverterParameter converterParameter: converterParameters) {
			if (converterParameter.isConverter() || !converterParameter.isPropagated()) {
				if (i > 0) {
					pw.print(", ");
				}
				
				if (converterParameter.isConverter()) {
					MutableDeclaredType parameterReplacedTypeParameters = ((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(converterParameter.getType())).setTypeVariables(toTypeParameters(converterTypeElement, false));
					pw.print(parameterReplacedTypeParameters, " " + converterParameter.getName());
				} else {
					pw.print(converterParameter.getType(), " " + converterParameter.getName());
				}
				i++;
			}
		}
		
		pw.println(") {");

		if (converterTypeElement.hasTypeParameters()) {
			converterReplacedTypeParameters = converterTypeElement.clone().setTypeVariables(toTypeParameters(converterTypeElement, false));
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
		ConverterTypeElement getConverter(MutableTypeMirror type);
		DomainType getDomainType(MutableTypeMirror type);
		DtoType getDtoType(MutableTypeMirror type);
	}
	
	class DomainTypeElementProvider implements TomBaseElementProvider {

		@Override
		public ConverterTypeElement getConverter(MutableTypeMirror type) {
			DomainType domainTypeElement = processingEnv.getTransferObjectUtils().getDomainType(type);
			if (domainTypeElement.getConfiguration() != null) {
				return domainTypeElement.getConfiguration().getConverter();
			}
			return null;
		}

		@Override
		public DomainType getDomainType(MutableTypeMirror type) {
			return processingEnv.getTransferObjectUtils().getDomainType(type);
		}

		@Override
		public DtoType getDtoType(MutableTypeMirror type) {
			return processingEnv.getTransferObjectUtils().getDomainType(type).getDto();
		}		
	}
	
	class DtoTypeElementProvider implements TomBaseElementProvider {

		@Override
		public ConverterTypeElement getConverter(MutableTypeMirror type) {
			DtoType dtoType = processingEnv.getTransferObjectUtils().getDtoType(type);
			if (dtoType.getConfiguration() != null) {
				return dtoType.getConverter();
			}
			
			return null;
		}

		@Override
		public DomainType getDomainType(MutableTypeMirror type) {
			return processingEnv.getTransferObjectUtils().getDtoType(type).getDomain();
		}

		@Override
		public DtoType getDtoType(MutableTypeMirror type) {
			return processingEnv.getTransferObjectUtils().getDtoType(type);
		}
	}

	public void printDtoConverterMethodName(ConverterTypeElement converterTypeElement, MutableTypeMirror type, ExecutableElement method, FormattedPrintWriter pw) {
		printConverterMethodName(converterTypeElement, type, new DtoTypeElementProvider(), method, pw);
	}

	public void printDomainConverterMethodName(ConverterTypeElement converterTypeElement, MutableTypeMirror type, ExecutableElement method, FormattedPrintWriter pw) {
		printConverterMethodName(converterTypeElement, type, new DomainTypeElementProvider(), method, pw);
	}

	protected MutableType[] getConverterParametersUsage(ConverterTypeElement converterTypeElement, MutableTypeMirror type, ExecutableElement method) {
		List<MutableTypeMirror> parameters = new ArrayList<MutableTypeMirror>();
		
		if (type.getKind().isDeclared() && converterTypeElement.hasTypeParameters()) {
			
			if (((MutableDeclaredType)type).getTypeVariables().size() > 0) {
				for (MutableTypeVariable typeArgumentMirror: ((MutableDeclaredType)type).getTypeVariables()) {
					
					for (MutableTypeMirror upperBound: typeArgumentMirror.getUpperBounds()) {
						parameters.add(upperBound);
					}

					for (MutableTypeMirror lowerBound: typeArgumentMirror.getLowerBounds()) {
						parameters.add(lowerBound);
					}
				}
			}
		}
		
		return parameters.toArray(new MutableTypeMirror[] {});
	}
	
	private MutableDeclaredType getConvertedResult(ConverterTypeElement converterTypeElement, MutableTypeMirror type, TomBaseElementProvider tomBaseElementProvider) {
		
		String methodName = getConverterMethodName(converterTypeElement);
		
		if (methodName == null) {
			return null;
		}

		if (type.getKind().isDeclared() && converterTypeElement.hasTypeParameters()) {
			
			if (((MutableDeclaredType)type).getTypeVariables().size() > 0) {
				List<MutableTypeVariable> converterArguments = new ArrayList<MutableTypeVariable>();
				for (MutableTypeMirror typeArgument: ((MutableDeclaredType)type).getTypeVariables()) {
					DtoType dtoType = tomBaseElementProvider.getDtoType(typeArgument);
					
					if (dtoType == null) {
						return null;
					}
					
					converterArguments.add(processingEnv.getTypeUtils().getTypeVariable(null, dtoType));
				}
				for (MutableTypeVariable typeArgument: ((MutableDeclaredType)type).getTypeVariables()) {
					converterArguments.add(processingEnv.getTypeUtils().getTypeVariable(null, tomBaseElementProvider.getDomainType(typeArgument)));
				}

				return converterTypeElement.clone().setTypeVariables(converterArguments.toArray(new MutableTypeVariable[] {}));
			}
		}
		
		return null;
	}
	
	private void printConverterMethodName(ConverterTypeElement converterTypeElement, MutableTypeMirror type, TomBaseElementProvider tomBaseElementProvider, ExecutableElement method, FormattedPrintWriter pw) {
		
		String methodName = getConverterMethodName(converterTypeElement);
		
		if (methodName == null) {
			return;
		}
		
		MutableDeclaredType convertedResult = getConvertedResult(converterTypeElement, type, tomBaseElementProvider);

		if (convertedResult != null) {
			pw.print("((", convertedResult, ")");
		}
		
		pw.print(methodName + "(");
		
		int i = 0;
		for (MutableType parameter: getConverterParametersUsage(converterTypeElement, type, method)) {
			printConverterParameter(tomBaseElementProvider, method, parameter, i);
			i++;
		}
		
		if (convertedResult != null) {
			pw.print(")");
		}
		pw.print(")");
	}
	
	private void printConverterParameter(TomBaseElementProvider tomBaseElementProvider, ExecutableElement method, MutableType type, int index) {
	
		if (index > 0) {
			pw.print(", ");
		}
	
		if (type instanceof MutableTypeMirror) {
			ConverterTypeElement converterTypeElement = tomBaseElementProvider.getConverter((MutableTypeMirror)type);
			
			if (converterTypeElement != null) {
				printConverterMethodName(converterTypeElement, (MutableTypeMirror)type, tomBaseElementProvider, method, pw);
			} else {
				if (type instanceof MutableTypeVariable) {
					pw.print("(", processingEnv.getTypeUtils().getDeclaredType(processingEnv.getTypeUtils().toMutableType(DtoConverter.class), (MutableTypeVariable)type, (MutableTypeVariable)type), ")null");
				} else if (type instanceof MutableDeclaredType) {
					pw.print("(", processingEnv.getTypeUtils().getDeclaredType(processingEnv.getTypeUtils().toMutableType(DtoConverter.class), (MutableDeclaredType)type, (MutableDeclaredType)type), ")null");
				} else {
					processingEnv.getMessager().printMessage(Kind.ERROR, "Unsupported type: " + type.toString() + " used in the converter " + converterTypeElement);
				}
			}
		} else {
			pw.print(type);
		}
	}

}