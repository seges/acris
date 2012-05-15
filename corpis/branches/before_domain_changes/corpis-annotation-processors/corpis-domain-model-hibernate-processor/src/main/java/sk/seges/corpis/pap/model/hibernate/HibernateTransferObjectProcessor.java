package sk.seges.corpis.pap.model.hibernate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.pap.model.hibernate.resolver.HibernateEntityResolver;
import sk.seges.sesam.pap.model.TransferObjectProcessor;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateTransferObjectProcessor extends TransferObjectProcessor {

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
	}
	
	@Override
	protected EntityResolver getEntityResolver() {
		return new HibernateEntityResolver(processingEnv);
	}
}