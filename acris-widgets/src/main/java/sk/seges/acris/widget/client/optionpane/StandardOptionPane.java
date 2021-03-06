package sk.seges.acris.widget.client.optionpane;

import sk.seges.acris.widget.client.Dialog;
import sk.seges.acris.widget.client.factory.StandardWidgetFactory;

public class StandardOptionPane extends OptionPane {

	private static StandardOptionPane instance;
	
	public StandardOptionPane() {
		super(new StandardWidgetFactory());
	}

	public static StandardOptionPane get() {
		if (instance == null) {
			instance = new StandardOptionPane();
		}
		
		return instance;
	}
	
	@Override
	public void addAditionalStyles(Dialog dialog) {
		dialog.addStyleName("site-option-pane");
	}
}
