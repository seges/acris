package sk.seges.sesam.pap.model.model;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.pap.model.model.api.GeneratedClass;
import sk.seges.sesam.pap.model.model.api.domain.DomainTypeVariable;
import sk.seges.sesam.pap.model.model.api.dto.DtoTypeVariable;

class DtoVariable extends TomBaseVariable implements GeneratedClass, DtoTypeVariable {

	private final MutableTypeVariable dtoType;
	private final boolean generated;

	DtoVariable(TypeVariable dtoTypeVariable, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.dtoType = (MutableTypeVariable)processingEnv.getTypeUtils().toMutableType(dtoTypeVariable);
		this.generated = false;
	}

	DtoVariable(WildcardType dtoWildcardType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.dtoType = (MutableTypeVariable)processingEnv.getTypeUtils().toMutableType(dtoWildcardType);
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

	public DomainTypeVariable getDomain() {

		if (getConfiguration() == null) {
			List<MutableTypeMirror> domainUpperBounds = new LinkedList<MutableTypeMirror>();
			for (MutableTypeMirror bound: getUpperBounds()) {
				domainUpperBounds.add(processingEnv.getTransferObjectUtils().getDtoType(bound).getDomain());
			}
			List<MutableTypeMirror> domainLowerBounds = new LinkedList<MutableTypeMirror>();
			for (MutableTypeMirror bound: getLowerBounds()) {
				domainLowerBounds.add(processingEnv.getTransferObjectUtils().getDtoType(bound).getDomain());
			}

			String variableName = getVariable();
			if (getVariable() != null && !getVariable().equals(MutableWildcardType.WILDCARD_NAME)) {
				variableName = ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + variableName;
			}

			return new DomainVariable(processingEnv.getTypeUtils().getTypeVariable(variableName, domainUpperBounds.toArray(new MutableTypeMirror[] {}), domainLowerBounds.toArray(new MutableTypeMirror[]{})), processingEnv, roundEnv);
		}
		
		return (DomainTypeVariable) getConfiguration().getDomain();
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
