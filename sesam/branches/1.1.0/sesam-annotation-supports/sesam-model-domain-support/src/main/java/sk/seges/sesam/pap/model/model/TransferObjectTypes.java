package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class TransferObjectTypes {

	private final RoundEnvironment roundEnv;
	private final ConfigurationProvider[] configurationProviders;
	private final TransferObjectProcessingEnvironment processingEnv;
	
	public TransferObjectTypes(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider[] configurationProviders) {
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.configurationProviders = configurationProviders;
	}

	public DomainTypeElement getDomainType(TypeMirror type) {
		return new DomainTypeElement(type, processingEnv, roundEnv, configurationProviders);
	}

	public DomainTypeElement getDomainType(MutableTypeMirror type) {
		return new DomainTypeElement(type, processingEnv, roundEnv, configurationProviders);
	}

	public DtoType getDtoType(MutableTypeMirror type) {
		switch (type.getKind()) {
		case ARRAY:
			return new DtoArray(getDtoType(type), processingEnv);
		case ANNOTATION_TYPE:
		case CLASS:
		case ENUM:
		case INTERFACE:
		case PRIMITIVE:
		case VOID:
			return new DtoDeclared(type, processingEnv, roundEnv, configurationProviders);
		case TYPEVAR:
			return new DtoVariable((MutableTypeVariable)type, processingEnv, roundEnv);
		case WILDCARD:
			return new DtoVariable((MutableWildcardType)type, processingEnv, roundEnv);
		}

		throw new RuntimeException("Unsupported DTO type! Unable to representet " + type.getKind() + " as a DTO.");
	}
	
	public DtoType getDtoType(TypeMirror type) {
		switch (type.getKind()) {
		case ARRAY:
			return new DtoArray(getDtoType(type), processingEnv);
		case DECLARED:
			return new DtoDeclared(type, processingEnv, roundEnv, configurationProviders);
		case ERROR:
		case EXECUTABLE:
		case NONE:
		case NULL:
		case OTHER:
		case PACKAGE:
			throw new RuntimeException("Unsupported DTO type! Unable to representet " + type.getKind() + " as a DTO.");
		case TYPEVAR:
			return new DtoVariable((TypeVariable)type, processingEnv, roundEnv);
		case WILDCARD:
			return new DtoVariable((WildcardType)type, processingEnv, roundEnv);
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
		case VOID:
			return new DtoDeclared(type, processingEnv, roundEnv, configurationProviders);
		}
		
		throw new RuntimeException("Unsupported DTO type! Unable to representet " + type.getKind() + " as a DTO.");
	}
	
	public boolean isSameType(DtoType dtoType1, DtoType dtoType2) {
		return processingEnv.getTypeUtils().isSameType(dtoType1, dtoType2);
	}
}