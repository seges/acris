package sk.seges.corpis.appscaffold.model.pap.accessor;

import javax.lang.model.element.Element;

import sk.seges.corpis.appscaffold.shared.annotation.ReadOnly;
import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class ReadOnlyAccessor extends SingleAnnotationAccessor<ReadOnly> {

	public ReadOnlyAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(element, ReadOnly.class, processingEnv);
	}

	public boolean isReadonly() {
		return isValid();
	}
}