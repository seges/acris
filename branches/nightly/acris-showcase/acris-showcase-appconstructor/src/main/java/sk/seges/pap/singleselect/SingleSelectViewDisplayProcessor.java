/**
 * 
 */
package sk.seges.pap.singleselect;

import com.google.gwt.user.client.ui.IsWidget;

import sk.seges.acris.scaffold.model.view.compose2.Singleselect;
import sk.seges.pap.ScaffoldConstant;
import sk.seges.pap.ScaffoldNameUtil;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;

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
		ScaffoldNameUtil.prefixIfEnclosed(inputType);
		inputType.addPackageSufix("." + ScaffoldConstant.PRES_PKG);
		return inputType.addClassSufix(Singleselect.class.getSimpleName() + ScaffoldConstant.DISPLAY_SUFFIX);
	}

	@Override
	protected void processElement(ProcessorContext context) {
	}

}
