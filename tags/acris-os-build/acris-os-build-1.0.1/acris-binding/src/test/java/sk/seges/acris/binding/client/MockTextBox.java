/**
 * 
 */
package sk.seges.acris.binding.client;

import sk.seges.acris.binding.client.providers.support.widget.HasEnabled;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;

public class MockTextBox implements HasEnabled, HasValue<String>, HasText {
	private boolean enabled = true;
	private String value;

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		setValue(value, true);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		this.value = value;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return null;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {}

	@Override
	public String getText() {
		return value;
	}

	@Override
	public void setText(String text) {
		setValue(text);
	}

}