package sk.seges.corpis.appscaffold.model.pap.accessor;

import javax.lang.model.element.Element;

import sk.seges.corpis.appscaffold.shared.annotation.PersistenceType;
import sk.seges.corpis.appscaffold.shared.annotation.PersistentObject;
import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class PersistentObjectAccessor extends SingleAnnotationAccessor<PersistentObject> {

	public PersistentObjectAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(element, PersistentObject.class, processingEnv);
	}

	public boolean isEntity() {
		return isValid();
	}
	
	public PersistenceType[] getPersistenceTypes() {
		return annotation.value();
	}
}