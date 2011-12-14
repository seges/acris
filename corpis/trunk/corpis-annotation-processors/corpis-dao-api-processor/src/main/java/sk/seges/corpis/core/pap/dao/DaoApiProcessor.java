package sk.seges.corpis.core.pap.dao;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.corpis.core.pap.dao.configurer.DaoApiProcessorConfigurer;
import sk.seges.corpis.core.pap.dao.model.DaoApiType;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DaoApiProcessor extends AbstractDaoApiProcessor {

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new DaoApiProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new DaoApiType(new DomainDataInterfaceType(context.getTypeElement(), processingEnv), processingEnv)
		};
	}
}