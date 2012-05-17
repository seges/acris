package sk.seges.acris.widget.client;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.widget.client.event.DialogInitializeEvent;
import sk.seges.acris.widget.client.handler.DialogInitializeHandler;
import sk.seges.acris.widget.client.handler.HasDialogInitializeHandlers;
import sk.seges.acris.widget.client.optionpane.OptionsPanel;
import sk.seges.acris.widget.client.util.WidgetUtils;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Universal popup dialog capable of displaying content widget with/without title. Title can be either string caption or widget also. All styles are
 * inherited from GWT dialog box and extended by own styles.
 * 
 * @author ladislav.gazo
 */
public class Dialog extends DialogBox implements FormHolder, HasDialogInitializeHandlers {

	private static final String STYLE_DIALOG = "acris-cmp-dialog";
	private static final String STYLE_DIALOG_CONTENT_WRAPPER = "acris-cmp-dialogContentWrapper";
	private static final String STYLE_CONTENT = "acris-cmp-content";

	private Widget captionWidget;
	private String caption;
	public Widget content;

	private FlowPanel dialogContentWrapper;
	private OptionsPanel options;

	private ClickHandler hidingClickHandler;

	private List<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

	public Dialog() {
		super();
		init();
	}

	private ClickHandler getHidingClickHandler() {
		if (hidingClickHandler == null) {
			hidingClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Dialog.this.hide();
				}
			};
		}
		return hidingClickHandler;
	}

	public Dialog(boolean autoHide, boolean modal) {
		super(autoHide, modal);
		init();
	}

	public Dialog(boolean autoHide) {
		super(autoHide);
		init();
	}

	protected void setStyles() {
		addStyleName(STYLE_DIALOG);
		dialogContentWrapper.setStyleName(STYLE_DIALOG_CONTENT_WRAPPER);
	}

	private void init() {
		dialogContentWrapper = new FlowPanel();
		setStyles();
		options = new OptionsPanel();
	}

	private boolean initialized = false;

	@Override
	protected void onLoad() {
		super.onLoad();

		if (!initialized) {
			if (content != null) {
				dialogContentWrapper.add(content);
			}
			if (options != null) {
				dialogContentWrapper.add(options);
				options.add(new Cleaner());
			}

			if (caption != null) {
				// only message title should be draggable
				setText(caption);
				if (!dialogContentWrapper.isAttached()) {
					add(dialogContentWrapper);
				}
			} else if (captionWidget != null) {
				setHTML(DOM.getInnerHTML(captionWidget.getElement()));
				if (!dialogContentWrapper.isAttached()) {
					add(dialogContentWrapper);
				}
			} else {
				if (!dialogContentWrapper.isAttached()) {
					add(dialogContentWrapper);
				}
			}
			initialized = true;
		}
	}

	public void clearOptions() {
		options.clearOptions();
	}

	public void reinitialize() {
		initialized = false;
		dialogContentWrapper.clear();
		onLoad();
	}

	/**
	 * Add an option to the list of options. Usually it is a button at least closing the dialog.
	 * 
	 * @param option
	 */
	public void addOption(Widget option) {
		options.addOption(option, getHidingClickHandler());
	}

	/**
	 * Add an option to the list of options with hiding click listener. Usually it is a button at least closing the dialog.
	 * 
	 * @param option
	 * @param addHideAction
	 *            If true dialog hiding click listener will be bundled with option.
	 */
	public void addOption(Widget option, ClickHandler hidingClickHandler) {
		options.addOption(option, hidingClickHandler);
	}

	/**
	 * @param option
	 * @param hidingClickHandler
	 * @return
	 */
	public Widget addOptionAndReturnWidget(Widget option) {
		options.addOption(option, getHidingClickHandler());
		return options.getOption();
	}

	/**
	 * Add an option to the list of options. The widget will not get a click handler closing the dialog. Usually it is a button at least closing the
	 * dialog.
	 * 
	 * @param option
	 */
	public void addOptionWithoutHiding(Widget option) {
		options.addOption(option, null);
	}

	/**
	 * Add a set of options to the existing list of options.
	 * 
	 * @param options
	 */
	public void addOptions(Widget options[]) {
		this.options.addOptions(options, getHidingClickHandler());
	}

	public void addOptions(Widget options[], ClickHandler hidingClickHandler) {
		this.options.addOptions(options, hidingClickHandler);
	}

	/**
	 * Add a separator in the options pane of the dialog.
	 */
	public void addOptionSeparator() {
		options.addOptionSeparator();
	}

	/**
	 * Set widget displayed in the dialog title. Either widget or string caption can be set, not both.
	 * 
	 * @param captionWidget
	 */
	public void setCaptionWidget(Widget captionWidget) {
		if (captionWidget != null) {
			throw new IllegalArgumentException("Cannot set widget caption when string caption is already set");
		}
		this.captionWidget = captionWidget;
	}

	/**
	 * Set string caption displayed in the dialog title. Either widget or string caption can be set, not both.
	 * 
	 * @param caption
	 */
	public void setCaption(String caption) {
		if (captionWidget != null) {
			throw new IllegalArgumentException("Cannot set string caption when widget caption is already set");
		}
		this.caption = caption;
	}

	/**
	 * Set content of the dialog. Content is filling inside the dialog.
	 * 
	 * @param content
	 */
	public void setContent(Widget content) {
		this.content = content;
		this.content.addStyleName(STYLE_CONTENT);
		this.fireEvent(new DialogInitializeEvent());
	}

	@Override
	public void show() {
		int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		int top = (Window.getClientHeight() - getOffsetHeight()) >> 1;
		setPopupPosition(Math.max(Window.getScrollLeft() + left, 0), Math.max(Window.getScrollTop() + top, 0));
		super.show();
	}

	@Override
	public void addDialogInitializeHandler(DialogInitializeHandler handler) {
		HandlerRegistration addedHandler = addHandler(handler, DialogInitializeEvent.getType());
		handlerRegistrations.add(addedHandler);
	}

	public void cleanup() {
		for (HandlerRegistration registration : handlerRegistrations) {
			registration.removeHandler();
		}
	}

	@Override
	public void center() {
		super.center();
		this.getElement().getStyle().setPosition(Position.FIXED);
	}

	public void centerAbsolute() {
		super.center();
	}

	@Override
	public void setPopupPosition(int left, int top) {
		super.setPopupPosition(left - WidgetUtils.getPageScrollX(), top - WidgetUtils.getPageScrollY());
		this.getElement().getStyle().setPosition(Position.FIXED);
	}

	@Override
	public void setAbsolutePopupPosition(int left, int top) {
		super.setPopupPosition(left, top);
		this.getElement().getStyle().setPosition(Position.ABSOLUTE);
	}
}
