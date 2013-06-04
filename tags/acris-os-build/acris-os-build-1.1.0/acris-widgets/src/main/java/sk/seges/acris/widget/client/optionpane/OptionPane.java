/**
 *
 */
package sk.seges.acris.widget.client.optionpane;

import sk.seges.acris.widget.client.Cleaner;
import sk.seges.acris.widget.client.Dialog;
import sk.seges.acris.widget.client.Messages;
import sk.seges.acris.widget.client.WidgetFactory;

import com.google.gwt.core.client.GWT;
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

	protected static Icons icons = (Icons) GWT.create(Icons.class);
	protected static Labels labels = (Labels) GWT.create(Labels.class);
	protected static Messages messages = (Messages) GWT.create(Messages.class);

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

	public OptionPane() {
		init();
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
	public static EPanelResult showMessageDialog(String message) {
		return showMessageDialog(message, labels.messageDialogTitle(), EMessageType.INFORMATION_MESSAGE);
	}

	/**
	 * Brings up an titled information-message dialog.
	 * 
	 * @param message
	 * @param title
	 * @return
	 */
	public static EPanelResult showMessageDialog(String message, String title) {
		return showMessageDialog(message, title, EMessageType.INFORMATION_MESSAGE);
	}

	public static EPanelResult showDefaultErrorDialog() {
		return showDefaultErrorDialog(null);
	}
	
	

	public static EPanelResult showDefaultErrorDialog(Throwable e) {
		if (e == null) {
			return showErrorDialog(messages.optionPane_unhandledException(),
					labels.errorDialogTitle());
		} else {
			return showStackTraceDialog(e);
		}
	}
	
	private static EPanelResult showStackTraceDialog(Throwable e) {
		Dialog dialog = WidgetFactory.modalDialog();
		dialog.setCaption(labels.errorDialogTitle());

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(new HTML(e.getMessage()));
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setHeight("200px");
		scrollPanel.add(new HTML(stackTraceToString(e)));
		verticalPanel.add(scrollPanel);
		TabPanel tabPanel = new TabPanel();
		tabPanel.add(new HTML(messages.optionPane_unhandledException()), messages.optionPane_unhandledExceptionTab1Title());
		tabPanel.add(verticalPanel, messages.optionPane_unhandledExceptionTab2Title());
		tabPanel.selectTab(0);
		
		OptionPane pane = new OptionPane();
		pane.setMessage(tabPanel);
		pane.setIcon(determineImage(EMessageType.ERROR_MESSAGE));
		
		dialog.setContent(pane);

		dialog.addOptions(OptionsFactory.createOptions(pane, EPanelOption.OK_OPTION, null));

		dialog.center();
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

	public static EPanelResult showErrorDialog(String message) {
		return showErrorDialog(message, labels.errorDialogTitle());
	}

	public static EPanelResult showErrorDialog(String message, String title) {
		return showMessageDialog(message, title, EMessageType.ERROR_MESSAGE);
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
	public static EPanelResult showMessageDialog(String message, String title, EMessageType type) {
		/*
		 * final ContentDialogBox dialog = new ContentDialogBox();
		 * dialog.setTitle(title);
		 * 
		 * OptionPane pane = new OptionPane(); pane.setMessage(new
		 * Label(message)); pane.setIcon(determineImage(type));
		 * 
		 * pane.onLoad();
		 * 
		 * Button ok = new Button(); ok.setText(labels.ok());
		 * ok.addClickListener(new OptionResultClickListener(pane,
		 * EMessageOptionResult.OK_OPTION) { public void onClick(Widget arg0) {
		 * super.onClick(arg0); dialog.hide(); } }); pane.add(ok);
		 * 
		 * dialog.add(pane); dialog.center(); return pane.getResult(); /
		 */
		Dialog dialog = WidgetFactory.modalDialog();
		dialog.setCaption(title);

		OptionPane pane = createOptionPaneFromMessage(message, type);
		dialog.setContent(pane);

		dialog.addOptions(OptionsFactory.createOptions(pane, EPanelOption.OK_OPTION, null));

		dialog.center();
		return pane.getResult();
		/**/
	}

	public static OptionPane createOptionPaneFromMessage(String message, EMessageType type) {
		OptionPane pane = new OptionPane();
		pane.setMessage(new HTML(message));
		pane.setIcon(determineImage(type));
		return pane;
	}

	public static void showConfirmationDialog(String title, String message, EPanelOption options,
			OptionPaneResultListener resultListener) {
		OptionPane pane = createOptionPaneFromMessage(message, EMessageType.QUESTION_MESSAGE);
		Dialog d = createMessageDialog(title, pane, false, true, options, resultListener);
		d.center();
	}

	/**
	 * Create message box (non-modal auto-hiding) with specified option pane.
	 * 
	 * @param pane
	 * @return
	 */
	public static Dialog createMessageBox(OptionPane pane) {
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
	public static Dialog createMessageBox(String title, OptionPane pane) {
		return createMessageDialog(title, pane, true, false);
	}

	public static Dialog createMessageDialog(String title, OptionPane pane, boolean autoHide, boolean modal) {
		return createMessageDialog(title, pane, autoHide, modal, EPanelOption.DEFAULT_OPTION);
	}

	public static Dialog createMessageDialog(String title, OptionPane pane, boolean autoHide, boolean modal,
			EPanelOption messageOption) {
		return createMessageDialog(title, pane, autoHide, modal,
				OptionsFactory.createOptions(pane, messageOption, null));
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
	public static Dialog createMessageDialog(String title, OptionPane pane, boolean autoHide, boolean modal,
			EPanelOption messageOption, OptionPaneResultListener resultListener) {
		return createMessageDialog(title, pane, autoHide, modal,
				OptionsFactory.createOptions(pane, messageOption, resultListener));
	}

	public static Dialog createMessageDialog(String title, OptionPane pane, boolean autoHide, boolean modal,
			Widget options[]) {
		Dialog dialog = WidgetFactory.dialog(autoHide, modal);
		dialog.setCaption(title);
		dialog.setContent(pane);
		dialog.addOptions(options);
		return dialog;
	}

	public static Dialog createMessageDialog(String title, String message, EMessageType type) {
		return createMessageDialog(title, message, type, false, true);
	}

	public static Dialog createMessageDialog(String title, String message, EMessageType type, boolean autoHide,
			boolean modal) {
		OptionPane pane = createOptionPaneFromMessage(message, type);
		return createMessageDialog(title, pane, autoHide, modal);
	}

	public static Dialog createErrorDialog(String message, String title) {
		return createMessageDialog(title, message, EMessageType.ERROR_MESSAGE);
	}
}
