package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.server.model.data.DeliveryData;
import sk.seges.corpis.server.domain.invoice.server.model.data.DeliveryOrderItemData;

@Entity
@Table(name = "delivery_order_item")
public class JpaDeliveryOrderItem extends JpaWebIDAwareOrderItem implements DeliveryOrderItemData {

	private static final long serialVersionUID = 5996576540848785299L;
	
	private DeliveryData delivery;
	
	@Override
	@ManyToOne(targetEntity = JpaDelivery.class)
	public DeliveryData getDelivery() {
		return delivery;
	}
	
	public void setDelivery(DeliveryData delivery) {
		this.delivery = delivery;
	}

}
