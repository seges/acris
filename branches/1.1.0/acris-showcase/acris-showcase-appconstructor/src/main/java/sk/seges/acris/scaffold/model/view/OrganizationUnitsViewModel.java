/**
 * 
 */
package sk.seges.acris.scaffold.model.view;

import sk.seges.acris.scaffold.annotation.Additional;
import sk.seges.acris.scaffold.annotation.Hint;
import sk.seges.acris.scaffold.annotation.View;
import sk.seges.acris.scaffold.hint.view.TreeHint;
import sk.seges.acris.scaffold.model.domain.OrganizationUnitModel;

@View
@Hint(hints = {TreeHint.class}) // based on the number of methods it can transform to a tree or tree table component, or custom tree-like structure (nested lists,...)
public interface OrganizationUnitsViewModel extends ViewModel<OrganizationUnitModel> {
	// by default all fields are localized - resource bundle is used with unique
	// prefix determining the translation for this field with description to a
	// person which translates this (this is supported in more advanced
	// translation formats, but in case of properties files a comment describing
	// the path to it can be used)
	String name();
	
	@Additional // transforms to a tooltip
	String costCenter();
}
