package sk.seges.acris.scaffold.model.view.compose;

import sk.seges.acris.scaffold.model.domain.CustomerModel;
import sk.seges.acris.scaffold.model.domain.OrderModel;
import sk.seges.acris.scaffold.model.domain.OrganizationUnitModel;

/**
 * @author ladislav.gazo
 */
@ViewComposer
public interface OrderSelectorAlternative {
	
	@Multiselect
	interface View extends OrderModel {
		
		@Disjunction
		interface FilterBy {
			OrganizationUnitModel orgUnit();
			CustomerModel customer();
		}
		
	}
	
}
