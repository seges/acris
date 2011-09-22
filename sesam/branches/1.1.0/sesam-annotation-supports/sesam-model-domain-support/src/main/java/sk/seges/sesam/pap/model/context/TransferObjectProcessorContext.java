package sk.seges.sesam.pap.model.context;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

public class TransferObjectProcessorContext implements ProcessorContext {
		
	protected final ConfigurationTypeElement configurationTypeElement;

	protected final Modifier modifier;
	protected final ExecutableElement method;
	protected final ExecutableElement domainMethod;

	protected TypeMirror domainMethodReturnType;

	protected NamedType fieldType;
	protected String fieldName;

	protected String domainFieldName;
	protected String domainFieldPath;

	protected ConverterTypeElement converterType;
	protected String localConverterName;

	public TransferObjectProcessorContext(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method) {
		this(configurationTypeElement, modifier, method, method);
	}

	public TransferObjectProcessorContext(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod) {
		
		this.configurationTypeElement = configurationTypeElement;
		this.modifier = modifier;
		this.method = method;
		this.domainMethod = domainMethod;
	}

	protected TypeMirror erasure(TransferObjectHelper toHelper, TypeElement typeElement, TypeMirror param) {

		if (typeElement == null) {
			return param;
		}
		
		if (!param.getKind().equals(TypeKind.TYPEVAR)) {
			return param;
		}
		
		TypeVariable typeVar = (TypeVariable)param;
		if (typeVar.getUpperBound() != null) {
			NamedType convertedTypeVar = toHelper.convertType(typeVar.getUpperBound());
			if (convertedTypeVar != null) {
				return typeVar.getUpperBound();
			}
		}

		if (typeVar.getLowerBound() != null) {
			NamedType convertedTypeVar = toHelper.convertType(typeVar.getLowerBound());
			if (convertedTypeVar != null) {
				return typeVar.getLowerBound();
			}
		}

		return ProcessorUtils.erasure(typeElement, typeVar);
	}

	private TypeParametersSupport typeParametersSupport;
	private ProcessingEnvironment processingEnv;
	private NameTypeUtils nameTypesUtils;
	private TransferObjectHelper toHelper;
	private RoundEnvironment roundEnv;
	
	public boolean initialize(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, EntityResolver entityResolver) {
		return initialize(processingEnv, roundEnv, entityResolver, null);
	}
	
	public boolean initialize(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, EntityResolver entityResolver, String path) {

		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
		this.toHelper = new TransferObjectHelper(nameTypesUtils, processingEnv, roundEnv);
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, nameTypesUtils);
		
		if (path == null) {
			this.domainFieldPath = toHelper.getFieldPath(getMethod());
			this.fieldName = MethodHelper.toField(getMethod());
		} else {
			this.domainFieldPath = path + "." + MethodHelper.toField(getMethod());
			this.fieldName = MethodHelper.toField(getDomainFieldPath());
		}

		this.domainFieldName = MethodHelper.toGetter(getDomainFieldPath());

		//context.setDomainTypeElement(toHelper.getDomainTypeElement(context.getConfigurationElement()));

		DomainTypeElement domainTypeElement = configurationTypeElement.getDomain();
		
		NamedType type = null;
			
		if (entityResolver.getTargetEntityType(getMethod()).getKind().equals(TypeKind.TYPEVAR)) {
			TypeMirror returnType = erasure(toHelper, domainTypeElement.asElement(), entityResolver.getTargetEntityType(getMethod()));
			if (returnType != null) {
				type = toHelper.convertType(returnType);
				if (type == null) {
					type = nameTypesUtils.toType(returnType);
				}
			} else {
				type = handleDomainTypeParameter(processingEnv, toHelper, entityResolver);
				
				if (type == null) {
					return false;
				}
			}
		} else {
			TypeMirror targetEntityType = entityResolver.getTargetEntityType(getMethod());
			
			type = toHelper.convertType(targetEntityType);
			if (type == null) {
				type = nameTypesUtils.toType(targetEntityType);
			}
		}

		TypeMirror domainReturnType = entityResolver.getTargetEntityType(getDomainMethod());
		
		if (entityResolver.getTargetEntityType(getDomainMethod()).getKind().equals(TypeKind.TYPEVAR)) {
			TypeMirror erasedType = erasure(toHelper, domainTypeElement.asElement(), domainReturnType);
			if (erasedType != null && !erasedType.toString().equals(Object.class.getCanonicalName())) {
				domainReturnType = erasedType;
			}/* else {
				TypeMirror targetEntityType = getTargetEntityType(context.getDomainMethod());
				if (targetEntityType != null && !context.getDomainMethod().getReturnType().equals(targetEntityType)) {
					domainReturnType = targetEntityType;
				}
			}*/
		}

		if (getMethod().getReturnType().getKind().equals(TypeKind.VOID)) {
			
			type = toHelper.convertType(domainReturnType);

			if (type == null) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find DTO alternative for " + entityResolver.getTargetEntityType(getDomainMethod()).toString() + ". Skipping getter " + 
						getMethod().getSimpleName().toString(),configurationTypeElement.asElement());
				return false;
			}
		}

		this.fieldType = type;

		setDomainMethodReturnType(domainReturnType);

		if (getMethod().getReturnType().equals(TypeKind.VOID) && domainReturnType.getKind().equals(TypeKind.DECLARED)) {
			
			NamedType domainReturnNamedType = nameTypesUtils.toType(domainReturnType);
			
			if (!type.getCanonicalName().equals(domainReturnNamedType.getCanonicalName())) {
				
				ImmutableType dtoType = null;
				
				if (domainReturnType.getKind().equals(TypeKind.DECLARED)) {
					dtoType = new DomainTypeElement(domainReturnType, processingEnv, roundEnv).getDtoTypeElement();
				}
	
				if (dtoType == null || !dtoType.getCanonicalName().equals(type.getCanonicalName())) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Return type from domain method " + domainReturnType.toString() + " " + domainTypeElement.getCanonicalName() + 
							"." + getDomainFieldName() + " is not compatible with specified return type in the DTO " + type.toString() + ". Please, check your configuration " + 
							configurationTypeElement.getCanonicalName(), configurationTypeElement.asElement());
					return false;
				}
			}
		} else if (getMethod().getReturnType().equals(TypeKind.VOID)) {
			if (!type.getCanonicalName().equals(domainReturnType.toString())) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Return type from domain method " + domainReturnType.toString() + " " + domainTypeElement.getCanonicalName() + 
						"." + getDomainFieldName() + " is not compatible with specified return type in the DTO " + type.toString() + ". Please, check your configuration " + 
						configurationTypeElement.getCanonicalName(), configurationTypeElement.asElement());
				return false;
			}
		}

		intializeConverter();
		
		return true;
	}
	
	protected void intializeConverter() {

		TypeMirror returnType = getDomainMethodReturnType();
		
		switch (returnType.getKind()) {
		case TYPEVAR:
			TypeMirror domainType = configurationTypeElement.getDomain().asType();
			
			if (domainType.getKind().equals(TypeKind.DECLARED)) {
				Integer parameterIndex = typeParametersSupport.getParameterIndexByName((DeclaredType)domainType, 
						((TypeVariable)returnType).asElement().getSimpleName().toString());
				if (parameterIndex != null) {
					this.localConverterName = LOCAL_CONVERTER_NAME + parameterIndex;
				}
			}
			break;
		case ARRAY:
			//TODO
			break;
		case DECLARED:
			Element returnElement = ((DeclaredType)returnType).asElement();
			switch (returnElement.getKind()) {
			case CLASS:
			case INTERFACE:

				//reads TransferObjectConfiguration annotation from method in the configuration
				ConverterTypeElement converterTypeElement = new ConfigurationTypeElement(getMethod(), processingEnv, roundEnv).getConverter();

				if (converterTypeElement != null) {
					this.converterType = converterTypeElement;
				} else {
					this.converterType = getConverterForDomainType(returnType);
				}
				
				break;
			}
			break;
		}
	}

	private ConverterTypeElement getConverterForDomainType(TypeMirror domainType) {
		
		ConfigurationTypeElement configurationElement = new DomainTypeElement(domainType, processingEnv, roundEnv).getConfiguration();

		if (configurationElement != null) {
			return configurationElement.getConverter();
		}
		
		return null;
	}
	
	protected NamedType handleDomainTypeParameter(ProcessingEnvironment processingEnv, TransferObjectHelper toHelper, EntityResolver entityResolver) {
		processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find erasure for the " + 
				entityResolver.getTargetEntityType(getDomainMethod()).toString() + " in the method: " + getFieldName(), 
				getConfigurationTypeElement().asElement());
		return null;
	}

	public void setDomainMethodReturnType(TypeMirror domainMethodReturnType) {
		this.domainMethodReturnType = domainMethodReturnType;
	}

	public TypeMirror getDomainMethodReturnType() {
		return domainMethodReturnType;
	}

	public String getDomainFieldPath() {
		return domainFieldPath;
	}

	public String getDomainFieldName() {
		return domainFieldName;
	}

	public NamedType getFieldType() {
		return fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public ConfigurationTypeElement getConfigurationTypeElement() {
		return configurationTypeElement;
	}

	public Modifier getModifier() {
		return modifier;
	}

	public ExecutableElement getMethod() {
		return method;
	}

	public ExecutableElement getDomainMethod() {
		return domainMethod;
	}
	
	@Override
	public ConverterTypeElement getConverter() {
		return converterType;
	}
	
	public String getLocalConverterName() {
		return localConverterName;
	}
}