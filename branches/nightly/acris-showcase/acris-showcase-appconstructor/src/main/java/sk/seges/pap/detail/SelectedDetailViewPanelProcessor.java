/**
 * 
 */
package sk.seges.pap.detail;

import java.util.List;

import sk.seges.acris.scaffold.model.view.compose.SelectedDetail;
import sk.seges.pap.ScaffoldConstant;
import sk.seges.pap.ScaffoldNameUtil;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

import com.google.gwt.user.client.ui.Composite;

/**
 * @author ladislav.gazo
 */
public class SelectedDetailViewPanelProcessor extends FluentProcessor {
	public SelectedDetailViewPanelProcessor() {
		reactsOn(SelectedDetail.class);
		setSuperClass(Composite.class);
		addImplementedInterface(new AlwaysRule() {
			@Override
			public List<MutableDeclaredType> getTypes(
					MutableDeclaredType typeElement) {
				ScaffoldNameUtil.prefixIfEnclosed(typeElement);
				typeElement.addPackageSufix("." + ScaffoldConstant.PRES_PKG);
				MutableDeclaredType displayInterface = typeElement.addClassSufix(SelectedDetail.class
								.getSimpleName() + ScaffoldConstant.DISPLAY_SUFFIX);
				return asList(displayInterface);
			}
		});
	}
	
	@Override
	protected MutableDeclaredType getResultType(MutableDeclaredType inputType) {
		ScaffoldNameUtil.prefixIfEnclosed(inputType);
		inputType.addPackageSufix("." + ScaffoldConstant.VIEW_PKG);
		return inputType.addClassSufix(SelectedDetail.class.getSimpleName()
				+ ScaffoldConstant.PANEL_SUFFIX);
	}

	@Override
	protected void processElement(ProcessorContext context) {
		// TODO Auto-generated method stub
		
	}

}
