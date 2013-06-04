/**
 * 
 */
package sk.seges.acris.widget.client.optionpane;


import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * Click listener that sets option result to option pane.
 * 
 * @author ladislav.gazo
 */
@Deprecated
public class OptionResultClickListener implements ClickListener {
	private final EPanelResult optionResult;
	private final OptionResultHandler optionPane;
	private final OptionPaneResultListener resultListener;
	
	public OptionResultClickListener(OptionResultHandler optionPane,
			EPanelResult optionResult, OptionPaneResultListener resultListener) {
		this.optionPane = optionPane;
		this.optionResult = optionResult;
		this.resultListener = resultListener;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
	 */
	public void onClick(Widget arg0) {
		optionPane.setResult(optionResult);
		if(resultListener != null) {
			resultListener.onResultPrepared(optionResult);
		}
	}
}
