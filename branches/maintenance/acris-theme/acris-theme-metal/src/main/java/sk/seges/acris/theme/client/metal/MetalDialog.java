package sk.seges.acris.theme.client.metal;

import sk.seges.acris.theme.client.annotation.Theme;
import sk.seges.acris.theme.client.annotation.ThemeElements;
import sk.seges.acris.theme.client.annotation.ThemeElements.ThemeElement;
import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.widget.client.Dialog;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DialogBoxHelper;
import com.google.gwt.user.client.ui.Widget;

@Theme(MetalTheme.NAME)
@ThemeSupport(widgetClass = Dialog.class, elementName = "dialogBox", template = @UiTemplate("MetalDialogBox.ui.xml"))
@ThemeElements({
	@ThemeElement(MetalDialogBox.HEADER),
	@ThemeElement(MetalDialogBox.CAPTION),
	@ThemeElement(MetalDialogBox.CLOSE)
	})
public abstract class MetalDialog extends Dialog implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

	protected static final String HEADER = "header";
	protected static final String CAPTION = "caption";
	protected static final String CLOSE = "close";
	
	private String text = null;
	private String html = null;

	private boolean dragging;
	private int dragStartX;
	private int dragStartY;

	protected MetalDialog(com.google.gwt.user.client.Element element) {
		super();
		element.appendChild(DialogBoxHelper.getDialogBoxWidget(this).getElement());
	}

	private void initializeHeader() {
		new Widget() {
			
			public <T extends MouseDownHandler & MouseMoveHandler & MouseUpHandler> void initialize(Element header, T handler) {
				setElement(header);
				addDomHandler(handler, MouseDownEvent.getType());
				addDomHandler(handler, MouseUpEvent.getType());
				addDomHandler(handler, MouseMoveEvent.getType());
				onAttach();
			}
			
		}.initialize(getElement(HEADER), this);
	}
	
	@Override
	public void setText(String text) {
		this.text = text;
		initializeHeader();
		getElement(CAPTION).setInnerText(text);
	}

	@Override
	public void setHTML(String html) {
		this.html = html;
		initializeHeader();
		getElement(CAPTION).setInnerHTML(html);
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String getHTML() {
		return html;
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