package sk.seges.acris.scaffold.model.domain;

import java.util.Date;
import java.util.List;

/**
 * @author ladislav.gazo
 *
 */
public interface OrderModel extends DomainModel {
	Long id();
	String orderId();
	Date valid();
	
	CustomerModel customer();
	List<OrderItemModel> orderItems();
	
}
