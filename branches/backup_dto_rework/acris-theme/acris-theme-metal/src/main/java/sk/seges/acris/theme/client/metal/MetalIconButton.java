package sk.seges.acris.theme.client.metal;

import sk.seges.acris.theme.client.annotation.Theme;
import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.widget.client.form.IconButton;

import com.google.gwt.uibinder.client.UiTemplate;

@Theme(MetalTheme.NAME)
@ThemeSupport(widgetClass = IconButton.class, elementName = "button", template = @UiTemplate("MetalButton.ui.xml"))
public interface MetalIconButton {}