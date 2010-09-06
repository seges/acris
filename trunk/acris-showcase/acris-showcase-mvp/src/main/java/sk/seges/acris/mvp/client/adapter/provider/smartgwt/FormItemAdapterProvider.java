package sk.seges.acris.mvp.client.adapter.provider.smartgwt;

import org.gwt.beansbinding.core.client.ConverterProvider;

import sk.seges.acris.binding.client.providers.annotations.OneToOne;
import sk.seges.acris.mvp.client.adapter.provider.smartgwt.core.AbstractBindingSmartChangedHandlerAdapterProvider;

import com.smartgwt.client.widgets.form.fields.FormItem;

@OneToOne
public class FormItemAdapterProvider extends AbstractBindingSmartChangedHandlerAdapterProvider<FormItem, Object> {

	@Override
	public Class<FormItem> getBindingWidgetClasses() {
		return FormItem.class;
	}

	@Override
	protected Object getValue(FormItem widget) {
		return widget.getValue();
	}

	@Override
	protected void setValue(FormItem widget, Object t) {
		Object convertedValue = t == null ? "" : ConverterProvider.defaultConvert(t, String.class);
		if (convertedValue instanceof String) {
			widget.setValue((String) convertedValue);
		} else {
			widget.setValue(t.toString());
		}
	}

	@Override
	public String getBindingWidgetProperty() {
		return "value";
	}
}