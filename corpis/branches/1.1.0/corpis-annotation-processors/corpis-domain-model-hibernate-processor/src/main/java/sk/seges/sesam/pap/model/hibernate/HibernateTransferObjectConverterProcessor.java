package sk.seges.sesam.pap.model.hibernate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.pap.model.TransferObjectConvertorProcessor;
import sk.seges.sesam.pap.model.hibernate.util.HibernateHelper;

public class HibernateTransferObjectConverterProcessor extends TransferObjectConvertorProcessor {

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
