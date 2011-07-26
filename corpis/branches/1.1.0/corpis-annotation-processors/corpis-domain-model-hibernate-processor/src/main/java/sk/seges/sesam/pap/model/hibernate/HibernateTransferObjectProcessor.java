package sk.seges.sesam.pap.model.hibernate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.pap.model.TransferObjectProcessor;
import sk.seges.sesam.pap.model.hibernate.util.HibernateHelper;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateTransferObjectProcessor extends TransferObjectProcessor {

	private HibernateHelper hibernateHelper;
	
	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		hibernateHelper = new HibernateHelper();
	}
	
	@Override
	protected TypeMirror getTargetEntityType(ExecutableElement method) {
		return hibernateHelper.getTargetEntityType(method);
	}

	@Override
	protected boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement) {
		return hibernateHelper.shouldHaveIdMethod(configurationElement, domainElement);
	}
}