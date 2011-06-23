package sk.seges.acris.widget.client.support;


public interface StyleSupport {

	public enum WidgetType {
		DIALOG
	}
	
	String[] getStylesForWidget(WidgetType componentType);
}
