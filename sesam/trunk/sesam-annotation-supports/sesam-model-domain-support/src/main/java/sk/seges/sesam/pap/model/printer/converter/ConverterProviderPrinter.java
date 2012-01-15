/**
   Copyright 2011 Seges s.r.o.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;
import sk.seges.sesam.shared.model.converter.api.InstantiableDtoConverter;

/**
 * @author Peter Simun (simun@seges.sk)
 */
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

	/**
	 * Prints type parameters
	 * 
	 * @author Peter Simun (simun@seges.sk)
	 *
	 */
	interface ParameterPrinter {
		
		/**
		 * Java API model version
		 */
		void print(TypeParameterElement parameter, FormattedPrintWriter pw);

		/**
		 * Mutable API version
		 */
		void print(MutableTypeVariable parameter, FormattedPrintWriter pw);
	}

	/**
	 * Converts type parameters into the mutable alternatives and prints them
	 * 
	 * @author Peter Simun (simun@seges.sk)
	 * 
	 */
	protected class ParameterTypesPrinter implements ParameterPrinter {

		@Override
		public void print(TypeParameterElement parameter, FormattedPrintWriter pw) {
			pw.print(processingEnv.getTypeUtils().toMutableType(parameter.asType()));
		}

		@Override
		public void print(MutableTypeVariable parameter, FormattedPrintWriter pw) {
			pw.print(parameter);
		}
	}

	/**
	 * Printing just simple name/variable of the type parameters
	 * 
	 * @author Peter Simun (simun@seges.sk)
	 *
	 */
	protected class ParameterNamesPrinter implements ParameterPrinter {

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

	/**
	 * Prints type variables of the parametrized converter including the brackets < and >
	 * Output should looks like
	 * <pre>
	 * <DTO, DOMAIN>
	 * </pre>
	 * or 
	 * <pre>
	 * <DTO1, DTO2, DOMAIN1, DOMAIN2>
	 * </pre>
	 * for more type variables
	 */
	protected void printConverterTypeParameters(ConverterTypeElement converterTypeElement, ParameterPrinter parameterPrinter) {
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

	/**
	 * Convert type variables from convert type into the mutable type variables without extends. Converting should be done in two ways:
	 * <ul>
	 * 	<li>extending mode - generating wildcard <? extends DTO></li>
	 * 	<li>single mode - generating just type variable <DTO>
	 * </ul>
	 * @param converterTypeElement converter type element
	 * @param addExtends whether generating wildcard types or type variables
	 */
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
	
	protected void printConverterParametersDefinition(List<ConverterParameter> converterParameters, ConverterTypeElement converterTypeElement) {
		int i = 0;
		for (ConverterParameter converterParameter: converterParameters) {
			if (converterParameter.isConverter()) {
				if (i > 0) {
					pw.print(", ");
				}
				
				MutableDeclaredType parameterReplacedTypeParameters = ((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(converterParameter.getType())).setTypeVariables(toTypeParameters(converterTypeElement, false));
				pw.print(parameterReplacedTypeParameters, " " + converterParameter.getName());
				
				i++;
			}
		}
	}

	private void printConverterCast(ConverterTypeElement converterTypeElement) {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		pw.print("(", typeUtils.getDeclaredType(typeUtils.toMutableType(InstantiableDtoConverter.class), 
				typeUtils.getTypeVariable(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX), 
				typeUtils.getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX)), ")");
	}
	
	private void printGenericConverterDefinition(ConverterTypeElement converterTypeElement) {
		MutableTypes typeUtils = processingEnv.getTypeUtils();

		DomainType domain = converterTypeElement.getDomain();
		pw.print("<");
		pw.print(typeUtils.getTypeVariable(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX, domain.getDto()));
		pw.print(", ", typeUtils.getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX, domain));
		pw.print(">");
		pw.print(" ", typeUtils.getDeclaredType(typeUtils.toMutableType(InstantiableDtoConverter.class), 
				typeUtils.getTypeVariable(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX), 
				typeUtils.getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX)));
	}
	
	/**
	 * Method prints getter for domain converter.<br/>
	 * The result should looks like (for not parametrized domain classes)
	 * <pre>
	 * private <DTO extends TransferObjectClass, DOMAIN extends DomainObjectClass> DtoConverter<DTO, DOMAIN> getDomainObjectConverter(TransactionPropagationModel[] arg1) {
	 * 	return new DomainObjectConverter(required_params, arg1);
	 * }
	 * </pre>
	 * 
	 * and for parametrized domain classes the result should be:
	 * 
	 * <pre>
	 * private<DTO, DOMAIN> CollectionConverter<? extends DTO, ? extends DOMAIN> getCollectionConverter(DtoConverter<DTO, DOMAIN> arg0) {
	 * 	return new CollectionConverter<DTO, DOMAIN>(arg0);
	 * }
	 * </pre>
	 * 
	 * @param converterTypeElement
	 *            Type representing converter element, e.g. DomainObjectConverter
	 * @param convertMethod
	 *            converter method name, e.g. getDomainObjectConverter
	 * @param supportExtends
	 *            determines whether generates wildcards or just type variables in the method definition that converts parametrized domain objects,
	 *            e.g. for List<TransferObjectClass> can be generated <DTO, Domain> or <? extends DTO, ? extends Domain>
	 * @param constructorIndex
	 *            determines which converter constructor should be used - constructors are ordered by number of parameters. -1 defines to use last
	 *            constructor and 0 defines to use the first one
	 */
	protected void printConverterMethod(ConverterTypeElement converterTypeElement, String convertMethod, boolean supportExtends, int constructorIndex) {

		List<ConverterParameter> converterParameters = getConverterParametersDefinition(converterTypeElement, constructorIndex);

		pw.print("private ");

		MutableDeclaredType converterReplacedTypeParameters = converterTypeElement;
		
		if (converterTypeElement.hasTypeParameters()) {
			//for parametrized domain class, like Collections<T> or PagedResult<T>
			printConverterTypeParameters(converterTypeElement, new ParameterTypesPrinter());
			converterReplacedTypeParameters = converterTypeElement.clone().setTypeVariables(toTypeParameters(converterTypeElement, supportExtends));
			pw.print(converterReplacedTypeParameters);
		} else {
			//instead of printing the concrete type, we should print the generic definition
			printGenericConverterDefinition(converterTypeElement);
		}
		
		pw.print(" " + convertMethod + "(");

		printConverterParametersDefinition(converterParameters, converterTypeElement);
		
		int i = 0;
		
		pw.println(") {");

		pw.print("return ");

		if (converterTypeElement.hasTypeParameters()) {
			converterReplacedTypeParameters = converterTypeElement.clone().setTypeVariables(toTypeParameters(converterTypeElement, false));
		} else {
			printConverterCast(converterTypeElement);
		}
		
		pw.print("new ", converterReplacedTypeParameters);
		
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
			return processingEnv.getTransferObjectUtils().getDomainType(type).getConverter();
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
			return processingEnv.getTransferObjectUtils().getDtoType(type).getConverter();
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
		List<MutableType> parameters = new ArrayList<MutableType>();
		
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
		
		return parameters.toArray(new MutableType[] {});
	}
	
	private MutableDeclaredType getConvertedResult(ConverterTypeElement converterTypeElement, MutableTypeMirror type, TomBaseElementProvider tomBaseElementProvider) {
		
		String methodName = getConverterMethodName(converterTypeElement);
		
		if (methodName == null) {
			return null;
		}

		if (type.getKind().isDeclared() && converterTypeElement.hasTypeParameters()) {
			return converterTypeElement.clone().setTypeVariables(new MutableTypeVariable[] {});
		}
		
		return null;
	}

	private MutableTypeMirror replaceByWildcard(MutableTypeMirror mutableTypeMirror, boolean clone) {
		if (mutableTypeMirror.getKind().isDeclared()) {
			return replaceByWildcard(clone ? ((MutableDeclaredType)mutableTypeMirror).clone() : ((MutableDeclaredType)mutableTypeMirror));
		}

		if (mutableTypeMirror.getKind().equals(MutableTypeKind.TYPEVAR)) {
			return replaceByWildcard(clone ? ((MutableTypeVariable)mutableTypeMirror).clone() : ((MutableTypeVariable)mutableTypeMirror));
		}
		
		return mutableTypeMirror;
	}
	
	private MutableDeclaredType replaceByWildcard(MutableDeclaredType mutableType) {
		for (MutableTypeMirror typeVariable: mutableType.getTypeVariables()) {
			replaceByWildcard(typeVariable, false);
		}
		return mutableType;
	}

	private MutableTypeVariable replaceByWildcard(MutableTypeVariable typeVariable) {
		if (typeVariable.getVariable() != null) {
			typeVariable.setVariable(MutableWildcardType.WILDCARD_NAME);
		}
		
		for (MutableTypeMirror variableUpperType: typeVariable.getUpperBounds()) {
			replaceByWildcard(variableUpperType, false);
		}

		for (MutableTypeMirror variableLowerType: typeVariable.getLowerBounds()) {
			replaceByWildcard(variableLowerType, false);
		}

		return typeVariable;
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