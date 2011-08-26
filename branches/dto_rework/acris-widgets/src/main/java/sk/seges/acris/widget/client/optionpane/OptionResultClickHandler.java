/**
 * 
 */
package sk.seges.acris.widget.client.optionpane;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Click listener that sets option result to option pane.
 */
public class OptionResultClickHandler implements ClickHandler {
	private final EPanelResult optionResult;
	private final OptionResultHandler optionPane;
	private final OptionPaneResultListener resultListener;

	public OptionResultClickHandler(OptionResultHandler optionPane,
			EPanelResult optionResult,
			OptionPaneResultListener resultListener) {
		this.optionPane = optionPane;
		this.optionResult = optionResult;
		this.resultListener = resultListener;
	}

	public void onClick(ClickEvent event) {
		optionPane.setResult(optionResult);
		if (resultListener != null) {
			resultListener.onResultPrepared(optionResult);
		}
	}
}
