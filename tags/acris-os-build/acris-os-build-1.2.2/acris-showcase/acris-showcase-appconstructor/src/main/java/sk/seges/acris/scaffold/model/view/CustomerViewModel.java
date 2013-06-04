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

	@Selected // transforms usually to combo box and says that it should show selected (or current) value
	@Path("role.name") // TODO: find a way to put there a constant, not a string - it should be connected to model somehow
	String role();
}
