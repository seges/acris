package sk.seges.acris.theme.client.shadow;

import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.theme.client.annotation.ThemeSupport.Resource;
import sk.seges.acris.theme.client.shadow.resources.ShadowThemeResources;
import sk.seges.acris.widget.client.form.ImageCheckBox;

@ThemeSupport(
		widgetClass = ImageCheckBox.class, elementName = "checkBox", themeName = ShadowTheme.NAME, 
		resources = @Resource(resourceClass = ShadowThemeResources.class)
)
public interface ShadowCheckBox {} 