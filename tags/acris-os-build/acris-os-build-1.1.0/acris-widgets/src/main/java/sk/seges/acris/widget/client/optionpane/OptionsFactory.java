package sk.seges.acris.widget.client.optionpane;

import sk.seges.acris.widget.client.WidgetFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class OptionsFactory extends Composite {
    protected static ButtonLabels labels = GWT.create(ButtonLabels.class);

    private static Button createButton(OptionResultHandler optionsHandler, String label, EPanelResult result, OptionPaneResultListener resultListener) {
        Button button = GWT.create(Button.class);
        button.setText(label);
        button.addClickHandler(new OptionResultClickHandler(optionsHandler, result, resultListener));
        return button;
    }

    private static Button createOKButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.ok(), EPanelResult.OK_OPTION, resultListener);
    }

    private static Button createSaveButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.save(), EPanelResult.SAVE_OPTION, resultListener);
    }

    private static Button createSubmitButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.submit(), EPanelResult.SUBMIT_OPTION, resultListener);
    }

    private static Button createCancelButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.cancel(), EPanelResult.CANCEL_OPTION, resultListener);
    }

    private static Button createYesButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.yes(), EPanelResult.YES_OPTION, resultListener);
    }

    private static Button createNoButton(OptionResultHandler optionsHandler, OptionPaneResultListener resultListener) {
        return createButton(optionsHandler, labels.no(), EPanelResult.NO_OPTION, resultListener);
    }

    public static Widget[] createOptions(OptionResultHandler optionsHandler, EPanelOption option, OptionPaneResultListener resultListener) {
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
            return new Widget[] { createYesButton(optionsHandler, resultListener), createNoButton(optionsHandler, resultListener) };
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

    public static Widget[] createOKCancelOptions(ClickHandler okHandler) {
        return createOKCancelCustomizedOptions(labels.ok(), okHandler);
    }

    public static Widget[] createSaveCancelOptions(ClickHandler okHandler) {
        return createOKCancelCustomizedOptions(labels.save(), okHandler);
    }

    public static Widget createSaveOption(ClickHandler okHandler) {
        return createOKCustomizedOption(labels.save(), okHandler);
    }

    public static Widget[] createSubmitCancelOptions(ClickHandler okHandler) {
        return createOKCancelCustomizedOptions(labels.submit(), okHandler);
    }

    public static Widget[] createOKCancelCustomizedOptions(String okMessage, ClickHandler okHandler) {
        Button ok = WidgetFactory.button(okMessage, okHandler);
        Button cancel = WidgetFactory.button(labels.cancel());
        return new Widget[] { cancel, ok };
    }

    public static Widget[] createOKCancelCustomizedOptions(String okMessage, ClickHandler okHandler, ClickHandler cancelHandler) {
        Button ok = WidgetFactory.button(okMessage, okHandler);
        Button cancel = WidgetFactory.button(labels.cancel(), cancelHandler);
        return new Widget[] { cancel, ok };
    }

    public static Widget createOKCustomizedOption(String okMessage, ClickHandler okHandler) {
        return WidgetFactory.button(okMessage, okHandler);
    }

    public static Widget createCloseOption() {
        return WidgetFactory.button(labels.close());
    }

    public static Widget createCancelOption() {
        return WidgetFactory.button(labels.cancel());
    }

    public static Widget[] createSelectCancelOptions(ClickHandler okHandler) {
        return createOKCancelCustomizedOptions(labels.select(), okHandler);
    }
}
