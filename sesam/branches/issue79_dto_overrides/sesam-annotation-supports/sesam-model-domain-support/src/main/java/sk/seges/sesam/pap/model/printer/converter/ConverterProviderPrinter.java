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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeParameterElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.HasConverter;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.resolver.DefaultParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

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

	public void printConverterMethods(boolean supportExtends, int constructorIndex) {
		for (Entry<String, ConverterTypeElement> converterEntry: converterCache.entrySet()) {
			printConverterMethodProvider(converterEntry.getValue(), ConverterTargetType.DOMAIN, supportExtends, constructorIndex);
			printConverterMethodProvider(converterEntry.getValue(), ConverterTargetType.DTO, supportExtends, constructorIndex);
			printConverterMethod(converterEntry.getValue(), ConverterTargetType.DOMAIN, supportExtends, constructorIndex);
			printConverterMethod(converterEntry.getValue(), ConverterTargetType.DTO, supportExtends, constructorIndex);
		}
	}

	public List<ConverterParameter> getConverterParametersDefinition(ConverterTypeElement converterTypeElement, int constructorIndex) {
		return converterTypeElement.getConverterParameters(parametersResolver, constructorIndex);
	}
	
	protected void printConverterParametersDefinition(List<ConverterParameter> converterParameters, ConverterTypeElement converterTypeElement) {
		int i = 0;
		for (ConverterParameter converterParameter: converterParameters) {
			if (!converterParameter.isPropagated()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(converterParameter.getType(), " " + converterParameter.getName());
				i++;
			}
		}
	}

	protected MutableType[] getConverterParametersUsage(ConverterTypeElement converterTypeElement, ExecutableElement method) {
		return new MutableType[] {
		};
	}
	
	protected void printConverterParametersUsage(List<ConverterParameter> converterParameters) {
		int i = 0;
		for (ConverterParameter converterParameter: converterParameters) {
			if (!converterParameter.isPropagated()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(converterParameter.getName());
				i++;
			}
		}
	}

	private boolean isTyped(ConverterTypeElement converterTypeElement) {
		DomainType domain = converterTypeElement.getDomain();
		
		return (domain != null && domain.getKind().isDeclared() && ((DomainDeclaredType)domain).hasTypeParameters());
	}
	
	private MutableTypeVariable[] toTypeVariables(MutableDeclaredType domainType) {
		MutableTypeVariable[] typeVariables = new MutableTypeVariable[domainType.getTypeVariables().size() * 2];
		
		for (int i = 0; i < domainType.getTypeVariables().size(); i++) {
			typeVariables[i*2] = processingEnv.getTypeUtils().getTypeVariable(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX);
			typeVariables[i*2 + 1] = processingEnv.getTypeUtils().getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX);
		}

		return typeVariables;
	}
	
	private MutableDeclaredType getTypedConverter(ConverterTypeElement converterType, boolean typed) {
		if (typed && converterType.getConfiguration().getRawDomain().getKind().isDeclared()) {
			return processingEnv.getTypeUtils().getDeclaredType(converterType.clone(), 
					toTypeVariables((MutableDeclaredType) converterType.getConfiguration().getRawDomain()));
		}
		
		return converterType;
	}
	
	private void printConverterCast(ConverterTypeElement converterTypeElement) {
		pw.print("(", getTypedConverter(converterTypeElement, isTyped(converterTypeElement)), ")");
	}
	
	private void printGenericConverterDefinition(ConverterTypeElement converterTypeElement) {
		MutableTypes typeUtils = processingEnv.getTypeUtils();

		DomainType domain = converterTypeElement.getDomain();
		
		if (domain != null && domain.getKind().isDeclared() && ((DomainDeclaredType)domain).hasTypeParameters()) {
			pw.print("<");
			pw.print(typeUtils.getTypeVariable(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX, domain.getDto()));
			pw.print(", ", typeUtils.getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX, domain));
			pw.print(">");
			pw.print(" ", getTypedConverter(converterTypeElement, isTyped(converterTypeElement)));
		} else {
			pw.print(converterTypeElement);
		}
	}
		
	protected MutableDeclaredType printConverterMethodDefinition(List<ConverterParameter> converterParameters, ConverterTypeElement converterTypeElement, 
			String methodName, boolean supportExtends, int constructorIndex) {
		pw.print("protected ");

		MutableDeclaredType converterReplacedTypeParameters = converterTypeElement;
		
		printGenericConverterDefinition(converterTypeElement);

		pw.print(" " + methodName + "(");

		printConverterParametersDefinition(converterParameters, converterTypeElement);
		pw.print(")");
		return converterReplacedTypeParameters;
	}
	
	private static final String TARGET_PARAMETER_NAME = "obj";
	private static final String CONVERTER_LOCAL_FIELD_NAME = "converter";
	
	public enum ConverterTargetType {
		DTO {
			@Override
			public String getMethodPrefix() {
				return "Dto";
			}

			@Override
			public MutableTypeMirror getObject(ConverterTypeElement converterType, MutableProcessingEnvironment processingEnv) {
				DtoType dto = converterType.getConfiguration().getRawDto();
				if (dto != null && dto.getKind().isDeclared()) {
					MutableDeclaredType dtoDeclared = ((DtoDeclaredType)dto).clone();
					
					MutableTypeVariable[] typeVariables = new MutableTypeVariable[dtoDeclared.getTypeVariables().size()];
					
					for (int i = 0; i < typeVariables.length; i++) {
						typeVariables[i] = processingEnv.getTypeUtils().getWildcardType((MutableTypeMirror)null, null);
					}
					
					dtoDeclared.setTypeVariables(typeVariables);
					return dtoDeclared;
				}
				
				return dto;
			}

			@Override
			public String getConverterMethodName() {
				return "getConverterForDto";
			}
		}, DOMAIN {
			@Override
			public String getMethodPrefix() {
				return "Domain";
			}

			@Override
			public MutableTypeMirror getObject(ConverterTypeElement converterType, MutableProcessingEnvironment processingEnv) {
				DomainType domain = converterType.getConfiguration().getRawDomain();
				if (domain != null && domain.getKind().isDeclared()) {
					MutableDeclaredType domainDeclared = ((DomainDeclaredType)domain).clone();
					
					MutableTypeVariable[] typeVariables = new MutableTypeVariable[domainDeclared.getTypeVariables().size()];
					
					for (int i = 0; i < typeVariables.length; i++) {
						typeVariables[i] = processingEnv.getTypeUtils().getWildcardType((MutableTypeMirror)null, null);
					}
					
					domainDeclared.setTypeVariables(typeVariables);
					return domainDeclared;
				}
				
				return domain;
			}

			@Override
			public String getConverterMethodName() {
				return "getConverterForDomain";
			}
		};
		
		public abstract String getMethodPrefix();
		public abstract MutableTypeMirror getObject(ConverterTypeElement converterType, MutableProcessingEnvironment processingEnv);
		public abstract String getConverterMethodName();
	}
	
	protected void printConverterMethodProvider(ConverterTypeElement converterTypeElement, ConverterTargetType converterTargetType, 
			boolean supportExtends, int constructorIndex) {
		List<ConverterParameter> converterParameters = getConverterParametersDefinition(converterTypeElement, constructorIndex);

		String converterMethod = getEnsuredConverterMethodName(converterTypeElement, converterTargetType);

		converterParameters.add(new ConverterParameter(converterTargetType.getObject(converterTypeElement, processingEnv), TARGET_PARAMETER_NAME, false));

		printConverterMethodDefinition(converterParameters, converterTypeElement, converterMethod, supportExtends, constructorIndex);
		pw.println(" {");

		pw.print(replaceTypeParametersByWildcards(processingEnv.getTypeUtils().toMutableType(DtoConverter.class)), " converter = ");
		pw.print(DefaultParametersResolver.CONVERTER_PROVIDER_NAME);
		pw.println(".", converterTargetType.getConverterMethodName() + "(" + TARGET_PARAMETER_NAME + ");");
		pw.println("if (" + CONVERTER_LOCAL_FIELD_NAME + " != null) {");
		pw.print("return (");
		pw.print(getTypedConverter(converterTypeElement, isTyped(converterTypeElement)));
		pw.println(") " + CONVERTER_LOCAL_FIELD_NAME + ";");
		pw.println("}");
		pw.print("return " + getConverterMethodName(converterTypeElement, converterTargetType), "(");
		printConverterParametersUsage(converterParameters);
		pw.println(");");
		pw.println("}");
		pw.println();
	}
	
	private MutableDeclaredType replaceTypeParametersByWildcards(MutableDeclaredType mutableType) {
		int typeVariableCount = mutableType.getTypeVariables().size();
		
		MutableTypeVariable[] wildcards = new MutableTypeVariable[typeVariableCount];
		
		for (int i = 0; i < typeVariableCount; i++) {
			wildcards[i] = processingEnv.getTypeUtils().getWildcardType((MutableTypeMirror) null, null);
		}
		
		mutableType.setTypeVariables(wildcards);
		return mutableType;
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
	 * @param methodName
	 *            converter method name, e.g. getDomainObjectConverter
	 * @param supportExtends
	 *            determines whether generates wildcards or just type variables in the method definition that converts parametrized domain objects,
	 *            e.g. for List<TransferObjectClass> can be generated <DTO, Domain> or <? extends DTO, ? extends Domain>
	 * @param constructorIndex
	 *            determines which converter constructor should be used - constructors are ordered by number of parameters. -1 defines to use last
	 *            constructor and 0 defines to use the first one
	 */
	protected void printConverterMethod(ConverterTypeElement converterTypeElement, ConverterTargetType converterTargetType, boolean supportExtends, int constructorIndex) {

		List<ConverterParameter> converterParameters = getConverterParametersDefinition(converterTypeElement, constructorIndex);
		List<ConverterParameter> originalParameters = new ArrayList<ConverterParameter>();
		originalParameters.addAll(converterParameters);
		converterParameters.add(new ConverterParameter(converterTargetType.getObject(converterTypeElement, processingEnv), TARGET_PARAMETER_NAME, false));

		String converterMethod = getConverterMethodName(converterTypeElement, converterTargetType);

		MutableDeclaredType converterReplacedTypeParameters = printConverterMethodDefinition(converterParameters, converterTypeElement, converterMethod, supportExtends, constructorIndex);
		pw.println("{");
		
		pw.print("return ");

		boolean converterInstantiable = converterTypeElement.isConverterInstantiable();

		if (converterTypeElement.hasTypeParameters()) {
			converterReplacedTypeParameters = getTypedConverter(converterTypeElement, true);
		} else if (converterInstantiable) {
			printConverterCast(converterTypeElement);
		}
		
		pw.print("new ", converterReplacedTypeParameters);
		
		pw.print("(");

		int i = 0;
		for (ConverterParameter parameter : originalParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(parameter.getName());
			i++;
		}

		pw.println(");");
		pw.println("}");
		pw.println();
	}

	public static final String ENSURE_CONVERTER_METHOD_PREFIX = "ensure";
	public static final String GET_CONVERTER_METHOD_PREFIX = "get";
	
	protected String getEnsuredConverterMethodName(ConverterTypeElement converterTypeElement, ConverterTargetType targetType) {
		if (converterTypeElement == null) {
			return null;
		}

		String converterMethod = ENSURE_CONVERTER_METHOD_PREFIX + targetType.getMethodPrefix() + converterTypeElement.getSimpleName();

		if (converterCache.containsKey(converterTypeElement.getSimpleName())) {
			return converterMethod;
		}
		
		converterCache.put(converterTypeElement.getSimpleName(), converterTypeElement);
		
		return converterMethod;
	}
	
	protected String getConverterMethodName(ConverterTypeElement converterTypeElement, ConverterTargetType targetType) {
		return getConverterMethodName(converterTypeElement, targetType, GET_CONVERTER_METHOD_PREFIX);
	}
	
	private String getConverterMethodName(ConverterTypeElement converterTypeElement, ConverterTargetType targetType, String prefix) {
		if (converterTypeElement == null) {
			return null;
		}

		return prefix + targetType.getMethodPrefix() + converterTypeElement.getSimpleName();
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

	public void printDtoConverterMethodName(DtoType dtoType, String parameterName, ExecutableElement method, FormattedPrintWriter pw) {
		printConverterMethodName(ConverterTargetType.DTO, dtoType, parameterName, new DtoTypeElementProvider(), method, pw);
	}

	public void printDomainConverterMethodName(DomainType domainType, String parameterName, ExecutableElement method, FormattedPrintWriter pw) {
		printConverterMethodName(ConverterTargetType.DOMAIN, domainType, parameterName, new DomainTypeElementProvider(), method, pw);
	}

	private MutableDeclaredType getConvertedResult(ConverterTypeElement converterTypeElement, ConverterTargetType targetType, MutableTypeMirror type, TomBaseElementProvider tomBaseElementProvider) {
		
		String methodName = getConverterMethodName(converterTypeElement, targetType);
		
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
	
	public void printConverterParams(ExecutableElement method, FormattedPrintWriter pw) {
		MutableType[] converterParametersUsage = getConverterParametersUsage(null, method);

		Set<String> parameterNames = new HashSet<String>();
		
		for (MutableType parameterType: converterParametersUsage) {
			if (parameterType instanceof MutableReferenceType) {
				if (((MutableReferenceType)parameterType).getReference() != null) {
					String parameterName = ((MutableReferenceType)parameterType).toString();
					
					MutableTypeValue reference = ((MutableReferenceType)parameterType).getReference();
					if (reference instanceof MutableArrayTypeValue) {
						pw.print(((MutableArrayTypeValue) reference).asType());
					} else if (reference instanceof MutableDeclaredTypeValue) {
						pw.print(((MutableDeclaredTypeValue) reference).asType());
					} else if (reference instanceof MutableReferenceTypeValue) {
						pw.print(((MutableReferenceTypeValue) reference).asType());
					}
					
					parameterNames.add(parameterName);
					
					pw.print(" ", ((MutableReferenceType)parameterType).toString(), " = ");
					pw.println(((MutableReferenceType)parameterType).getReference(), ";");
				}
			}
		}
	}
	
	private <T extends MutableTypeMirror & HasConverter> void printConverterMethodName(ConverterTargetType targetType, T type, String parameterName, TomBaseElementProvider tomBaseElementProvider, ExecutableElement method, FormattedPrintWriter pw) {
		
		String methodName = getEnsuredConverterMethodName(type.getConverter(), targetType);
		
		if (methodName == null) {
			return;
		}

		MutableType[] converterParametersUsage = getConverterParametersUsage(type.getConverter(), method);

		MutableDeclaredType convertedResult = getConvertedResult(type.getConverter(), targetType, type, tomBaseElementProvider);
		
		if (convertedResult != null) {
			pw.print("((", convertedResult, ")");
		}
		
		pw.print(methodName + "(");
				
		if (pw.printAll(converterParametersUsage) > 0) {
			pw.print(", ");
        }

        pw.print(parameterName);
        
		if (convertedResult != null) {
			pw.print(")");
		}
		
		pw.print(")");
	}
}