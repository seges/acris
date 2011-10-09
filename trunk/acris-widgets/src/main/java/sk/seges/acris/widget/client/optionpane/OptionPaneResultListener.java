/**
 * 
 */
package sk.seges.acris.widget.client.optionpane;


/**
 * @author ladislav.gazo
 */
public interface OptionPaneResultListener {
	/**
	 * Called when an option in the option pane dialog was selected and
	 * appropriate result is returned.
	 * 
	 * @param result
	 */
	void onResultPrepared(EPanelResult result);
}
