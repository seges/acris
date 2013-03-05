package sk.seges.acris.theme.pap.specific;

import javax.lang.model.type.TypeKind;

import sk.seges.acris.widget.client.Dialog;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;

public class ThemeDialogBoxProcessor extends AbstractComponentSpecificProcessor {

	@Override
	protected ExecutableMethodDefinition[] getOuterMethodDefinitions() {
		return new ExecutableMethodDefinition[] {
				new ExecutableMethodDefinition(PopupPanel.class, "setPopupPosition").params(TypeKind.INT, TypeKind.INT),
				new ExecutableMethodDefinition(PopupPanel.class, "getPopupTop"),
				new ExecutableMethodDefinition(PopupPanel.class, "getPopupLeft"),
				new ExecutableMethodDefinition(PopupPanel.class, "show"),
		};
	}
    
	@Override
	protected Class<?>[] getComponentClasses() {
		return new Class<?>[] {DialogBox.class, Dialog.class};
	}
}