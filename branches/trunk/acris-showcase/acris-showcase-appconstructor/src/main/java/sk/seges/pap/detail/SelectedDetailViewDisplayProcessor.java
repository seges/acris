/**
 * 
 */
package sk.seges.pap.detail;

import sk.seges.acris.scaffold.model.view.compose.SelectedDetail;
import sk.seges.pap.ScaffoldConstant;
import sk.seges.pap.ScaffoldNameUtil;
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
		ScaffoldNameUtil.prefixIfEnclosed(inputType);
		inputType.addPackageSufix("." + ScaffoldConstant.PRES_PKG);
		return inputType.addClassSufix(SelectedDetail.class.getSimpleName() + ScaffoldConstant.DISPLAY_SUFFIX);
	}

	@Override
	protected void processElement(ProcessorContext context) {
	}
}
