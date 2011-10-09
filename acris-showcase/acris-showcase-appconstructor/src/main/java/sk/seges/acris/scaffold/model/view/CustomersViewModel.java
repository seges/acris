/**
 * 
 */
package sk.seges.acris.scaffold.model.view;

import java.util.List;

import sk.seges.acris.scaffold.annotation.Path;
import sk.seges.acris.scaffold.annotation.View;
import sk.seges.acris.scaffold.model.domain.CustomerModel;

/**
 * @author gazol
 */
@View
public interface CustomersViewModel extends ViewModel<List<CustomerModel>> {
	String surname();
	
	@Path("role.description")
	String description();
}
