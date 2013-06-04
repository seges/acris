package sk.seges.acris.widget.client.optionpane;

import sk.seges.acris.widget.client.factory.WidgetFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class OptionsFactory extends Composite {
    
	protected static ButtonLabels labels = GWT.create(ButtonLabels.class);

	protected WidgetFactory widgetFactory;
	
	public OptionsFactory(WidgetFactory widgetFactory) {
		this.widgetFactory = widgetFactory;
	}

	protected Button createButton(OptionResultHandler optionsHandler, String label, EPanelResult result, OptionPaneResultListener resultListener) {
        Button button = GWT.create(Button.class);
        button.setText(label);
        button.addClickHandler(new OptionResultClickHandler(optionsHandler, result, resultListener));
        return button;
    }

    private Button createOKButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.ok(), EPanelResult.OK_OPTION, resultListener);
    }

    private Button createSaveButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.save(), EPanelResult.SAVE_OPTION, resultListener);
    }

    private Button createSubmitButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.submit(), EPanelResult.SUBMIT_OPTION, resultListener);
    }

    private Button createCancelButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.cancel(), EPanelResult.CANCEL_OPTION, resultListener);
    }

    protected Widget createYesButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.yes(), EPanelResult.YES_OPTION, resultListener);
    }

    protected Button createNoButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.no(), EPanelResult.NO_OPTION, resultListener);
    }

    public Widget[] createOptions(OptionResultHandler optionsHandler, EPanelOption option, OptionPaneResultListener resultListener) {
        if (EPanelOption.DEFAULT_OPTION.equals(option)) {
            return new Widget[] { createOKButton(optionsHandler, resultListener) };
        } else if (EPanelOption.OK_OPTION.equals(option)) {
            return new Widget[] { createOKButton(optionsHandler, resultListener) };
        } else if (EPanelOption.OK_CANCEL_OPTION.equals(option)) {
            return new Widget[] { createOKButton(optionsHandler, resultListener), createCancelButton(optionsHandler, resultListener) };
        } else if (EPanelOption.YES_NO_CANCEL_OPTION.equals(option)) {
            return new Widget[] { createYesButton(optionsHandler, resultListener), createNoButton(optionsHandler, resultListener),
                    createCancelButton(optionsHandler, resultListener) };
        } else if (EPanelOption.YES_NO_OPTION.equals(option)) {
            return new Widget[] { createNoButton(optionsHandler, resultListener), createYesButton(optionsHandler, resultListener) };
        } else if (EPanelOption.CANCEL_SUBMIT_OPTION.equals(option)) {
            return new Widget[] { createSubmitButton(optionsHandler, resultListener), createCancelButton(optionsHandler, resultListener) };
        } else if (EPanelOption.SUBMIT_OPTION.equals(option)) {
            return new Widget[] { createSubmitButton(optionsHandler, resultListener) };
        } else if (EPanelOption.CANCEL_SAVE_OPTION.equals(option)) {
            return new Widget[] { createSaveButton(optionsHandler, resultListener), createCancelButton(optionsHandler, resultListener) };
        } else if (EPanelOption.SAVE_OPTION.equals(option)) {
            return new Widget[] { createSaveButton(optionsHandler, resultListener) };
        }
        throw new IllegalArgumentException("Unsupported option " + option);
    }

    public Widget[] createOKCancelOptions(ClickHandler okHandler) {
        return createOKCancelCustomizedOptions(labels.ok(), okHandler);
    }

    public Widget[] createOKCancelOptions(ClickHandler okHandler, ClickHandler cancelHandler) {
        return createOKCancelCustomizedOptions(labels.ok(), okHandler, cancelHandler);
    }

    public Widget[] createSaveCancelOptions(ClickHandler okHandler) {
        return createOKCancelCustomizedOptions(labels.save(), okHandler);
    }

    public Widget[] createSaveCancelOptions(ClickHandler okHandler, ClickHandler cancelHandler) {
        return createOKCancelCustomizedOptions(labels.save(), okHandler, cancelHandler);
    }

    public Widget createSaveOption(ClickHandler okHandler) {
        return createOKCustomizedOption(labels.save(), okHandler);
    }

    public Widget[] createSubmitCancelOptions(ClickHandler okHandler) {
        return createOKCancelCustomizedOptions(labels.submit(), okHandler);
    }

    public Widget[] createSubmitCancelOptions(ClickHandler okHandler, ClickHandler cancelHandler) {
        return createOKCancelCustomizedOptions(labels.submit(), okHandler, cancelHandler);
    }

    public Widget[] createOKCancelCustomizedOptions(String okMessage, ClickHandler okHandler) {
        Button ok = widgetFactory.button(okMessage, okHandler);
        Button cancel = widgetFactory.button(labels.cancel());
        return new Widget[] { cancel, ok };
    }

    public Widget[] createOKCancelCustomizedOptions(String okMessage, ClickHandler okHandler, ClickHandler cancelHandler) {
        Button ok = widgetFactory.button(okMessage, okHandler);
        Button cancel = widgetFactory.button(labels.cancel(), cancelHandler);
        return new Widget[] { cancel, ok };
    }

    public Widget createOKCustomizedOption(String okMessage, ClickHandler okHandler) {
        return widgetFactory.button(okMessage, okHandler);
    }

    public Widget createCloseOption() {
        return widgetFactory.button(labels.close());
    }

    public Widget createCancelOption() {
        return widgetFactory.button(labels.cancel());
    }

    public Widget[] createSelectCancelOptions(ClickHandler okHandler) {
        return createOKCancelCustomizedOptions(labels.select(), okHandler);
    }
    
    public Widget createOKOption(ClickHandler okHandler) {
        return widgetFactory.button(labels.ok(), okHandler);
    }
    
    public Widget createSelectOption(ClickHandler okHandler) {
    	return createOKCustomizedOption(labels.select(), okHandler);
    }
}
