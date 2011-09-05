package sk.seges.acris.scaffold.model.view;

import sk.seges.acris.scaffold.annotation.Path;
import sk.seges.acris.scaffold.annotation.Selected;
import sk.seges.acris.scaffold.annotation.View;
import sk.seges.acris.scaffold.model.domain.CustomerModel;

/**
 * @author ladislav.gazo
 */
@View
public interface CustomerViewModel extends ViewModel<CustomerModel> {
	String firstName();

	@Selected
	@Path("role.name")
	String role();
}
