package sk.seges.acris.widget.client.support;


public class DefaultStyleSupport implements StyleSupport {

	@Override
	public String[] getStylesForWidget(WidgetType componentType) {
		switch (componentType) {
		case DIALOG:
			return new String[] {"acris-cmp-dialog"};
		default:
			break;
		}
		return null;
	}


}