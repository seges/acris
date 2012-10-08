/**
 * 
 */
package sk.seges.pap.type;

import javax.lang.model.element.Element;

import sk.seges.acris.scaffold.model.view.compose.ViewComposer;
import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

/**
 * @author ladislav.gazo
 */
public class ViewComposerAnnotation extends
		SingleAnnotationAccessor<ViewComposer> {

	public ViewComposerAnnotation(Element element,
			MutableProcessingEnvironment processingEnv) {
		super(element, ViewComposer.class, processingEnv);
	}

}
