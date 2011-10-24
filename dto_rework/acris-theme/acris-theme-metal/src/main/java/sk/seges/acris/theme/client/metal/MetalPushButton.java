package sk.seges.acris.theme.client.metal;

import sk.seges.acris.theme.client.annotation.Theme;
import sk.seges.acris.theme.client.annotation.ThemeSupport;

import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.PushButton;

@Theme(MetalTheme.NAME)
@ThemeSupport(widgetClass = PushButton.class, elementName = "button", template = @UiTemplate("MetalButton.ui.xml"))
public interface MetalPushButton {}