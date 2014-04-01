package sk.seges.acris.widget.client.resize;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.widget.client.util.WidgetUtils;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Resizable panel with handles. Add a ResizableListener to catch onResize events.
 * 
 * @author Martin
 */
public class ResizablePanel extends HTML {

	public static final String RESIZABLE_PANEL_STYLE = "resizable-panel";
	public static final String RESIZABLE_CONTENT_STYLE = "resizable-content";
	public static final String RESIZABLE_HANDLE_STYLE = "resizable-handle";
	public static final String X_RESIZABLE_HANDLE_STYLE = "x-resizable-handle";
	public static final String RESIZABLE_OVERLAY_STYLE = "resizable-overlay";
	public static final String RESIZABLE_PROXY_STYLE = "resizable-proxy";

	private List<ResizableListener> resizableListeners = new ArrayList<ResizableListener>();

	private Element proxyElement = null;
	private Element handleElement = null;
	private Element contentElement = null;
	private Element overlayElement = null;

	private Cursor cursorResize = Cursor.AUTO;

	private boolean dragging = false;
	private boolean enabled = true;
	private boolean keepAspect = false;

	public ResizablePanel() {
		this(true, false);
	}

	public ResizablePanel(boolean resizeEnabled) {
		this(resizeEnabled, false);
	}

	public ResizablePanel(boolean resizeEnabled, boolean keepAspect) {
		super();

		this.enabled = resizeEnabled;
		this.keepAspect = keepAspect;

		initComponents();
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if (enabled) {
			sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEMOVE);
		} else {
			sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP);
		}
	}

	private void initComponents() {
		getElement().setClassName(RESIZABLE_PANEL_STYLE);
		//relative position is there cause of borders with absolute positioning
		getElement().getStyle().setPosition(Position.RELATIVE);

		handleElement = DOM.createDiv();
		handleElement.addClassName(RESIZABLE_HANDLE_STYLE);
		handleElement.getStyle().setPosition(Position.RELATIVE);
		handleElement.getStyle().setOverflow(Overflow.HIDDEN);
		getElement().appendChild(handleElement);

		Element handleN = createHandle("north");
		Element handleE = createHandle("east");
		Element handleS = createHandle("south");
		Element handleW = createHandle("west");
		Element handleNE = createHandle("northeast");
		Element handleSE = createHandle("southeast");
		Element handleSW = createHandle("southwest");
		Element handleNW = createHandle("northwest");
		getElement().appendChild(handleN);
		getElement().appendChild(handleE);
		getElement().appendChild(handleS);
		getElement().appendChild(handleW);
		getElement().appendChild(handleNE);
		getElement().appendChild(handleSE);
		getElement().appendChild(handleSW);
		getElement().appendChild(handleNW);

		contentElement = DOM.createDiv();
		contentElement.addClassName(RESIZABLE_CONTENT_STYLE);
		handleElement.appendChild(contentElement);

		proxyElement = DOM.createDiv();
		proxyElement.addClassName(RESIZABLE_PROXY_STYLE);
		proxyElement.getStyle().setProperty("border", "1px dashed #3B5A82");
		proxyElement.getStyle().setPosition(Position.ABSOLUTE);
		proxyElement.getStyle().setLeft(0, Unit.PX);
		proxyElement.getStyle().setTop(0, Unit.PX);
		proxyElement.getStyle().setZIndex(100000);
		proxyElement.getStyle().setWidth(getBorderSize(this.getOffsetWidth()), Unit.PX);
		proxyElement.getStyle().setHeight(getBorderSize(this.getOffsetHeight()), Unit.PX);
		proxyElement.getStyle().setDisplay(Display.NONE);
		getBody().appendChild(proxyElement);

		com.google.gwt.dom.client.Element childElement = getBody().getFirstChildElement();
		while (childElement != null) {
			if (childElement.getClassName().contains(RESIZABLE_OVERLAY_STYLE)) {
				overlayElement = (Element) childElement;
				return;
			}
			childElement = childElement.getNextSiblingElement();
		}

		overlayElement = DOM.createDiv();
		overlayElement.addClassName(RESIZABLE_OVERLAY_STYLE);
		overlayElement.getStyle().setPosition(Position.ABSOLUTE);
		overlayElement.getStyle().setLeft(0, Unit.PX);
		overlayElement.getStyle().setTop(0, Unit.PX);
		overlayElement.getStyle().setZIndex(200000);
		overlayElement.getStyle().setWidth(100, Unit.PCT);
		overlayElement.getStyle().setHeight(100, Unit.PCT);
		overlayElement.getStyle().setDisplay(Display.NONE);
		getBody().appendChild(overlayElement);
	}

	private Element createHandle(String suffix) {
		Element handle = DOM.createDiv();
		handle.addClassName(X_RESIZABLE_HANDLE_STYLE);
		handle.addClassName(X_RESIZABLE_HANDLE_STYLE + "-" + suffix);
		return handle;
	}

	private int getBorderSize(int offsetSize) {
		return offsetSize >= 8 ? offsetSize - 2 : 6;
	}

	private int getAspectWidth(int height) {
		int width = (height * this.getOffsetWidth()) / this.getOffsetHeight();
		return width > 0 ? width : 1;
	}

	private int getAspectHeight(int width) {
		int height = (width * this.getOffsetHeight()) / this.getOffsetWidth();
		return height > 0 ? height : 1;
	}

	@Override
	public String getHTML() {
		return contentElement.getInnerHTML();
	}

	@Override
	public void setHTML(String html) {
		contentElement.setInnerHTML(html);
	}

	public Element getContentElement() {
		return contentElement;
	}

	public void setContentElement(Element element) {
		contentElement.appendChild(element);
	}

	@Override
	public void onBrowserEvent(Event event) {
		if (enabled) {
			switch (DOM.eventGetType(event)) {
			case Event.ONMOUSEDOWN:
				if (event.getButton() == NativeEvent.BUTTON_LEFT) {
					event.preventDefault();
					onMouseDown(event);
				}
				break;
			case Event.ONMOUSEUP:
				if (event.getButton() == NativeEvent.BUTTON_LEFT) {
					event.preventDefault();
					onMouseUp(event);
				}
				break;
			case Event.ONMOUSEMOVE:
				event.preventDefault();
				onMouseMove(event);
				break;
			default:
				super.onBrowserEvent(event);
				break;
			}
		} else {
			switch (DOM.eventGetType(event)) {
			case Event.ONMOUSEDOWN:
				if (event.getButton() == NativeEvent.BUTTON_LEFT) {
					event.preventDefault();
					onMouseDown(event);
				}
				break;
			case Event.ONMOUSEUP:
				if (event.getButton() == NativeEvent.BUTTON_LEFT) {
					event.preventDefault();
					onMouseUp(event);
				}
				break;
			default:
				super.onBrowserEvent(event);
				break;
			}
		}
	}

	protected Element getBody() {
		return RootPanel.getBodyElement();
	}

	protected void onMouseDown(Event event) {
		if (enabled && !dragging) {
			cursorResize = getCursorResize(event);
			if (isCursorResize(cursorResize)) {
				dragging = true;
				DOM.setCapture(this.getElement());
				event.stopPropagation();

				com.google.gwt.dom.client.Element childElement = getElement().getFirstChildElement();
				while (childElement != null) {
					if (childElement.getClassName().contains(X_RESIZABLE_HANDLE_STYLE)) {
						childElement.getStyle().setDisplay(Display.NONE);
					}
					childElement = childElement.getNextSiblingElement();
				}

				overlayElement.getStyle().setCursor(cursorResize);
				overlayElement.getStyle().setWidth(getBody().getOffsetWidth(), Unit.PX);
				overlayElement.getStyle().setHeight(getBody().getOffsetHeight(), Unit.PX);
				overlayElement.getStyle().setDisplay(Display.BLOCK);

				proxyElement.getStyle().setCursor(cursorResize);
				proxyElement.getStyle().setLeft(this.getAbsoluteLeft(), Unit.PX);
				proxyElement.getStyle().setTop(this.getAbsoluteTop(), Unit.PX);
				proxyElement.getStyle().setWidth(getBorderSize(this.getOffsetWidth()), Unit.PX);
				proxyElement.getStyle().setHeight(getBorderSize(this.getOffsetHeight()), Unit.PX);
				proxyElement.getStyle().setDisplay(Display.BLOCK);

				onResizing(this.getOffsetWidth(), this.getOffsetHeight());
			}
		}
	}

	protected void onMouseUp(Event event) {
		if (enabled && dragging) {
			dragging = false;
			DOM.releaseCapture(this.getElement());
			event.stopPropagation();

			int width = proxyElement.getOffsetWidth();
			int height = proxyElement.getOffsetHeight();

			com.google.gwt.dom.client.Element childElement = getElement().getFirstChildElement();
			while (childElement != null) {
				if (childElement.getClassName().contains(X_RESIZABLE_HANDLE_STYLE)) {
					childElement.getStyle().clearDisplay();
				}
				childElement = childElement.getNextSiblingElement();
			}

			overlayElement.getStyle().clearCursor();
			overlayElement.getStyle().setWidth(getBody().getOffsetWidth(), Unit.PX);
			overlayElement.getStyle().setHeight(getBody().getOffsetHeight(), Unit.PX);
			overlayElement.getStyle().setDisplay(Display.NONE);

			proxyElement.getStyle().clearCursor();
			proxyElement.getStyle().setLeft(this.getAbsoluteLeft(), Unit.PX);
			proxyElement.getStyle().setTop(this.getAbsoluteTop(), Unit.PX);
			proxyElement.getStyle().setDisplay(Display.NONE);

			getElement().getStyle().setWidth(width, Unit.PX);
			getElement().getStyle().setHeight(height, Unit.PX);

			contentElement.getStyle().setWidth(width, Unit.PX);
			contentElement.getStyle().setHeight(height, Unit.PX);

			onResize(width, height);
		}
	}

	protected void onMouseMove(Event event) {
		if (enabled && dragging) {
			event.stopPropagation();

			if (this.getOffsetWidth() > 0 && this.getOffsetHeight() > 0) {
				int cursorX = DOM.eventGetClientX(event);
				int cursorY = DOM.eventGetClientY(event);

				int thisX = this.getAbsoluteLeft() - WidgetUtils.getPageScrollX();
				int thisY = this.getAbsoluteTop() - WidgetUtils.getPageScrollY();

				int width = 0;
				int height = 0;

				if (cursorResize == Cursor.SE_RESIZE) {
					if (cursorX > thisX && cursorY > thisY) {
						height = cursorY - thisY;

						if (keepAspect && !event.getShiftKey()) {
							width = getAspectWidth(height);
						} else {
							width = cursorX - thisX;
						}
					}
				} else if (cursorResize == Cursor.NW_RESIZE) {
					if (cursorX < thisX + this.getOffsetWidth() - 6 && cursorY < thisY + this.getOffsetHeight() - 6) {
						height = thisY - cursorY + this.getOffsetHeight();
						proxyElement.getStyle().setTop(cursorY + WidgetUtils.getPageScrollY(), Unit.PX);

						if (keepAspect && !event.getShiftKey()) {
							width = getAspectWidth(height);
							proxyElement.getStyle().setLeft(thisX + this.getOffsetWidth() - width + WidgetUtils.getPageScrollX(), Unit.PX);
						} else {
							width = thisX - cursorX + this.getOffsetWidth();
							proxyElement.getStyle().setLeft(cursorX + WidgetUtils.getPageScrollX(), Unit.PX);
						}
					}
				} else if (cursorResize == Cursor.NE_RESIZE) {
					if (cursorX > thisX && cursorY < thisY + this.getOffsetHeight() - 6) {
						height = thisY - cursorY + this.getOffsetHeight();
						proxyElement.getStyle().setTop(cursorY + WidgetUtils.getPageScrollY(), Unit.PX);

						if (keepAspect && !event.getShiftKey()) {
							width = getAspectWidth(height);
						} else {
							width = cursorX - thisX;
						}
					}
				} else if (cursorResize == Cursor.SW_RESIZE) {
					if (cursorX < thisX + this.getOffsetWidth() - 6 && cursorY > thisY) {
						height = cursorY - thisY;

						if (keepAspect && !event.getShiftKey()) {
							width = getAspectWidth(height);
							proxyElement.getStyle().setLeft(thisX + this.getOffsetWidth() - width + WidgetUtils.getPageScrollX(), Unit.PX);
						} else {
							width = thisX - cursorX + this.getOffsetWidth();
							proxyElement.getStyle().setLeft(cursorX + WidgetUtils.getPageScrollX(), Unit.PX);
						}
					}
				} else if (cursorResize == Cursor.N_RESIZE) {
					if (cursorX > thisX && cursorY < thisY + this.getOffsetHeight() - 6) {
						height = thisY - cursorY + this.getOffsetHeight();
						proxyElement.getStyle().setTop(cursorY + WidgetUtils.getPageScrollY(), Unit.PX);

						if (keepAspect && !event.getShiftKey()) {
							width = getAspectWidth(height);
						} else {
							width = this.getOffsetWidth();
						}
					}
				} else if (cursorResize == Cursor.E_RESIZE) {
					if (cursorX > thisX && cursorY > thisY) {
						width = cursorX - thisX;

						if (keepAspect && !event.getShiftKey()) {
							height = getAspectHeight(width);
						} else {
							height = this.getOffsetHeight();
						}
					}
				} else if (cursorResize == Cursor.S_RESIZE) {
					if (cursorX > thisX && cursorY > thisY) {
						height = cursorY - thisY;

						if (keepAspect && !event.getShiftKey()) {
							width = getAspectWidth(height);
						} else {
							width = this.getOffsetWidth();
						}
					}
				} else if (cursorResize == Cursor.W_RESIZE) {
					if (cursorX < thisX + this.getOffsetWidth() - 6 && cursorY > thisY) {
						width = thisX - cursorX + this.getOffsetWidth();
						proxyElement.getStyle().setLeft(cursorX + WidgetUtils.getPageScrollX(), Unit.PX);

						if (keepAspect && !event.getShiftKey()) {
							height = getAspectHeight(width);
						} else {
							height = this.getOffsetHeight();
						}
					}
				}

				if (width > 0 && height > 0) {
					proxyElement.getStyle().setWidth(getBorderSize(width), Unit.PX);
					proxyElement.getStyle().setHeight(getBorderSize(height), Unit.PX);

					onResizing(width, height);
				}
			}
		}
	}

	protected boolean isCursorResize(Cursor cursor) {
		return cursor != Cursor.AUTO;
	}

	protected Cursor getCursorResize(Event event) {
		int cursorX = DOM.eventGetClientX(event);
		int cursorY = DOM.eventGetClientY(event);

		int thisX = this.getAbsoluteLeft() - WidgetUtils.getPageScrollX();
		int thisY = this.getAbsoluteTop() - WidgetUtils.getPageScrollY();

		int width = this.getOffsetWidth();
		int height = this.getOffsetHeight();

		if (((thisX + width - 6) <= cursorX && cursorX <= (thisX + width))
				&& ((thisY + height - 6) <= cursorY && cursorY <= (thisY + height))) {
			return Cursor.SE_RESIZE;
		} else if (((thisX) <= cursorX && cursorX <= (thisX + 6)) && ((thisY) <= cursorY && cursorY <= (thisY + 6))) {
			return Cursor.NW_RESIZE;
		} else if (((thisX + width - 6) <= cursorX && cursorX <= (thisX + width)) && ((thisY) <= cursorY && cursorY <= (thisY + 6))) {
			return Cursor.NE_RESIZE;
		} else if (((thisX) <= cursorX && cursorX <= (thisX + 6)) && ((thisY + height - 6) <= cursorY && cursorY <= (thisY + height))) {
			return Cursor.SW_RESIZE;
		} else if (((thisX + 6) < cursorX && cursorX < (thisX + width - 6)) && ((thisY) <= cursorY && cursorY <= (thisY + 6))) {
			return Cursor.N_RESIZE;
		} else if (((thisX + width - 6) <= cursorX && cursorX <= (thisX + width))
				&& ((thisY + 6) < cursorY && cursorY < (thisY + height - 6))) {
			return Cursor.E_RESIZE;
		} else if (((thisX + 6) < cursorX && cursorX < (thisX + width - 6))
				&& ((thisY + height - 6) <= cursorY && cursorY <= (thisY + height))) {
			return Cursor.S_RESIZE;
		} else if (((thisX) <= cursorX && cursorX <= (thisX + 6)) && ((thisY + 6) < cursorY && cursorY < (thisY + height - 6))) {
			return Cursor.W_RESIZE;
		}

		return Cursor.AUTO;
	}

	protected void onResize(int width, int height) {
		for (ResizableListener listener : resizableListeners) {
			listener.onResize(width, height);
		}
	}

	protected void onResizing(int width, int height) {
		for (ResizableListener listener : resizableListeners) {
			listener.onResizing(width, height);
		}
	}

	public void addResizableListener(ResizableListener listener) {
		resizableListeners.add(listener);
	}
}
