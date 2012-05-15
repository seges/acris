package sk.seges.corpis.appscaffold.model.pap.accessor;

import javax.lang.model.element.Element;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;


public class BaseObjectAccessor extends SingleAnnotationAccessor<BaseObject> {

	public BaseObjectAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(element, BaseObject.class, processingEnv);
	}
}