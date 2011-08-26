package sk.seges.acris.reporting.client.panel.parameter;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateTypePanel extends AbstractTypePanel<String> {
	
	private DateBox dateBox;
	
	@Override
	public String getValue() {
		Date value = dateBox.getValue();
		if (value == null)
			return null;
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		DateTimeFormat format = DateTimeFormat.getFormat("yyyyMMddHHmmss");
		return format.format(value);
	}
	
	@Override
	public void setValue(String t) {
		DateTimeFormat format = DateTimeFormat.getFormat("yyyyMMddHHmmss");
		dateBox.setValue(format.parse(t));
	}

	@Override
	protected void initOwnComponents() {
		dateBox = GWT.create(DateBox.class);
		container.add(dateBox);
	}
}
