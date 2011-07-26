/**
 *  Copyright 2011 Seges s.r.o.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package sk.seges.acris.theme.client.shadow;

import sk.seges.acris.theme.client.annotation.Theme;
import sk.seges.acris.theme.client.annotation.ThemeResources;
import sk.seges.acris.theme.client.annotation.ThemeResources.ThemeResource;
import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.theme.client.shadow.ShadowTheme;
import sk.seges.acris.theme.client.shadow.resources.ShadowThemeResources;
import sk.seges.acris.widget.client.form.ImageCheckBox;

import com.google.gwt.user.client.ui.CheckBox;

/**
 * Styled {@link CheckBox} with shadow theme. This is not the standard system based check box because it does not allows
 * you to define own border. Shadow components should have a border so we are using {@link ImageCheckBox} instead of
 * {@link CheckBox}
 * 
 * @author Peter Simun (simun@seges.sk)
 */
@Theme(ShadowTheme.NAME)
@ThemeSupport(widgetClass = ImageCheckBox.class, elementName = "checkBox")
@ThemeResources(@ThemeResource(resourceClass = ShadowThemeResources.class))
public interface ShadowCheckBox {}