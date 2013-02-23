package sk.seges.corpis.core.pap.dao;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.core.pap.dao.configurer.DataDaoApiProcessorConfigurer;
import sk.seges.corpis.core.pap.dao.model.DataDaoApiType;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DataDaoApiProcessor extends AbstractDaoApiProcessor {

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new DataDaoApiProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new DataDaoApiType(context.getMutableType(), processingEnv)
		};
	}
}
