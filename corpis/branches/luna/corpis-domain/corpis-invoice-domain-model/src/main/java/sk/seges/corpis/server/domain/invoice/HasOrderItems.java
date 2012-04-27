package sk.seges.corpis.server.domain.invoice;

import java.util.List;

import sk.seges.corpis.server.domain.invoice.server.model.data.OrderData;
import sk.seges.corpis.server.domain.invoice.server.model.data.OrderItemData;

public interface HasOrderItems<O extends OrderData> {

	List<OrderItemData<O>> getOrderItems();

	void setOrderItems(List<OrderItemData<O>> orderItems);
}