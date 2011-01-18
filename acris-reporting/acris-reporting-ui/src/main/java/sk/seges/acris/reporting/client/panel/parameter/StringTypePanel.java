package sk.seges.acris.reporting.client.panel.parameter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.TextBox;

public class StringTypePanel extends AbstractTypePanel<String> {
	
	private TextBox textBox;

	@Override
	protected void initOwnComponents() {
		textBox = GWT.create(TextBox.class);
		container.add(textBox);
	}
	
	@Override
	public void setValue(String t) {
		textBox.setValue(t);
	}

	@Override
	public String getValue() {
		return textBox.getValue();
	}
}
