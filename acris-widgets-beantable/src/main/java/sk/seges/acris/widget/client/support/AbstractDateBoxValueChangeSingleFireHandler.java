/**
 * 
 */
package sk.seges.acris.widget.client.support;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author stefan.sivak
 *
 * @since Jul 25, 2013
 */
public abstract class AbstractDateBoxValueChangeSingleFireHandler implements ValueChangeHandler<Date> {

	private final DateBox dateBox;

	private Date lastDate;

	/**
	 * 
	 */
	public AbstractDateBoxValueChangeSingleFireHandler(DateBox dateBox) {
		this.dateBox = dateBox;
	}

	public abstract void onSingleValueChange(ValueChangeEvent<Date> event);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(
	 * com.google.gwt.event.logical.shared.ValueChangeEvent)
	 */
	@Override
	public void onValueChange(ValueChangeEvent<Date> event) {
		Date newDate = dateBox.getValue();
		newDate.setHours(0);
		newDate.setMinutes(0);
		newDate.setSeconds(0);

		if (!newDate.equals(lastDate)) {
			lastDate = newDate;
			onSingleValueChange(event);
		}
	}



}
