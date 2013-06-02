/**
 * 
 */
package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.server.model.data.OrderData;
import sk.seges.sesam.domain.IDomainObject;

/**
 * @author eldzi
 */
@Entity
@Table(name = "webid_aware_order_item")
@Inheritance(strategy = InheritanceType.JOINED)
public class JpaWebIDAwareOrderItem extends JpaOrderItem implements IDomainObject<Long> {
	private static final long serialVersionUID = 3399448840385713282L;
	
	@Override
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = JpaWebIDAwareOrder.class)
	@JoinColumn(name = "orders_id")
	public OrderData getOrder() {
		return super.getOrder();
	}	
}