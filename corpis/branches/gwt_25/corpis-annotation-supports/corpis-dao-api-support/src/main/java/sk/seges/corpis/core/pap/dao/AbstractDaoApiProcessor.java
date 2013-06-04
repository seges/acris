package sk.seges.corpis.core.pap.dao;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;

public abstract class AbstractDaoApiProcessor extends MutableAnnotationProcessor {

	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		
		if (alreadyExists) {
			return false;
		}
		
		DataAccessObject daoAnnotation = context.getMutableType().getAnnotation(DataAccessObject.class);
		
		if (daoAnnotation == null) {
			return false;
		}
		
		return daoAnnotation.provider().equals(Provider.INTERFACE);
	}
	
	@Override
	protected void processElement(ProcessorContext context) {}

}
