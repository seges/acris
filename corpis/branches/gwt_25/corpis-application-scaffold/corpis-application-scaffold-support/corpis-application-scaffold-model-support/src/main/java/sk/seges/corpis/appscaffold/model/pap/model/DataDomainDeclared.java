package sk.seges.corpis.appscaffold.model.pap.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.DomainDeclared;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class DataDomainDeclared extends DomainDeclared {

	private DataTypeResolver dataTypeResolver;

	public DataDomainDeclared(MutableDeclaredType domainType, MutableDeclaredType dtoType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(domainType, dtoType, envContext, configurationContext);
		dataTypeResolver = new DataTypeResolver(envContext);
	}

	public DataDomainDeclared(DeclaredType domainType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(domainType, envContext, configurationContext);
		dataTypeResolver = new DataTypeResolver(envContext);
	}

	public DataDomainDeclared(MutableDeclaredType domainType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(domainType, envContext, configurationContext);
		dataTypeResolver = new DataTypeResolver(envContext);
	}

	public DataDomainDeclared(PrimitiveType domainType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(domainType, envContext, configurationContext);
		dataTypeResolver = new DataTypeResolver(envContext);
	}

	protected DomainType getDomainForType(TypeMirror type) {
		if (type.getKind().equals(TypeKind.DECLARED)) {
			List<MutableDeclaredType> domainDataTypes = new DataTypeResolver(environmentContext).getDomainData((MutableDeclaredType) environmentContext.getProcessingEnv().getTypeUtils().toMutableType(type));
			if (domainDataTypes.size() > 0) {
				return super.getDomainForType(environmentContext.getProcessingEnv().getElementUtils().getTypeElement(domainDataTypes.get(0).getCanonicalName()).asType());
			}
		}

		return super.getDomainForType(type);
	}

	protected List<ConfigurationTypeElement> getConfigurations(MutableTypeMirror domainType) {
		List<ConfigurationTypeElement> domainConfigurations = super.getConfigurations(domainType);
		
		if (domainType.getKind().equals(MutableTypeKind.CLASS) || domainType.getKind().equals(MutableTypeKind.INTERFACE)) {
			List<MutableDeclaredType> domainData = dataTypeResolver.getDomainData((MutableDeclaredType)domainType);

			if (domainData.size() > 0) {
				List<ConfigurationTypeElement> dataConfigurations = super.getConfigurations(domainData.get(0));
				if (domainConfigurations != null && domainConfigurations.size() > 0) {
					List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();
					result.addAll(domainConfigurations);
					result.addAll(dataConfigurations);
					return result;
				}
				return dataConfigurations;
			}
		}
		
		return domainConfigurations;
	}

}