package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.pap.model.model.api.GeneratedClass;
import sk.seges.sesam.pap.model.model.api.dto.DtoTypeVariable;

class DtoVariable extends TomBaseVariable implements GeneratedClass, DtoTypeVariable {

	private final MutableTypeMirror dtoType;
	private final boolean generated;

	DtoVariable(TypeVariable dtoTypeVariable, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.dtoType = processingEnv.getTypeUtils().toMutableType(dtoTypeVariable);
		this.generated = false;
	}

	DtoVariable(WildcardType dtoWildcardType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.dtoType = processingEnv.getTypeUtils().toMutableType(dtoWildcardType);
		this.generated = false;
	}

	DtoVariable(MutableTypeVariable dtoTypeVariable, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.dtoType = dtoTypeVariable;
		this.generated = false;
	}

	DtoVariable(MutableWildcardType dtoWildcardVariable, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.dtoType = dtoWildcardVariable;
		this.generated = false;
	}

	@Override
	public ConfigurationTypeElement getConfiguration() {
		return null;
	}

	public ConverterTypeElement getConverter() {
		return null;
	}

	public DomainTypeElement getDomain() {
		if (dtoType != null) {
			return processingEnv.getTransferObjectUtils().getDomainType(dtoType);
		}

		return null;
	}

	@Override
	public boolean isGenerated() {
		return generated;
	}

	@Override
	protected MutableTypeVariable getDelegate() {
		return (MutableTypeVariable)processingEnv.getTypeUtils().toMutableType(dtoType);
	}
}
