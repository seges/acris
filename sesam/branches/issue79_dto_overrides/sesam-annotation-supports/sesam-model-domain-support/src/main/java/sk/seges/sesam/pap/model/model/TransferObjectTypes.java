package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
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

	public DomainType getDomainType(MutableTypeMirror type) {
		if (type instanceof DomainType) {
			return (DomainType)type;
		}
		
		switch (type.getKind()) {
		case ARRAY:
			return new DomainArray(getDomainType(type), processingEnv);
		case ANNOTATION_TYPE:
		case CLASS:
		case ENUM:
		case INTERFACE:
		case PRIMITIVE:
		case VOID:
			return new DomainDeclared((MutableDeclaredType) type, processingEnv, roundEnv, configurationProviders);
		case TYPEVAR:
			return new DomainVariable((MutableTypeVariable)type, processingEnv, roundEnv, configurationProviders);
		case WILDCARD:
			return new DomainVariable((MutableWildcardType)type, processingEnv, roundEnv, configurationProviders);
		}

		throw new RuntimeException("Unsupported domain type! Unable to represent " + type.getKind() + " as a domain.");
	}
	
	public DomainType getDomainType(TypeMirror type) {
		switch (type.getKind()) {
		case ARRAY:
			return new DomainArray(getDomainType(((ArrayType)type).getComponentType()), processingEnv);
		case DECLARED:
			return new DomainDeclared((DeclaredType) type, processingEnv, roundEnv, configurationProviders);
		case ERROR:
		case EXECUTABLE:
		case NONE:
		case NULL:
		case OTHER:
		case PACKAGE:
			throw new RuntimeException("Unsupported domain type! Unable to represent " + type.getKind() + " as a domain.");
		case TYPEVAR:
			return new DomainVariable((TypeVariable)type, processingEnv, roundEnv, configurationProviders);
		case WILDCARD:
			return new DomainVariable((WildcardType)type, processingEnv, roundEnv, configurationProviders);
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
			return new DomainDeclared((PrimitiveType) type, processingEnv, roundEnv, configurationProviders);
		case VOID:
			return new DomainDeclared((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(type), processingEnv, roundEnv, configurationProviders);
		}
		
		throw new RuntimeException("Unsupported domain type! Unable to represent " + type.getKind() + " as a domain.");
	}

	
	public DtoType getDtoType(MutableTypeMirror type) {
		if (type instanceof DtoType) {
			return (DtoType)type;
		}
		
		switch (type.getKind()) {
		case ARRAY:
			return new DtoArray(getDtoType(type), processingEnv);
		case ANNOTATION_TYPE:
		case CLASS:
		case ENUM:
		case INTERFACE:
		case PRIMITIVE:
		case VOID:
			return new DtoDeclared((MutableDeclaredType) type, processingEnv, roundEnv, configurationProviders);
		case TYPEVAR:
			return new DtoVariable((MutableTypeVariable)type, processingEnv, roundEnv);
		case WILDCARD:
			return new DtoVariable((MutableWildcardType)type, processingEnv, roundEnv);
		}

		throw new RuntimeException("Unsupported DTO type! Unable to represent " + type.getKind() + " as DTO.");
	}
	
	public DtoType getDtoType(TypeMirror type) {
		switch (type.getKind()) {
		case ARRAY:
			return new DtoArray(getDtoType(((ArrayType)type).getComponentType()), processingEnv);
		case DECLARED:
			return new DtoDeclared((DeclaredType) type, processingEnv, roundEnv, configurationProviders);
		case ERROR:
		case EXECUTABLE:
		case NONE:
		case NULL:
		case OTHER:
		case PACKAGE:
			throw new RuntimeException("Unsupported DTO type! Unable to represent " + type.getKind() + " as DTO.");
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
			return new DtoDeclared((PrimitiveType) type, processingEnv, roundEnv, configurationProviders);
		case VOID:
			return new DtoDeclared((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(type), processingEnv, roundEnv, configurationProviders);
		}
		
		throw new RuntimeException("Unsupported DTO type! Unable to represent " + type.getKind() + " as DTO.");
	}
	
	public boolean isSameType(DtoType dtoType1, DtoType dtoType2) {
		return processingEnv.getTypeUtils().isSameType(dtoType1, dtoType2);
	}
}