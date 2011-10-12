package sk.seges.acris.binding.client.providers;

import java.util.Date;

import sk.seges.acris.binding.client.providers.annotations.OneToOne;
import sk.seges.acris.binding.client.providers.support.AbstractBindingValueChangeHandlerAdapterProvider;

import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Provider of the adapter for DateBox. Used for bidirectional binding between
 * Date and DateBox component.
 * 
 * @author fat
 */
@OneToOne
public class DateBoxAdapterProvider extends
		AbstractBindingValueChangeHandlerAdapterProvider<DateBox, Date> {

	protected Date getValue(DateBox datebox) {
		return datebox.getValue();
	}

	protected void setValue(DateBox datebox, Date value) {
		datebox.setValue(value);
	}

	@Override
	public Class<DateBox> getBindingWidgetClasses() {
		return DateBox.class;
	}

	@Override
	public boolean isSupportSuperclass() {
		return false;
	}
}