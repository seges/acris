package sk.seges.acris.widget.client.celltable.filterable;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.datepicker.client.DatePicker;
import sk.seges.acris.widget.client.celltable.AbstractFilterableTable.Validator;
import sk.seges.sesam.shared.model.api.PropertyHolder;
import sk.seges.sesam.shared.model.dto.BetweenExpressionDTO;

import java.util.Date;

public class DateBoxFilterCell extends AbstractFilterableCell<BetweenExpressionDTO> {

	protected interface Template extends SafeHtmlTemplates {
		@Template("<div>{0}</div>")
		SafeHtml header(String columnName);

		@Template("<input type=\"text\" id=\"{0}\" value=\"{1}\"tabindex=\"-1\" class=\"dateFilter\"></input>")
		SafeHtml from(String id, String value);

		@Template("<input type=\"text\" id=\"{0}\" value=\"{1}\"tabindex=\"-1\" class=\"dateFilter\"></input>")
		SafeHtml to(String id, String value);
	}

	private static final String FILTER_INPUT_PREFIX = "filterInput";
	private static final String FROM_SUFFIX = "From";
	private static final String TO_SUFFIX = "To";
	
	protected static Template template;
	protected final String text;

	private static final int ESCAPE = 27;

	private final DatePicker datePicker;
	private int offsetX = 10;
	private int offsetY = 10;
	private Object lastKey;
	private Element lastParent;
	private int lastIndex;
	private int lastColumn;
	private BetweenExpressionDTO lastValue;
	private PopupPanel panel;
	private ValueUpdater<BetweenExpressionDTO> valueUpdater;
	private String suffix = null;
	
	private final Validator validator;
	
	public DateBoxFilterCell(Validator validator, String text) {
		super("click", "keydown");
		this.text = text;
		this.validator = validator;

		initTemplate();
		this.datePicker = new DatePicker();
		initDatePanel();
	}
	
	protected void initDatePanel(){		
		this.panel = new PopupPanel(true, true) {
			@Override
			protected void onPreviewNativeEvent(NativePreviewEvent event) {
				if (Event.ONKEYUP == event.getTypeInt()) {
					if (event.getNativeEvent().getKeyCode() == ESCAPE) {
						// Dismiss when escape is pressed
						panel.hide();
					}
				}
			}
		};
		panel.addCloseHandler(new CloseHandler<PopupPanel>() {
			public void onClose(CloseEvent<PopupPanel> event) {
				lastKey = null;
				lastValue = null;
				lastIndex = -1;
				lastColumn = -1;
				if (lastParent != null && !event.isAutoClosed()) {
					// Refocus on the containing cell after the user selects a value, but
					// not if the popup is auto closed.
					lastParent.focus();
				}
				lastParent = null;
			}
		});
		panel.add(datePicker);

		// Hide the panel and call valueUpdater.update when a date is selected
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				// Remember the values before hiding the popup.
				final Element cellParent = lastParent;
				BetweenExpressionDTO oldValue = lastValue;
				Object key = lastKey;
				int index = lastIndex;
				int column = lastColumn;
				panel.hide();

				// Update the cell and value updater.
				final Date date = event.getValue();

				if (suffix.equals(FROM_SUFFIX)) {
					oldValue.setLoValue(new PropertyHolder(date));
				} else {
					oldValue.setHiValue(new PropertyHolder(date));
				}
				
				//setViewData(key, date);
				setValue(new Context(index, column, key), cellParent, oldValue);
				
				if (valueUpdater != null) {
					valueUpdater.update(oldValue);
				}
			}
		});
	}
	
	protected void initTemplate() {
		if (template == null) {
			template = GWT.create(Template.class);
		}
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, BetweenExpressionDTO value, NativeEvent event,
			ValueUpdater<BetweenExpressionDTO> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if ("click".equals(event.getType())) {
			onEnterKeyDown(context, parent, value, event, valueUpdater);
		}
	}
	
	@Override
	public void render(Context context, BetweenExpressionDTO value, SafeHtmlBuilder sb) {

		sb.append(template.header(text));

		String s = null;
		if (value.getLoValue() != null) {
			s = validator.toString(value.getLoValue());
		}
		if (s != null) {
			sb.append(template.from(FILTER_INPUT_PREFIX + value.getProperty() + FROM_SUFFIX, s));
		} else {
			sb.append(template.from(FILTER_INPUT_PREFIX + value.getProperty() + FROM_SUFFIX, ""));
		}

		s = null;
		if (value.getHiValue() != null) {
			s = validator.toString(value.getHiValue());
		}
		if (s != null) {
			sb.append(template.from(FILTER_INPUT_PREFIX + value.getProperty() + TO_SUFFIX, s));
		} else {
			sb.append(template.from(FILTER_INPUT_PREFIX + value.getProperty() + TO_SUFFIX, ""));
		}
	}

	private boolean handleDate(Context context, Element parent, BetweenExpressionDTO value, String suffix, ValueUpdater<BetweenExpressionDTO> valueUpdater, Date date) {
		if (hasFocus(FILTER_INPUT_PREFIX + value.getProperty() + suffix)) {
			
			this.lastKey = context.getKey();
			this.lastParent = parent;
			this.lastValue = value;
			this.lastIndex = context.getIndex();
			this.lastColumn = context.getColumn();
			this.valueUpdater = valueUpdater;
			this.suffix = suffix;
			
			if (date != null) {
				datePicker.setCurrentMonth(date);
				datePicker.setValue(date);
			}
			
			panel.setPopupPositionAndShow(new PositionCallback() {
				public void setPosition(int offsetWidth, int offsetHeight) {
					panel.setPopupPosition(lastParent.getAbsoluteLeft() + offsetX, lastParent.getAbsoluteTop() + offsetY);
				}
			});
			
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void onEnterKeyDown(Context context, Element parent, BetweenExpressionDTO value, NativeEvent event,
			ValueUpdater<BetweenExpressionDTO> valueUpdater) {

		boolean filtered = handleDate(context, parent, value, FROM_SUFFIX, valueUpdater, value.getLoValue().getDateValue());

		if (!filtered) {
			filtered = handleDate(context, parent, value, TO_SUFFIX, valueUpdater, value.getHiValue().getDateValue());
		}

		if (!filtered) {
			if (valueUpdater != null) {
				BetweenExpressionDTO newExpression = new BetweenExpressionDTO();
				newExpression.setProperty(value.getProperty());

				valueUpdater.update(newExpression);
			}
		}
	}

	@Override
	protected String valueToString(BetweenExpressionDTO value, int index) {
		
		switch (index) {
		case 1:
			if (value.getLoValue() != null) {
				return validator.toString(value.getLoValue());
			}
			return null;
		case 2:
			if (value.getHiValue() != null) {
				return validator.toString(value.getHiValue());
			}
			return null;
		}
		return null;
	}

}