package sk.seges.acris.widget.client.util;

import com.google.gwt.user.client.ui.UIObject;

import sk.seges.acris.widget.client.support.StyleSupport;
import sk.seges.acris.widget.client.support.StyleSupport.WidgetType;

public class StyleUtils {

	public static <T extends UIObject> T applyWidgetStyles(StyleSupport styleSupport, WidgetType widgetType, T widget) {
		if (styleSupport == null || widget == null) {
			return widget;
		}
		
		String[] stylesForWidget = styleSupport.getStylesForWidget(widgetType);
		if (stylesForWidget == null) {
			return widget;
		}
		
		for (String styleForWidget: stylesForWidget) {
			widget.addStyleName(styleForWidget);
		}
		
		return widget;
	}
}
