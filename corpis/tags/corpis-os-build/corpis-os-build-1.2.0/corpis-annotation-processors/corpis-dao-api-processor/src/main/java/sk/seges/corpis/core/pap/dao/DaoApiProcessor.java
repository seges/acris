package sk.seges.corpis.core.pap.dao;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.core.pap.dao.configurer.DaoApiProcessorConfigurer;
import sk.seges.corpis.core.pap.dao.model.DaoApiType;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DaoApiProcessor extends MutableAnnotationProcessor {

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new DaoApiProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new DaoApiType(context.getMutableType(), processingEnv)
		};
	}
	
	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		DataAccessObject daoAnnotation = context.getTypeElement().getAnnotation(DataAccessObject.class);
		
		if (daoAnnotation == null) {
			return false;
		}
		
		return daoAnnotation.provider().equals(Provider.INTERFACE);
	}
	
	@Override
	protected void processElement(ProcessorContext context) {}
}