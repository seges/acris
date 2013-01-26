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

import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.ParameterElement.ParameterUsageContext;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.Field;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.HasConverter;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;
import sk.seges.sesam.shared.model.converter.api.InstantiableDtoConverter;

/**
 * @author Peter Simun (simun@seges.sk)
 */
public class ConverterProviderPrinter extends AbstractConverterPrinter {

	protected static final String TARGET_PARAMETER_NAME = "obj";

	protected final FormattedPrintWriter pw;
	protected UsageType usageType;
	
	private Map<String, ConverterTypeElement> converterCache = new HashMap<String, ConverterTypeElement>();
	
	public ConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv, 
			ConverterConstructorParametersResolverProvider parametersResolverProvider, UsageType usageType) {
		super(parametersResolverProvider, processingEnv);
		this.pw = pw;
		this.usageType = usageType;
	}

	/**
	 * Prints type parameters
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

	public UsageType changeUsage(UsageType usageType) {
		UsageType previousUsage = this.usageType;
		this.usageType = usageType;
		return previousUsage;
	}
	
	public void printConverterMethods(boolean supportExtends, ConverterInstancerType converterInstancerType) {
		for (Entry<String, ConverterTypeElement> converterEntry: converterCache.entrySet()) {
			printGetConverterMethod(converterEntry.getValue(), ConverterTargetType.DOMAIN, supportExtends, converterInstancerType);
			printGetConverterMethod(converterEntry.getValue(), ConverterTargetType.DTO, supportExtends, converterInstancerType);
		}
	}

	public List<ConverterConstructorParameter> getConverterParametersDefinition(ConverterTypeElement converterTypeElement, ConverterInstancerType converterInstancerType) {
		return converterTypeElement.getConverterParameters(parametersResolverProvider.getParameterResolver(usageType), converterInstancerType);
	}
	
	protected void printConverterParametersDefinition(List<ConverterConstructorParameter> converterParameters, ConverterTypeElement converterTypeElement) {
		int i = 0;
		for (ConverterConstructorParameter converterParameter: converterParameters) {
			if (!converterParameter.isPropagated()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(converterParameter.getType(), " " + converterParameter.getName());
				i++;
			}
		}
	}

	protected ParameterElement[] getConverterParameters(ConverterTypeElement converterTypeElement, ExecutableElement method) {
		return parametersResolverProvider.getParameterResolver(usageType).getConstructorAditionalParameters();
	}
	
	protected int printConverterParametersUsage(List<ConverterConstructorParameter> converterParameters) {
		int i = 0;
		for (ConverterConstructorParameter converterParameter: converterParameters) {
			if (!converterParameter.isPropagated()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(converterParameter.getName());
				i++;
			}
		}
		
		return i;
	}

	private boolean isTyped(ConverterTypeElement converterTypeElement) {
		//DomainType domain = converterTypeElement.getDomain();
//		return (domain != null && domain.getKind().isDeclared() && ((DomainDeclaredType)domain).hasTypeParameters());
		
		return (converterTypeElement != null && converterTypeElement.hasTypeParameters());
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
		
		MutableDeclaredType converterBase = converterType.getConverterBase();
		
		if (converterBase.hasTypeParameters()) {

			MutableDeclaredType result = processingEnv.getTypeUtils().toMutableType(InstantiableDtoConverter.class);
			MutableTypeVariable[] typeVariables = new MutableTypeVariable[2];
			typeVariables[0] = processingEnv.getTypeUtils().getTypeVariable(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX);
			typeVariables[1] = processingEnv.getTypeUtils().getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX);
			result.setTypeVariables(typeVariables);
			return result;
		}
		
		return converterType;
	}
	
	private void printConverterCast(ConverterTypeElement converterTypeElement) {
		pw.print("(", getTypedConverter(converterTypeElement, isTyped(converterTypeElement)), ")");
	}
	
	private void printGenericConverterDefinition(ConverterTypeElement converterTypeElement) {
		MutableTypes typeUtils = processingEnv.getTypeUtils();

		MutableDeclaredType converterBase = converterTypeElement.getConverterBase();
		
		//DomainType domain = converterTypeElement.getDomain();
		
		if (converterBase.hasTypeParameters()) {
			pw.print("<");
			pw.print(typeUtils.getTypeVariable(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX, converterBase.getTypeVariables().get(0)));
			pw.print(", ",typeUtils.getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX, converterBase.getTypeVariables().get(1)));
			pw.print(">");
			pw.print(" ", getTypedConverter(converterTypeElement, /*processingEnv.getTypeUtils().implementsType(converterTypeElement, 
					processingEnv.getTypeUtils().toMutableType(InstantiableDtoConverter.class)) */isTyped(converterTypeElement)));
		} else {
//			pw.print(DtoConverter.class, "<" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + ", " + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + ">");
//			pw.print(getTypedConverter(converterTypeElement, false));
			pw.print(converterTypeElement);
		}
	}
		
	protected MutableDeclaredType printConverterMethodDefinition(List<ConverterConstructorParameter> converterParameters, ConverterTypeElement converterTypeElement, 
			String methodName) {
		pw.print("protected ");

		MutableDeclaredType converterReplacedTypeParameters = converterTypeElement;
		
		printGenericConverterDefinition(converterTypeElement);

		pw.print(" " + methodName + "(");

		printConverterParametersDefinition(converterParameters, converterTypeElement);
		pw.print(")");
		return converterReplacedTypeParameters;
	}
		
	protected List<ConverterConstructorParameter> getConverterProviderMethodAdditionalParameters(ConverterTypeElement converterTypeElement, ConverterTargetType converterTargetType) {
		//MutableTypes typeUtils = processingEnv.getTypeUtils();
		//MutableDeclaredType objectClassType = typeUtils.toMutableType(Class.class).setTypeVariables(typeUtils.getWildcardType(converterTargetType.getObject(converterTypeElement, processingEnv), null));
		ConverterConstructorParameter converterParameter = new ConverterConstructorParameter(
				converterTargetType.getObject(converterTypeElement, processingEnv)
				/*objectClassType*/, TARGET_PARAMETER_NAME, null, false, processingEnv);
		ArrayList<ConverterConstructorParameter> params = new ArrayList<ConverterConstructorParameter>();
		params.add(converterParameter);
		return params;
	}

	protected void printConverterResultCast(ConverterTypeElement converterTypeElement) {
		if (converterTypeElement.getConverterBase().hasTypeParameters()) {
			pw.print(getTypedConverter(converterTypeElement, isTyped(converterTypeElement)));
		} else {
			pw.print(converterTypeElement);
		}
	}

	//TODO same as getConverterProviderMethodAdditionalParameters?
	protected ConverterConstructorParameter getAdditionalConverterParameter(ConverterTypeElement converterTypeElement, ConverterTargetType converterTargetType) {
		return new ConverterConstructorParameter(converterTargetType.getObject(converterTypeElement, processingEnv), TARGET_PARAMETER_NAME, 
				null, false, processingEnv);
	}
	
	protected void printGetConverterMethod(ConverterTypeElement converterTypeElement, ConverterTargetType converterTargetType, boolean supportExtends, ConverterInstancerType converterInstancerType) {

		List<ConverterConstructorParameter> converterParameters = getConverterParametersDefinition(converterTypeElement, converterInstancerType);
		List<ConverterConstructorParameter> originalParameters = new ArrayList<ConverterConstructorParameter>();
		originalParameters.addAll(converterParameters);
		
		converterParameters.addAll(getConverterProviderMethodAdditionalParameters(converterTypeElement, converterTargetType));

		String converterMethod = getConverterMethodName(converterTypeElement, converterTargetType);

		MutableDeclaredType converterReplacedTypeParameters = printConverterMethodDefinition(converterParameters, converterTypeElement, converterMethod);
		pw.println("{");

		//TODO print converter parameter usage definition - ala printConverterParams
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
		
		ParameterUsageContext context = new ParameterUsageContext() {
			
			@Override
			public ExecutableElement getMethod() {
				return null;
			}
		};
		
		for (ConverterConstructorParameter parameter : originalParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			
			pw.print(parameter.getUsage(context));
			i++;
		}

		pw.println(");");
		pw.println("}");
		pw.println();
	}

	public static final String GET_CONVERTER_METHOD_PREFIX = "get";
	
	protected String getGetConverterMethodName(ConverterTypeElement converterTypeElement, ConverterTargetType targetType) {
		if (converterTypeElement == null) {
			return null;
		}

		String converterMethod = GET_CONVERTER_METHOD_PREFIX + targetType.getMethodPrefix() + converterTypeElement.getSimpleName();

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

	public void printDtoGetConverterMethodName(DtoType dtoType, Field field, ExecutableElement method, FormattedPrintWriter pw, boolean inlineAware) {
		printGetConverterMethodName(ConverterTargetType.DTO, dtoType, field, new DtoTypeElementProvider(), method, pw, inlineAware);
	}

	public void printDomainGetConverterMethodName(DomainType domainType, Field field, ExecutableElement method, FormattedPrintWriter pw, boolean inlineAware) {
		printGetConverterMethodName(ConverterTargetType.DOMAIN, domainType, field, new DomainTypeElementProvider(), method, pw, inlineAware);
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

	public void printConverterParams(final ExecutableElement method, FormattedPrintWriter pw) {
		ParameterElement[] constructorAditionalParameters = getConverterParameters(null, method);
		
		ParameterUsagePrinter usagePrinter = new ParameterUsagePrinter(pw);
		ParameterUsageContext usageContext = new ParameterUsageContext() {
			
			@Override
			public ExecutableElement getMethod() {
				return method;
			}
		};
		
		for (ParameterElement parameterType: constructorAditionalParameters) {
			usagePrinter.printReferenceDeclaration(parameterType.getUsage(usageContext));
		}
	}
	
	private <T extends MutableTypeMirror & HasConverter> void printGetConverterMethodName(ConverterTargetType targetType, T type, Field field, TomBaseElementProvider tomBaseElementProvider, ExecutableElement method, FormattedPrintWriter pw, boolean inlineAware) {
		printConverterMethodName(targetType, type, null, field, tomBaseElementProvider, method, pw, getGetConverterMethodName(type.getConverter(), targetType), inlineAware);
	}

	private ParameterElement getParameterOfType(ParameterElement[] converterParameters, Class<?> clazz) {
		for (ParameterElement converterParameterUsage: converterParameters) {
			if (processingEnv.getTypeUtils().isAssignable(converterParameterUsage.getType(), processingEnv.getTypeUtils().toMutableType(clazz))) {
				return converterParameterUsage;
			}
		}
		
		return null;
	}

	public MutableDeclaredType getDtoConverterType(DomainType domainType, boolean usage) {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		MutableDeclaredType dtoConverter = typeUtils.toMutableType(DtoConverter.class);

		MutableTypeVariable dtoTypeVariable = usage ? 
				processingEnv.getTypeUtils().getTypeVariable(((MutableTypeVariable)domainType.getDto()).getVariable()) : (MutableTypeVariable)domainType.getDto();
		MutableTypeVariable domainTypeVariable = (MutableTypeVariable)domainType;
		
		if (usage) {
			domainTypeVariable = processingEnv.getTypeUtils().getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + domainTypeVariable.getVariable());
		} else {
			domainTypeVariable = domainTypeVariable.clone().setVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + domainTypeVariable.getVariable());
		}
		
		dtoConverter = dtoConverter.setTypeVariables(dtoTypeVariable, domainTypeVariable);
		
		return dtoConverter;
	}
	
	public void printObtainConverterFromCache(ConverterTargetType targetType, DomainType domainType, Field field, final ExecutableElement domainMethod,
			boolean castConverter) {

		MutableDeclaredType dtoConverter = null;

		if (domainType instanceof MutableTypeVariable) {
			dtoConverter = getDtoConverterType(domainType, true);
		} else {
			dtoConverter = domainType.getConverter();
		}

		if (castConverter) {
			pw.print("((", dtoConverter, ")(", processingEnv.getTypeUtils().toMutableType(DtoConverter.class).setTypeVariables(), ")");
		}
		
		ParameterElement[] converterParametersUsage = getConverterParameters(domainType.getConverter(), domainMethod);
		ParameterElement converterProviderParameter = getParameterOfType(converterParametersUsage, ConverterProviderContext.class);

		pw.print(converterProviderParameter.getName() + "." + targetType.getConverterMethodName() + "(");
		printField(field);

//		ParameterUsageContext usageContext = new ParameterUsageContext() {
//			
//			@Override
//			public ExecutableElement getMethod() {
//				return domainMethod;
//			}
//		};

//        printParameterElement(getParameterOfType(converterParametersUsage, ConvertedInstanceCache.class), 
//        		usageContext, true, 1, false);
        
		pw.print((castConverter ? ")" : "") + ")");
	}

	private void printField(Field field) {
		//Cast to the correct type
		if (field.getCastType() != null) {
			pw.print("(", field.getCastType(), ")");
		}

        pw.print(field.getName());
	}
	
	private int printParameterElement(ParameterElement parameter, ParameterUsageContext usageContext, boolean inlineAware, int i, boolean usageOnly) {
		if (!parameter.isPropagated()) {
			if (i > 0) {
				pw.print(", ");
			}
			MutableType parameterUsage = parameter.getUsage(usageContext);
			
			if (inlineAware && parameterUsage instanceof MutableReferenceType && ((MutableReferenceType)parameterUsage).isInline()) {
				pw.print(((MutableReferenceType)parameterUsage).getReference());
			} else {
				pw.print(parameterUsage);
			}
			i++;
		} else if (!usageOnly) {
			if (i > 0) {
				pw.print(", ");
			}
			
			pw.print(parameter.getName());
			
			i++;
		}
		
		return i;
	}
	
	private <T extends MutableTypeMirror & HasConverter> void printConverterMethodName(ConverterTargetType targetType, T type, T sourceType, Field field, TomBaseElementProvider tomBaseElementProvider, final ExecutableElement method, FormattedPrintWriter pw, String methodName, boolean inlineAware) {
		
		if (methodName == null) {
			return;
		}

		ParameterElement[] converterParametersUsage = getConverterParameters(type.getConverter(), method);

		MutableDeclaredType convertedResult = getConvertedResult(type.getConverter(), targetType, type, tomBaseElementProvider);
		
		if (convertedResult != null) {
			pw.print("((", convertedResult, ")");
		}
		
		pw.print(methodName + "(");

		ParameterUsageContext usageContext = new ParameterUsageContext() {
			
			@Override
			public ExecutableElement getMethod() {
				return method;
			}
		};
		
		int i = 0;
		for (ParameterElement parameter: converterParametersUsage) {
			i = printParameterElement(parameter, usageContext, inlineAware, i, true);
		}
		
		if (i > 0 && field != null) {
			pw.print(", ");
		}

		if (sourceType != null &&  processingEnv.getTypeUtils().isAssignable(type instanceof DelegateMutableType ? ((DelegateMutableType)type).ensureDelegateType() : type, 
				sourceType instanceof DelegateMutableType ? ((DelegateMutableType)type).ensureDelegateType() : sourceType)) {
			if (type.getKind().isDeclared() && ((MutableDeclaredType)type).getTypeVariables().size() > 0) {
				//TODO use cast utils!
			} else {
				if (field != null && field.getCastType() == null) {
					
					if (field.getType().toString(ClassSerializer.SIMPLE, false).equals(Class.class.getSimpleName())) {
						field.setCastType(processingEnv.getTypeUtils().getDeclaredType(processingEnv.getTypeUtils().toMutableType(Class.class), 
								new MutableDeclaredType[] { (MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(type) }));
					} else {
						field.setCastType(type);
					}
				}
			}
		} else {
			//TODO log error
		}

		if (field != null) {
			//Cast to the correct type
			printField(field);
		}

		if (convertedResult != null) {
			pw.print(")");
		}

		pw.print(")");
	}
}