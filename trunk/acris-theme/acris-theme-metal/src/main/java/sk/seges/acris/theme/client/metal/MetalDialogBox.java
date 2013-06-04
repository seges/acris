package sk.seges.acris.theme.client.metal;

import sk.seges.acris.theme.client.annotation.Theme;
import sk.seges.acris.theme.client.annotation.ThemeElements;
import sk.seges.acris.theme.client.annotation.ThemeElements.ThemeElement;
import sk.seges.acris.theme.client.annotation.ThemeSupport;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DialogBoxHelper;
import com.google.gwt.user.client.ui.Widget;

@Theme(MetalTheme.NAME)
@ThemeSupport(widgetClass = DialogBox.class, elementName = "dialogBox")
@ThemeElements({
	@ThemeElement(MetalDialogBox.HEADER),
	@ThemeElement(MetalDialogBox.CAPTION),
	@ThemeElement(MetalDialogBox.CLOSE)
	})
public abstract class MetalDialogBox extends DialogBox implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

	protected static final String HEADER = "header";
	protected static final String CAPTION = "caption";
	protected static final String CLOSE = "close";
	
	private String text = null;

	private boolean dragging;
	private int dragStartX;
	private int dragStartY;

	protected MetalDialogBox(com.google.gwt.user.client.Element element) {
		super();
		element.appendChild(DialogBoxHelper.getDialogBoxWidget(this).getElement());
	}

	@Override
	public void setText(String text) {
		this.text = text;

		new Widget() {
		
			public <T extends MouseDownHandler & MouseMoveHandler & MouseUpHandler> void initialize(Element header, T handler) {
				setElement(header);
				addDomHandler(handler, MouseDownEvent.getType());
				addDomHandler(handler, MouseUpEvent.getType());
				addDomHandler(handler, MouseMoveEvent.getType());
				onAttach();
			}
			
		}.initialize(getElement(HEADER), this);

		getElement(CAPTION).setInnerText(text);
	}

	public String getText() {
		return text;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		dragging = true;
		DOM.setCapture(getElement(HEADER));
		dragStartX = event.getX();
		dragStartY = event.getY();
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (dragging) {
			int absX = event.getX() + getAbsoluteLeft();
			int absY = event.getY() + getAbsoluteTop();
			setPopupPosition(absX - dragStartX, absY - dragStartY);
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		dragging = false;
		DOM.releaseCapture(getElement(HEADER));
	}

	protected abstract Element getElement(String name);
}