package sk.seges.sesam.pap.model.utils;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.NullCheck;
import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;

public class TransferObjectHelper {

	public static final String DTO_SUFFIX = "Dto";
	public static final String DEFAULT_SUFFIX = "Configuration";

	private NameTypes nameTypes;
	private ProcessingEnvironment processingEnv;
	private RoundEnvironment roundEnv;

	public TransferObjectHelper(NameTypes nameTypes, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		this.nameTypes = nameTypes;
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
	}

	NameTypes getNameTypes() {
		return nameTypes;
	}

	//TODO move to the processorUtils
	public boolean isUnboxedType(TypeMirror typeMirror) {
		try {
			return (processingEnv.getTypeUtils().unboxedType(typeMirror) != null);
		} catch (IllegalArgumentException e) {
		}

		return false;
	}

	//Move to DtoTypeElement
	@Deprecated
	public ImmutableType convertType(TypeMirror type, NameTypes nameTypes, ConfigurationTypeElement transferObjectConfiguration) {
		if (type == null || type.getKind().equals(TypeKind.NONE) || type.getKind().equals(TypeKind.NULL) || type.getKind().equals(TypeKind.ERROR)) {
			return null;
		}

		if (type.getKind().isPrimitive()) {
			return new InputClass(type, (String) null, type.toString().toLowerCase());
		}

		ImmutableType immutableType = nameTypes.toImmutableType(type);

		if (type.getKind().equals(TypeKind.DECLARED)) {

			DtoTypeElement dto = null;
			
			if (transferObjectConfiguration == null) {
				dto = new DomainTypeElement(type, processingEnv, roundEnv).getDtoTypeElement();
			}
			
			if (dto != null) {
				return dto;
			}

			DeclaredType declaredType = ((DeclaredType) type);

			if (declaredType.asElement().getKind().equals(ElementKind.ENUM)) {
				return immutableType;
			}

			if (isUnboxedType(declaredType)) {
				return immutableType;
			}

		} else if (type.getKind().equals(TypeKind.ARRAY)) {
			ArrayType arrayType = (ArrayType) type;
			NamedType componentType = convertType(arrayType.getComponentType());
			if (componentType != null) {
				return new ArrayNamedType(componentType);
			}
		}

		return immutableType;
	}

	//Move to DtoTypeElement
	@Deprecated
	public ImmutableType convertType(TypeMirror type) {
		return convertType(type, getNameTypes(), null);
	}

	//Move to DtoTypeElement
	@Deprecated
	public NamedType getDtoSuperclass(ConfigurationTypeElement configurationTypeElement) {

		DomainTypeElement superClassDomainType = configurationTypeElement.getDomain().getSuperClass();

		if (superClassDomainType != null) {
			return convertType(superClassDomainType.asType());
		}

		return null;
	}

	public static String getFieldPath(ExecutableElement method) {
		if (method == null) {
			return null;
		}

		Field fieldAnnotation = method.getAnnotation(Field.class);

		String fieldPath = MethodHelper.toField(method);

		if (fieldAnnotation != null && NullCheck.checkNull(fieldAnnotation.value()) != null) {
			fieldPath = fieldAnnotation.value();
		}

		return fieldPath;
	}

	//TODO move to the methodHelper
	public boolean isGetterMethod(ExecutableElement method) {
		return method.getSimpleName().toString().startsWith(MethodHelper.GETTER_PREFIX);
	}

	//TODO move to the methodHelper
	public boolean hasSetterMethod(TypeElement element, ExecutableElement method) {

		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		for (ExecutableElement elementMethod : methods) {

			if (elementMethod.getModifiers().contains(Modifier.PUBLIC)
					&& elementMethod.getSimpleName().toString().equals(MethodHelper.toSetter(method)) && elementMethod.getParameters().size() == 1
					&& processingEnv.getTypeUtils().isAssignable(elementMethod.getParameters().get(0).asType(), method.getReturnType())) {
				return true;
			}
		}

		if (element.getSuperclass() != null && element.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			return hasSetterMethod((TypeElement) ((DeclaredType) element.getSuperclass()).asElement(), method);
		}

		return false;
	}
}