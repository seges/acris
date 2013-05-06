package sk.seges.acris.widget.client.celltable.filterable;

import java.io.Serializable;

import sk.seges.acris.widget.client.celltable.AbstractFilterableTable.Validator;
import sk.seges.sesam.dao.SimpleExpression;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class InputFilterColumn<T extends Comparable<? extends Serializable>> extends AbstractFilterableCell<SimpleExpression<T>> {

	protected interface Template extends SafeHtmlTemplates {
		@Template("<div style=\"\">{0}</div>")
		SafeHtml header(String columnName);

		@Template("<input type=\"text\" id=\"{0}\" value=\"{1}\" tabindex=\"-1\"></input>")
		SafeHtml input(String id, String value);
	}

	protected static Template template;
	protected final String text;
	
	private static final int ENTER = 13;

	protected static final String FILTER_INPUT_PREFIX = "filterInput";

	private boolean isChanged = false;
	private final Validator<T> validator;

	public InputFilterColumn(Validator<T> validator, String text) {
		super("keydown", "keyup", "change", "blur", "click", "focus");
		this.validator = validator;
		this.text = text;
		initTemplate();		
	}
	
	protected void initTemplate(){
		if (template == null) {
			template = GWT.create(Template.class);
		}
	}

	protected String convertToString(T t) {
		return validator.toString(t);
	}

	protected T convertFromString(String value) {
		return validator.getValue(value);
	}

	@Override
	protected String valueToString(SimpleExpression<T> value, int index) {
		return validator.toString(value.getValue());
	}
	
	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, SimpleExpression<T> value, SafeHtmlBuilder sb) {
		sb.append(template.header(text));
		sb.append(template.input(FILTER_INPUT_PREFIX + value.getProperty(), convertToString(value.getValue())));
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, SimpleExpression<T> value, NativeEvent event, ValueUpdater<SimpleExpression<T>> valueUpdater) {
		if (value == null)
			return;
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if ("keyup".equals(event.getType())) {
			if (hasFocus(FILTER_INPUT_PREFIX + value.getProperty())) {
				isChanged = true;
				InputElement elem = getInputElement(parent);
				
				if (elem.getValue() != null && !elem.getValue().isEmpty()) {
					value.setValue(convertFromString(elem.getValue()));
				} else {
					value.setValue(null);
				}
				
				if (valueUpdater != null) {
					if (event.getKeyCode() == ENTER) {
						valueUpdater.update(value);
					}
				}
			}
		} else if ("click".equals(event.getType())) {
			changeSorting(value, valueUpdater);
		} else if ("blur".equals(event.getType())) {
			isChanged = false;
		}
	}

	protected void changeSorting(SimpleExpression<T> value, ValueUpdater<SimpleExpression<T>> valueUpdater) {
		if (!hasFocus(FILTER_INPUT_PREFIX + value.getProperty())) {
			if (valueUpdater != null) {
				SimpleExpression<T> newExpression = new SimpleExpression<T>();
				newExpression.setProperty(value.getProperty());

				valueUpdater.update(newExpression);
			}
		}
	}
	
	protected InputElement getInputElement(Element parent) {
		Element elem = parent.getElementsByTagName("input").getItem(0);
		assert (elem.getClass() == InputElement.class);
		return elem.cast();
	}

	public boolean isChanged() {
		return isChanged;
	}
}