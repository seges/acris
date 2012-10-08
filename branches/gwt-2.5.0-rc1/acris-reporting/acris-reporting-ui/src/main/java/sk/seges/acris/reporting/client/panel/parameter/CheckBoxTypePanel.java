package sk.seges.acris.reporting.client.panel.parameter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.CheckBox;

public class CheckBoxTypePanel extends AbstractTypePanel<Boolean> {

	private CheckBox checkBox;
	
	@Override
	protected void initOwnComponents() {
		checkBox = GWT.create(CheckBox.class);
		container.add(checkBox);
	}

	@Override
	public void setValue(String t) {
		checkBox.setValue(Boolean.valueOf(t));
	}
	
	@Override
	public Boolean getValue() {
		return checkBox.getValue();
	}

}
