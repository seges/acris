/**
 * 
 */
package sk.seges.pap;

import com.google.gwt.user.client.ui.Composite;

import sk.seges.acris.scaffold.model.view.compose.ViewComposer;
import sk.seges.acris.scaffold.mvp.CompositionView;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

/**
 * @author ladislav.gazo
 */
public class CompositionProcessor extends FluentProcessor {
	public CompositionProcessor() {
		reactsOn(ViewComposer.class);
		addImplementedInterface(CompositionView.class);
		setSuperClass(Composite.class);
	}
	
	@Override
	protected MutableDeclaredType getResultType(MutableDeclaredType inputType) {
		return inputType.addClassSufix("CompositionPanel");
	}
	
	@Override
	protected void processElement(ProcessorContext context) {
		
	}

}
