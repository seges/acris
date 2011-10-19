package sk.seges.acris.widget.client.optionpane;

import sk.seges.acris.widget.client.factory.StandardWidgetFactory;

public class StandardOptionsFactory extends OptionsFactory {

	private static StandardOptionsFactory instance;
	
	public StandardOptionsFactory() {
		super(new StandardWidgetFactory());
	}
	
	public static StandardOptionsFactory get() {
		if (instance == null) {
			instance = new StandardOptionsFactory();
		}
		
		return instance;
	}
}
