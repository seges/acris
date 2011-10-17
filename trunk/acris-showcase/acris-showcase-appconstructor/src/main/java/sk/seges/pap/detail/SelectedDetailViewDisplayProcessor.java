/**
 * 
 */
package sk.seges.pap.detail;

import sk.seges.acris.scaffold.model.view.compose.SelectedDetail;
import sk.seges.pap.type.DisplayType;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author ladislav.gazo
 */
public class SelectedDetailViewDisplayProcessor extends FluentProcessor {
	public SelectedDetailViewDisplayProcessor() {
		reactsOn(SelectedDetail.class);
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
