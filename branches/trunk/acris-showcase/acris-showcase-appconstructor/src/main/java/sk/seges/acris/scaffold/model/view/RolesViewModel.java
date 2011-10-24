/**
 * 
 */
package sk.seges.acris.scaffold.model.view;

import java.util.List;

import sk.seges.acris.scaffold.annotation.View;
import sk.seges.acris.scaffold.model.domain.RoleModel;

@View
public interface RolesViewModel extends ViewModel<List<RoleModel>> {
	// no fields specified means all fields will be used from RoleModel to build the table
}
