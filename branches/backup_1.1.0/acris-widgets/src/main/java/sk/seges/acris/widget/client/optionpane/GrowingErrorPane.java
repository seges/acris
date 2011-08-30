/**
 * 
 */
package sk.seges.acris.widget.client.optionpane;

import sk.seges.acris.widget.client.factory.WidgetFactory;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * @author ladislav.gazo
 */
public class GrowingErrorPane extends OptionPane {
	private static final String STYLE_ERROR = "acris-cmp-error";
	private static final String STYLE_ERROR_STACK = "acris-cmp-errorStack";
	private static final String STYLE_ERROR_PANE = "acris-cmp-errorPane";
	
	private boolean initialized = false;
	
	private FlowPanel errorStack;
	
	public GrowingErrorPane(WidgetFactory widgetFactory) {
		super(widgetFactory);
		addStyleName(STYLE_ERROR_PANE);
		
		errorStack = new FlowPanel();
		errorStack.addStyleName(STYLE_ERROR_STACK);
	}
	
	@Override
	protected void onLoad() {
		if(!initialized) {
			setMessage(errorStack);
			setIcon(determineImage(EMessageType.ERROR_MESSAGE));
			super.onLoad();
			initialized = true;
		}
	}
	
	public void addMessage(String message) {
		Label error = new Label(message);
		error.setStyleName(STYLE_ERROR);
		errorStack.add(error);
	}
	
	public void clearMessages() {
		errorStack.clear();
	}
}
