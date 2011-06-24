/**
 *
 */
package sk.seges.acris.widget.client.optionpane;

import sk.seges.acris.widget.client.Cleaner;
import sk.seges.acris.widget.client.Dialog;
import sk.seges.acris.widget.client.Messages;
import sk.seges.acris.widget.client.factory.WidgetFactory;
import sk.seges.acris.widget.client.support.DefaultStyleSupport;
import sk.seges.acris.widget.client.support.StyleSupport;
import sk.seges.acris.widget.client.support.StyleSupport.WidgetType;
import sk.seges.acris.widget.client.util.StyleUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ladislav.gazo
 */
public class OptionPane extends FlowPanel implements OptionResultHandler {
	
	private static final String STYLE_OPTION_PANE = "acris-cmp-optionPane";
	private static final String STYLE_INPUT = "acris-cmp-input";
	private static final String STYLE_MESSAGE = "acris-cmp-message";
	private static final String STYLE_ICON = "acris-cmp-icon";

	private boolean initialized = false;

	private Image icon;
	private Widget message;
	private Widget input;

	private EPanelResult result;

	private OptionsFactory optionsFactory;
	private final WidgetFactory widgetFactory;
	
	protected static Icons icons = (Icons) GWT.create(Icons.class);
	protected static Labels labels = (Labels) GWT.create(Labels.class);
	protected static Messages messages = (Messages) GWT.create(Messages.class);

	public OptionPane(WidgetFactory widgetFactory) {
		this.widgetFactory = widgetFactory;
		this.optionsFactory = new OptionsFactory(widgetFactory);
		init();
	}
	
	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
		icon.addStyleName(STYLE_ICON);
	}

	public Widget getMessage() {
		return message;
	}

	public void setMessage(Widget message) {
		this.message = message;
		message.addStyleName(STYLE_MESSAGE);
	}

	public Widget getInput() {
		return input;
	}

	public void setInput(Widget input) {
		this.input = input;
		input.addStyleName(STYLE_INPUT);
	}

	public EPanelResult getResult() {
		return result;
	}

	public void setResult(EPanelResult result) {
		this.result = result;
	}

	private void init() {
		setStyleName(STYLE_OPTION_PANE);
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		if (!initialized) {
			boolean beCleaned = false;
			if (icon != null) {
				add(icon);
				beCleaned = true;
			}
			if (message != null) {
				add(message);
				beCleaned = true;
			}
			if (input != null) {
				add(input);
				beCleaned = true;
			}
			if (beCleaned) {
				add(new Cleaner());
			}
			initialized = true;
		}
	}

	protected static Image determineImage(EMessageType type) {
		if (EMessageType.INFORMATION_MESSAGE.equals(type)) {
			return icons.info().createImage();
		} else if (EMessageType.CONFIRM_MESSAGE.equals(type)) {
			return icons.question().createImage();
		} else if (EMessageType.QUESTION_MESSAGE.equals(type)) {
			return icons.question().createImage();
		} else if (EMessageType.ERROR_MESSAGE.equals(type)) {
			return icons.alert().createImage();
		} else if (EMessageType.PLAIN_MESSAGE.equals(type)) {
			return null;
		} else if (EMessageType.WARNING_MESSAGE.equals(type)) {
			return icons.warning().createImage();
		}
		throw new IllegalArgumentException("Unsupported message type " + type);
	}

	/**
	 * Brings up an information-message dialog titled with default message
	 * dialog title.
	 * 
	 * @param message
	 * @return
	 */
	public EPanelResult showMessageDialog(String message) {
		return showMessageDialog(message, labels.messageDialogTitle(), EMessageType.INFORMATION_MESSAGE);
	}

	public EPanelResult showMessageDialog(String message, StyleSupport styleSupport) {
		return showMessageDialog(message, labels.messageDialogTitle(), EMessageType.INFORMATION_MESSAGE, styleSupport);
	}

	/**
	 * Brings up an titled information-message dialog.
	 * 
	 * @param message
	 * @param title
	 * @return
	 */
	public EPanelResult showMessageDialog(String message, String title) {
		return showMessageDialog(message, title, EMessageType.INFORMATION_MESSAGE);
	}

	public EPanelResult showMessageDialog(String message, String title, StyleSupport styleSupport) {
		return showMessageDialog(message, title, EMessageType.INFORMATION_MESSAGE, styleSupport);
	}

	public EPanelResult showDefaultErrorDialog() {
		return showDefaultErrorDialog(null, new DefaultStyleSupport());
	}

	public EPanelResult showDefaultErrorDialog(StyleSupport styleSupport) {
		return showDefaultErrorDialog(null, styleSupport);
	}

	public EPanelResult showDefaultErrorDialog(Throwable e) {
		return showDefaultErrorDialog(e, new DefaultStyleSupport());
	}
	
	public EPanelResult showDefaultErrorDialog(Throwable e, StyleSupport styleSupport) {
		if (e == null) {
			return showErrorDialog(messages.optionPane_unhandledException(), labels.errorDialogTitle(), styleSupport);
		} else {
			return showStackTraceDialog(e, styleSupport);
		}
	}

	private EPanelResult showStackTraceDialog(Throwable e, StyleSupport styleSupport) {
		final Dialog dialog = widgetFactory.modalDialog();
		StyleUtils.applyWidgetStyles(styleSupport, WidgetType.DIALOG, dialog);
		dialog.setCaption(labels.errorDialogTitle());

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(new HTML(e.getMessage()));
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setHeight("200px");
		scrollPanel.add(new HTML(stackTraceToString(e)));
		verticalPanel.add(scrollPanel);
		TabPanel tabPanel = new TabPanel();
		tabPanel.add(new HTML(messages.optionPane_unhandledException()),
				messages.optionPane_unhandledExceptionTab1Title());
		tabPanel.add(verticalPanel, messages.optionPane_unhandledExceptionTab2Title());
		tabPanel.selectTab(0);

		OptionPane pane = new OptionPane(widgetFactory);
		pane.setMessage(tabPanel);
		pane.setIcon(determineImage(EMessageType.ERROR_MESSAGE));

		dialog.setContent(pane);

		dialog.addOptions(optionsFactory.createOptions(pane, EPanelOption.OK_OPTION, null));

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				dialog.center();
			}
		});
		
		return pane.getResult();
	}

	public static String stackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();

		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append("\n");
		}
		if (e.getCause() != null) {
			sb.append(stackTraceToString(e.getCause()));
		}
		return sb.toString();
	}

	public EPanelResult showErrorDialog(String message) {
		return showErrorDialog(message, labels.errorDialogTitle());
	}

	public EPanelResult showErrorDialog(String message, StyleSupport styleSupport) {
		return showErrorDialog(message, labels.errorDialogTitle(), styleSupport);
	}

	public EPanelResult showErrorDialog(String message, String title) {
		return showMessageDialog(message, title, EMessageType.ERROR_MESSAGE);
	}

	public EPanelResult showErrorDialog(String message, String title, StyleSupport styleSupport) {
		return showMessageDialog(message, title, EMessageType.ERROR_MESSAGE, styleSupport);
	}

	/**
	 * Brings up a dialog that displays a message using a default icon
	 * determined by the messageType parameter.
	 * 
	 * @param message
	 * @param title
	 * @param type
	 * @return
	 */
	public EPanelResult showMessageDialog(String message, String title, EMessageType type) {
		return showMessageDialog(message, title, type, new DefaultStyleSupport());
	}
	
	public EPanelResult showMessageDialog(String message, String title, EMessageType type, StyleSupport styleSupport) {

		final Dialog dialog = widgetFactory.modalDialog();
		StyleUtils.applyWidgetStyles(styleSupport, WidgetType.DIALOG, dialog);
		dialog.setCaption(title);

		OptionPane pane = createOptionPaneFromMessage(message, type);
		dialog.setContent(pane);

		dialog.addOptions(optionsFactory.createOptions(pane, EPanelOption.OK_OPTION, null));

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				dialog.center();
			}
		});
		
		return pane.getResult();
	}

	public OptionPane createOptionPaneFromMessage(String message, EMessageType type) {
		OptionPane pane = new OptionPane(widgetFactory);
		pane.setMessage(new HTML(message));
		pane.setIcon(determineImage(type));
		return pane;
	}

	public void showConfirmationDialog(String title, String message, EPanelOption options,
			OptionPaneResultListener resultListener) {
		OptionPane pane = createOptionPaneFromMessage(message, EMessageType.QUESTION_MESSAGE);
		Dialog dialog = createMessageDialog(title, pane, false, true, options, resultListener);

		dialog.center();
		dialog.getElement().getStyle().setTop((Window.getClientHeight() - dialog.getOffsetHeight()) / 2, Unit.PX);
	}

	/**
	 * Create message box (non-modal auto-hiding) with specified option pane.
	 * 
	 * @param pane
	 * @return
	 */
	public Dialog createMessageBox(OptionPane pane) {
		return createMessageBox(null, pane);
	}

	/**
	 * Create message box (non-modal auto-hiding) with specified option pane and
	 * title.
	 * 
	 * @param pane
	 * @param title
	 * @return
	 */
	public Dialog createMessageBox(String title, OptionPane pane) {
		return createMessageDialog(title, pane, true, false);
	}

	public Dialog createMessageDialog(String title, OptionPane pane, boolean autoHide, boolean modal) {
		return createMessageDialog(title, pane, autoHide, modal, EPanelOption.DEFAULT_OPTION);
	}

	public Dialog createMessageDialog(String title, OptionPane pane, boolean autoHide, boolean modal,
			EPanelOption messageOption) {
		return createMessageDialog(title, pane, autoHide, modal,
				optionsFactory.createOptions(pane, messageOption, null));
	}

	/**
	 * Creates message box where after option is selected a result listener will
	 * be triggered.
	 * 
	 * @param title
	 * @param pane
	 * @param autoHide
	 * @param modal
	 * @param messageOption
	 * @param resultListener
	 * @return
	 */
	public Dialog createMessageDialog(String title, OptionPane pane, boolean autoHide, boolean modal,
			EPanelOption messageOption, OptionPaneResultListener resultListener) {
		return createMessageDialog(title, pane, autoHide, modal,
				optionsFactory.createOptions(pane, messageOption, resultListener));
	}

	public Dialog createMessageDialog(String title, OptionPane pane, boolean autoHide, boolean modal,
			Widget options[]) {
		return createMessageDialog(title, pane, autoHide, modal, options, new DefaultStyleSupport());
	}
	
	public Dialog createMessageDialog(String title, OptionPane pane, boolean autoHide, boolean modal,
			Widget options[], StyleSupport styleSupport) {
		Dialog dialog = widgetFactory.dialog(autoHide, modal);
		StyleUtils.applyWidgetStyles(styleSupport, WidgetType.DIALOG, dialog);
		dialog.setCaption(title);
		dialog.setContent(pane);
		dialog.addOptions(options);
		return dialog;
	}

	public Dialog createMessageDialog(String title, String message, EMessageType type) {
		return createMessageDialog(title, message, type, false, true);
	}

	public Dialog createMessageDialog(String title, String message, EMessageType type, boolean autoHide,
			boolean modal) {
		OptionPane pane = createOptionPaneFromMessage(message, type);
		return createMessageDialog(title, pane, autoHide, modal);
	}

	public Dialog createErrorDialog(String message, String title) {
		return createMessageDialog(title, message, EMessageType.ERROR_MESSAGE);
	}
}