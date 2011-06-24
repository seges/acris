package sk.seges.acris.widget.client.factory;

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
}
