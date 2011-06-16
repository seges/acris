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

import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.widget.client.form.select.ComboBox;

import com.google.gwt.user.client.ui.ListBox;

/**
 * Styled {@link ComboBox} with shadow theme. This is not the standard system based {@link ListBox} because it does not
 * allows you to define custom drop down arrow. Shadow components should have a custom drop down arrow so we are using
 * {@link ComboBox} instead of {@link ListBox}
 * 
 * @author Peter Simun (simun@seges.sk)
 */
@ThemeSupport(widgetClass = ComboBox.class, elementName = "comboBox", themeName = ShadowTheme.NAME)
public class ShadowComboBox {}