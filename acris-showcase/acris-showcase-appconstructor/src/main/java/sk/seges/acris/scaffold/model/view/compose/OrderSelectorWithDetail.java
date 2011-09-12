package sk.seges.acris.scaffold.model.view.compose;

import sk.seges.acris.scaffold.model.domain.OrderModel;
import sk.seges.acris.scaffold.model.view.CustomerDisplayName;
import sk.seges.acris.scaffold.model.view.CustomerViewModel;

@ViewComposer
public interface OrderSelectorWithDetail extends OrderSelectorAlternative {
	
	@SelectedDetail(of = View.class)
	interface Detail extends OrderModel {
		@Validate(OrderIdValidator.class)
		String orderId();
		
		@Editable(editor = CustomerViewModel.class)
		CustomerDisplayName customerDisplayName();
	}
}
