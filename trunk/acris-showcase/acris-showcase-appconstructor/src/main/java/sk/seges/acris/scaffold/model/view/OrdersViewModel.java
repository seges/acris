/**
 * 
 */
package sk.seges.acris.scaffold.model.view;

import java.util.List;

import sk.seges.acris.scaffold.annotation.View;
import sk.seges.acris.scaffold.model.domain.OrderModel;

@View
public interface OrdersViewModel extends ViewModel<List<OrderModel>> {
	String orderId();

	// view model will pass a model object (or its parts) to FieldComposer to create the result
	CustomerDisplayName customerDisplayName();
}
