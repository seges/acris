package sk.seges.corpis.core.pap.dao;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.core.pap.dao.configurer.HibernateDataDaoProcessorConfigurer;
import sk.seges.corpis.core.pap.dao.model.AbstractHibernateDaoType;
import sk.seges.corpis.core.pap.dao.model.HibernateDataDaoType;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateDataDaoProcessor extends AbstractHibernateDaoProcessor {

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new HibernateDataDaoProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new HibernateDataDaoType(context.getMutableType(), processingEnv)
		};
	}

	@Override
	protected AbstractHibernateDaoType getHibernateDaoType(MutableDeclaredType mutableType, MutableProcessingEnvironment processingEnv) {
		return new HibernateDataDaoType(mutableType, processingEnv);
	}
}
