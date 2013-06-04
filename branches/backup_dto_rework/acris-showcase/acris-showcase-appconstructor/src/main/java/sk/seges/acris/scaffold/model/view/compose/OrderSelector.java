package sk.seges.acris.scaffold.model.view.compose;

import sk.seges.acris.scaffold.model.domain.CustomerModel;
import sk.seges.acris.scaffold.model.domain.OrderModel;
import sk.seges.acris.scaffold.model.domain.OrganizationUnitModel;

/**
 * Desired look:
 * 
 * On top a TabPanel with two tabs - tree of organization units and table of
 * customers.
 * 
 * A "filter me" button which takes currently selected value in the currently
 * selected tab (e.g. take a pre-determined key in view-context) and call the
 * service.
 * 
 * Result of the call is filled into a table of orders where I can multiselect
 * (Add/Add all/Remove/Remove all) to the final table.
 * 
 * The logic will take panels corresponding to class names unless overriden with
 * custom mapping in the selector.<br/>
 * Example situation: OrderModel, because it is multiselect, will use
 * OrdersViewModel
 */
public class OrderSelector extends Composer {
	// TODO: think about providing custom mapping, e.g. in the form filterBy =>
	// transforms to a Button and TabPanel with all views

	public void compose() {
		multiselect(OrderModel.class).filterBy().or()
				.add(OrganizationUnitModel.class).add(CustomerModel.class);
	}
}
