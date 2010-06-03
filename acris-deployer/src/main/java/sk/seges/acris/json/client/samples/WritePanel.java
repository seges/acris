package sk.seges.acris.json.client.samples;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WritePanel extends VerticalPanel {

	public static final String GRAY_COLOR = "#646464";
	public static final String PURPLE_COLOR = "#ab0055";
	public static final String BLACK_COLOR = "#000000";
	public static final String BLUE_COLOR = "#0000c0";

	private Map<String, Widget> widgets = new HashMap<String, Widget>();

	private int indent = 0;
	private boolean doIndent = true;

	private static final int INDENT_SIZE = 4;

	private FlowPanel currentLine;

	public WritePanel() {
		currentLine = new FlowPanel();
		add(currentLine);
	}

	@SuppressWarnings("unchecked")
	public String getWidgetText(String property) {
		Widget w = widgets.get(property);
		
		if (w instanceof HasValue<?>) {
			return ((HasValue<String>)w).getValue();
		} else if (w instanceof HasText) {
			return ((HasText)w).getText();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void setWidgetText(String property, String text) {
		Widget w = widgets.get(property);
		
		if (w instanceof HasValue<?>) {
			((HasValue<String>)w).setValue(text);
		} else if (w instanceof HasText) {
			((HasText)w).setText(text);
		}
	}

	public WritePanel addLabelWidget(String text) {
		return addLabelWidget(text, BLACK_COLOR);
	}

	public WritePanel addLabelWidgetb(String text) {
		return addLabelWidget(text, BLUE_COLOR);
	}

	public WritePanel addLabelWidgetp(String text) {
		return addLabelWidget(text, PURPLE_COLOR);
	}

	public WritePanel addLabelWidgetg(String text) {
		return addLabelWidget(text, GRAY_COLOR);
	}

	public WritePanel addLabelWidget(String text, String color) {
		final Label label = new Label(text);
		DOM.setStyleAttribute(label.getElement(), "color", color);
		DOM.setStyleAttribute(label.getElement(), "border", "none");
		DOM.setStyleAttribute(label.getElement(), "display", "inline");
		setBasicStyle(label.getElement());
		currentLine.add(label);
		widgets.put(text, label);
		return this;
	}

	public WritePanel indent() {
		indent += INDENT_SIZE;
		return this;
	}

	public WritePanel outdent() {
		indent -= INDENT_SIZE;
		return this;
	}

	private void setBasicStyle(Element element) {
		DOM.setStyleAttribute(element, "fontFamily", "\"Courier New\"");
		DOM.setStyleAttribute(element, "fontSize", "12pt");
		if (doIndent) {
			DOM.setStyleAttribute(element, "paddingLeft", (indent * 5) + "px");
		}
	}

	public WritePanel write(String text) {
		return write(text, BLACK_COLOR);
	}

	public WritePanel writeb(String text) {
		return write(text, BLUE_COLOR);
	}

	public WritePanel writep(String text) {
		return write(text, PURPLE_COLOR);
	}

	public WritePanel writeg(String text) {
		return write(text, GRAY_COLOR);
	}
	
	public WritePanel write(String text, String color) {
		Label label = new Label(text);
		DOM.setStyleAttribute(label.getElement(), "color", color);
		DOM.setStyleAttribute(label.getElement(), "display", "inline");
		setBasicStyle(label.getElement());
		currentLine.add(label);
		doIndent = false;
		return this;
	}

	public WritePanel nextLine() {
		currentLine = new FlowPanel();
		add(currentLine);
		doIndent = true;
		return this;
	}

	public WritePanel writeln(String text) {
		return writeln(text, BLACK_COLOR);
	}

	public WritePanel writelnb(String text) {
		return writeln(text, BLUE_COLOR);
	}

	public WritePanel writelnp(String text) {
		return writeln(text, PURPLE_COLOR);
	}

	public WritePanel writelng(String text) {
		return writeln(text, GRAY_COLOR);
	}

	public WritePanel writeln(String text, String color) {
		write(text, color);
		return nextLine();
	}

	public WritePanel addWidget(String text) {
		return addWidget(text, BLACK_COLOR);
	}

	public WritePanel addWidgetb(String text) {
		return addWidget(text, BLUE_COLOR);
	}

	public WritePanel addWidgetp(String text) {
		return addWidget(text, PURPLE_COLOR);
	}

	public WritePanel addWidgetg(String text) {
		return addWidget(text, GRAY_COLOR);
	}

	public WritePanel addWidget(String text, String color) {
		final TextBox textBox = new TextBox();
		DOM.setStyleAttribute(textBox.getElement(), "color", color);
		DOM.setStyleAttribute(textBox.getElement(), "border", "none");
		DOM.setStyleAttribute(textBox.getElement(), "display", "inline");
		setBasicStyle(textBox.getElement());
		currentLine.add(textBox);

		widgets.put(text, textBox);

		final Label label = new Label(text);
		DOM.setStyleAttribute(label.getElement(), "color", color);
		DOM.setStyleAttribute(label.getElement(), "display", "inline");
		DOM.setStyleAttribute(label.getElement(), "position", "absolute");
		DOM.setStyleAttribute(label.getElement(), "left", "-100px");
		DOM.setStyleAttribute(label.getElement(), "top", "-100px");
		setBasicStyle(label.getElement());
		currentLine.add(label);

		DeferredCommand.addCommand(new Command() {

			@Override
			public void execute() {
				int width = label.getOffsetWidth() + 2;
				textBox.setWidth(width + "px");
			}
		});

		textBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {

				String text;

				if (isMapableCharacter(event.getNativeEvent())) {
					text = textBox.getText() + getCharCode(event.getNativeEvent());
				} else {
					text = textBox.getText();
				}

				label.setText(text);
				int width = label.getOffsetWidth() + 2;
				textBox.setWidth(width + "px");

			}
		});

		textBox.setText(text);

		doIndent = false;
		return this;
	}

	private native boolean isMapableCharacter(NativeEvent e)/*-{
		var code = e.keyCode ? e.keyCode : e.charCode ? e.charCode : e.which ? e.which : void 0;
		if( e.which ) {
		    if( code && ( code > 33 ) && ( ! ( e.ctrlKey || e.altKey ) ) ){
		        return true;
		    }
		}
		return false;
	}-*/;

	private native char getCharCode(NativeEvent e)/*-{
		var code = e.keyCode ? e.keyCode : e.charCode ? e.charCode : e.which ? e.which : void 0;
		if( e.which ) {
		    if( code && ( code > 33 ) && ( ! ( e.ctrlKey || e.altKey ) ) ){
		        return code;
		    }
		}
		return void 0;
	}-*/;
}