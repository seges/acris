package sk.seges.sesam.pap.model.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElementType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;

public class TransferObjectTypes {

	private final EnvironmentContext<TransferObjectProcessingEnvironment> envContext;
	
	public TransferObjectTypes(EnvironmentContext<TransferObjectProcessingEnvironment> envContext) {
		this.envContext = envContext;
	}

	public List<DtoType> convertToDtoTypes(List<? extends MutableTypeMirror> types) {
		List<DtoType> result = new ArrayList<DtoType>();
		for (MutableTypeMirror type: types) {
			result.add(getDomainType(type).getDto());
		}
		
		return result;
	}

	public List<DtoType> convertToDtoTypesFromElements(List<? extends MutableElementType> elements) {
		List<DtoType> result = new ArrayList<DtoType>();
		for (MutableElementType element: elements) {
			result.add(getDomainType(element.asType()).getDto());
		}
		
		return result;
	}

	public DomainType getDomainType(MutableTypeMirror type) {
		if (type instanceof DomainType) {
			return (DomainType)type;
		}
		
		switch (type.getKind()) {
		case ARRAY:
			return new DomainArray(getDomainType(type), envContext.getProcessingEnv());
		case ANNOTATION_TYPE:
		case CLASS:
		case ENUM:
		case INTERFACE:
		case PRIMITIVE:
		case VOID:
			return new DomainDeclared((MutableDeclaredType) type, envContext, null);
		case TYPEVAR:
			return new DomainVariable((MutableTypeVariable)type, envContext, null);
		case WILDCARD:
			return new DomainVariable((MutableWildcardType)type, envContext, null);
		}

		throw new RuntimeException("Unsupported domain type! Unable to represent " + type.getKind() + " as a domain.");
	}
	
	public DomainType getDomainType(TypeMirror type) {
		switch (type.getKind()) {
		case ARRAY:
			return new DomainArray(getDomainType(((ArrayType)type).getComponentType()), envContext.getProcessingEnv());
		case DECLARED:
			return new DomainDeclared((DeclaredType) type, envContext, null);
		case ERROR:
		case EXECUTABLE:
		case NONE:
		case NULL:
		case OTHER:
		case PACKAGE:
			throw new RuntimeException("Unsupported domain type! Unable to represent " + type.getKind() + " as a domain.");
		case TYPEVAR:
			return new DomainVariable((TypeVariable)type, envContext, null);
		case WILDCARD:
			return new DomainVariable((WildcardType)type, envContext, null);
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
			return new DomainDeclared((PrimitiveType) type, envContext, null);
		case VOID:
			return new DomainDeclared((MutableDeclaredType)envContext.getProcessingEnv().getTypeUtils().toMutableType(type), envContext, null);
		}
		
		throw new RuntimeException("Unsupported domain type! Unable to represent " + type.getKind() + " as a domain.");
	}

	
	public DtoType getDtoType(MutableTypeMirror type) {
		if (type instanceof DtoType) {
			return (DtoType)type;
		}
		
		switch (type.getKind()) {
		case ARRAY:
			MutableArrayType arrayType = (MutableArrayType)type;
			return new DtoArray(getDtoType(arrayType.getComponentType()), envContext.getProcessingEnv());
		case ANNOTATION_TYPE:
		case CLASS:
		case ENUM:
		case INTERFACE:
		case PRIMITIVE:
		case VOID:
			return new DtoDeclared((MutableDeclaredType) type, envContext, null);
		case TYPEVAR:
			return new DtoVariable((MutableTypeVariable)type, envContext, null);
		case WILDCARD:
			return new DtoVariable((MutableWildcardType)type, envContext, null);
		}

		throw new RuntimeException("Unsupported DTO type! Unable to represent " + type.getKind() + " as DTO.");
	}
	
	public DtoType getDtoType(TypeMirror type) {
		switch (type.getKind()) {
		case ARRAY:
			return new DtoArray(getDtoType(((ArrayType)type).getComponentType()), envContext.getProcessingEnv());
		case DECLARED:
			return new DtoDeclared((DeclaredType) type, envContext, null);
		case ERROR:
		case EXECUTABLE:
		case NONE:
		case NULL:
		case OTHER:
		case PACKAGE:
			throw new RuntimeException("Unsupported DTO type! Unable to represent " + type.getKind() + " as DTO.");
		case TYPEVAR:
			return new DtoVariable((TypeVariable)type, envContext, null);
		case WILDCARD:
			return new DtoVariable((WildcardType)type, envContext, null);
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
			return new DtoDeclared((PrimitiveType) type, envContext, null);
		case VOID:
			return new DtoDeclared((MutableDeclaredType) envContext.getProcessingEnv().getTypeUtils().toMutableType(type), 
					envContext, null);
		}
		
		throw new RuntimeException("Unsupported DTO type! Unable to represent " + type.getKind() + " as DTO.");
	}
	
	public boolean isSameType(DtoType dtoType1, DtoType dtoType2) {
		return envContext.getProcessingEnv().getTypeUtils().isSameType(dtoType1, dtoType2);
	}
}