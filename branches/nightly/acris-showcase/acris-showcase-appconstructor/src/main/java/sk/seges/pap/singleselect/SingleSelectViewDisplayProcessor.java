/**
 * 
 */
package sk.seges.pap.singleselect;

import sk.seges.acris.scaffold.model.view.compose2.Singleselect;
import sk.seges.pap.type.DisplayType;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author ladislav.gazo
 */
public class SingleSelectViewDisplayProcessor extends FluentProcessor {

	public SingleSelectViewDisplayProcessor() {
		reactsOn(Singleselect.class);
		addImplementedInterface(IsWidget.class);
		setResultKind(MutableTypeKind.INTERFACE);
	}

	@Override
	protected MutableDeclaredType getResultType(MutableDeclaredType inputType) {
		return new DisplayType(inputType);
	}

	@Override
	protected void doProcessElement(ProcessorContext context) {
	}

}
