package sk.seges.sesam.pap.model.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

	DtoVariable(TypeVariable dtoTypeVariable, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(envContext, configurationContext);
		
		this.dtoType = (MutableTypeVariable) getTypeUtils().toMutableType(dtoTypeVariable);
		this.generated = false;
	}

	DtoVariable(WildcardType dtoWildcardType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(envContext, configurationContext);
		
		this.dtoType = (MutableTypeVariable) getTypeUtils().toMutableType(dtoWildcardType);
		this.generated = false;
	}

	DtoVariable(MutableTypeVariable dtoTypeVariable, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(envContext, configurationContext);
		
		this.dtoType = dtoTypeVariable;
		this.generated = false;
	}

	DtoVariable(MutableWildcardType dtoWildcardVariable, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(envContext, configurationContext);
		
		this.dtoType = dtoWildcardVariable;
		this.generated = false;
	}

	@Override
	public List<ConfigurationTypeElement> getConfigurations() {
		return new ArrayList<ConfigurationTypeElement>();
	}

	public ConverterTypeElement getConverter() {
		return null;
	}

	public DomainTypeVariable getDomain() {

		ConfigurationTypeElement domainDefinitionConfiguration = getDomainDefinitionConfiguration();
		
		if (domainDefinitionConfiguration == null) {
			List<MutableTypeMirror> domainUpperBounds = new LinkedList<MutableTypeMirror>();
			for (MutableTypeMirror bound: getUpperBounds()) {
				domainUpperBounds.add(getTransferObjectUtils().getDtoType(bound).getDomain());
			}
			List<MutableTypeMirror> domainLowerBounds = new LinkedList<MutableTypeMirror>();
			for (MutableTypeMirror bound: getLowerBounds()) {
				domainLowerBounds.add(getTransferObjectUtils().getDtoType(bound).getDomain());
			}

			String variableName = getVariable();
//			if (getVariable() != null && !getVariable().equals(MutableWildcardType.WILDCARD_NAME)) {
//				variableName = ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + variableName;
//			}

			return new DomainVariable(getTypeUtils().getTypeVariable(variableName, domainUpperBounds.toArray(new MutableTypeMirror[] {}), domainLowerBounds.toArray(new MutableTypeMirror[]{})), envContext, configurationContext);
		}
		
		return (DomainTypeVariable) domainDefinitionConfiguration.getDomain();
	}

	@Override
	public boolean isGenerated() {
		return generated;
	}

	@Override
	protected MutableTypeVariable getDelegate() {
		return (MutableTypeVariable) getTypeUtils().toMutableType(dtoType);
	}

	@Override
	protected List<ConfigurationTypeElement> getConfigurationsForType() {
		return new ArrayList<ConfigurationTypeElement>();
	}
}
