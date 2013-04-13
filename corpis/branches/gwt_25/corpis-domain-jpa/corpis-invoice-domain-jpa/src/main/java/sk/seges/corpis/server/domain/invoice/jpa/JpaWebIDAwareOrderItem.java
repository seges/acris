/**
 * 
 */
package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.server.model.data.OrderData;
import sk.seges.sesam.domain.IDomainObject;

/**
 * @author eldzi
 */
@Entity
@Table(name = "webid_aware_order_item")
@SequenceGenerator(name = JpaWebIDAwareOrderItem.SEQ_ORDER_ITEMS, sequenceName = "seq_orders", initialValue = 1)
@Inheritance(strategy = InheritanceType.JOINED)
public class JpaWebIDAwareOrderItem extends JpaOrderItemBase implements IDomainObject<Long> {
	private static final long serialVersionUID = 3399448840385713282L;
	
	protected static final String SEQ_ORDER_ITEMS = "seqOrderItems";
	
	@Override
	@Id
	@GeneratedValue(generator = SEQ_ORDER_ITEMS)
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = JpaWebIDAwareOrder.class)
	@JoinColumn(name = "orders_id")
	public OrderData getOrder() {
		return super.getOrder();
	}	
}