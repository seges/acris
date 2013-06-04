package sk.seges.corpis.appscaffold.model.pap.accessor;

import javax.lang.model.element.Element;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;


public class DomainInterfaceAccessor extends SingleAnnotationAccessor<DomainInterface> {

	public DomainInterfaceAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(element, DomainInterface.class, processingEnv);
	}
}