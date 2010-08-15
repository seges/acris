package sk.seges.acris.mvp.client.view.core.adapter.smartgwt;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.smartgwt.client.widgets.form.fields.FormItem;


public class HasValueSmartProvider<T> extends Composite implements HasValue<T> {

	private FormItem formItem;
	
	public HasValueSmartProvider(FormItem formItem) {
		this.formItem = formItem;
	}
	
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getValue() {
		return (T) formItem.getValue();
	}

	@Override
	public void setValue(T value) {
		if (value instanceof String) {
			formItem.setValue((String)value);
		} else if (value instanceof Boolean) {
			formItem.setValue((Boolean)value);
	    } else if (value instanceof Date) {
			formItem.setValue((Date)value);
	    } else if (value instanceof Double) {
			formItem.setValue((Double)value);
	    } else if (value instanceof Float) {
			formItem.setValue((Float)value);
	    } else if (value instanceof Integer) {
			formItem.setValue((Integer)value);
		} else {
			formItem.setValue(value.toString());
		}
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		setValue(value);
	}
}