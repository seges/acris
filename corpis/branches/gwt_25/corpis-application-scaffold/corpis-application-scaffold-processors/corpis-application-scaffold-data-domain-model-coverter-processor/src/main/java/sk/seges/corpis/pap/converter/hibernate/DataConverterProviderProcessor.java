package sk.seges.corpis.pap.converter.hibernate;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.pap.converter.hibernate.provider.DataRoundEnvConfigurationProvider;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DataConverterProviderProcessor extends HibernateConverterProviderProcessor {

	@Override
	protected ConfigurationProvider[] getConfigurationProviders(MutableDeclaredType service, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		return new ConfigurationProvider[] {
				new DataRoundEnvConfigurationProvider(context)
		};
	}
}