package sk.seges.acris.binding.client.providers;

import org.gwt.beansbinding.core.client.ConverterProvider;

import sk.seges.acris.binding.client.providers.annotations.OneToOne;
import sk.seges.acris.binding.client.providers.support.AbstractBindingChangeHandlerAdapterProvider;

import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * @author fat
 */
@OneToOne
public final class TextBoxBaseAdapterProvider extends
		AbstractBindingChangeHandlerAdapterProvider<TextBoxBase, Object> {

	@Override
	public Class<TextBoxBase> getBindingWidgetClasses() {
		return TextBoxBase.class;
	}

	@Override
	protected String getValue(TextBoxBase widget) {
		return widget.getText();
	}

	@Override
	protected void setValue(TextBoxBase widget, Object t) {
		Object convertedValue = t == null ? "" : ConverterProvider.defaultConvert(t, String.class);
		if (convertedValue instanceof String) {
			widget.setText((String) convertedValue);
		} else {
			widget.setText(t.toString());
		}
	}
}