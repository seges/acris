package sk.seges.corpis.shared.domain.invoice.api;

import java.util.List;

public interface HasOrderItems<O extends OrderData<?>> {

	List<OrderItemData<O>> getOrderItems();

	void setOrderItems(List<OrderItemData<O>> orderItems);
}