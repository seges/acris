package sk.seges.corpis.server.domain.transportation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Id;

import sk.seges.corpis.server.domain.invoice.jpa.JpaOrderItem;
import sk.seges.corpis.server.domain.invoice.server.model.data.AccountableItemData;

@Entity
@Table(name = "order_item_transportation_order")
@SequenceGenerator(name = JpaOrderItemTransportationOrder.SEQ_ORDER_ITEM_TRANSPORTATION_ORDER, sequenceName = "seq_order_item_transportation_order", initialValue = 1)
@SuppressWarnings("serial")
public class JpaOrderItemTransportationOrder extends JpaTransportationOrderBase  {

	protected static final String SEQ_ORDER_ITEM_TRANSPORTATION_ORDER = "seqOrderItemTransporationOrder";
	
	private AccountableItemData accountableItem;
	private Long id;
	
	@Override
	@Id
	@GeneratedValue(generator = SEQ_ORDER_ITEM_TRANSPORTATION_ORDER)
	public Long getId() {
		return id;
	}
	
	@ManyToOne(targetEntity = JpaOrderItem.class)
	public AccountableItemData getAccountableItem() {
		return accountableItem;
	}
	
	public void setAccountableItem(AccountableItemData accountableItem) {
		this.accountableItem = accountableItem;
	}
}
