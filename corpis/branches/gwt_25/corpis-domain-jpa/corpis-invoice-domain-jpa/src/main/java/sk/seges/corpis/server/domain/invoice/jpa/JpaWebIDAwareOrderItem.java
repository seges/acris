/**
 * 
 */
package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.sesam.domain.IDomainObject;

/**
 * @author eldzi
 */
@Entity
@Table(name = "webid_aware_order_items")
@SequenceGenerator(name = JpaWebIDAwareOrderItem.SEQ_ORDER_ITEMS, sequenceName = "seq_orders", initialValue = 1)
@Inheritance(strategy = InheritanceType.JOINED)
public class JpaWebIDAwareOrderItem extends JpaOrderItemBase<JpaWebIDAwareOrder> implements IDomainObject<Long> {
	private static final long serialVersionUID = 3399448840385713282L;
	
	protected static final String SEQ_ORDER_ITEMS = "seqOrderItems";
	
	@Id
	@GeneratedValue(generator = SEQ_ORDER_ITEMS)
	private Long id;
	
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", toString()=" + super.toString() + "]";
	}
}
