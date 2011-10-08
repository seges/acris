package sk.seges.corpis.shared.domain.invoice.api;

public interface OrderItemData<O extends OrderData<?>> extends AccountableItemData {

	O getOrder();

	void setOrder(O order);
}