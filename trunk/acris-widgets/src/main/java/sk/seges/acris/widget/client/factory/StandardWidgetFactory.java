package sk.seges.acris.widget.client.factory;

import sk.seges.acris.widget.client.Dialog;

public class StandardWidgetFactory extends WidgetFactory {

	private static StandardWidgetFactory instance;
	
	public StandardWidgetFactory() {
		super(new StandardWidgetProvider());
	}

	public static StandardWidgetFactory get() {
		if (instance == null) {
			instance = new StandardWidgetFactory();
		}
		
		return instance;
	}
	
	@Override
	public Dialog dialog() {
		Dialog dialog = super.dialog();
		dialog.addStyleName("acris-cmp-dialog");
		return dialog;
	}
}
