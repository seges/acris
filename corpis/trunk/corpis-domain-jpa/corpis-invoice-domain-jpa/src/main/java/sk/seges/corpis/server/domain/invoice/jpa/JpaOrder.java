package sk.seges.corpis.server.domain.invoice.jpa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.server.domain.invoice.server.model.data.OrderData;
import sk.seges.corpis.server.domain.invoice.server.model.data.OrderItemData;

@Entity
@SequenceGenerator(name = JpaOrder.SEQ_ORDERS, sequenceName = "seq_orders", initialValue = 1)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "orders", uniqueConstraints = {@UniqueConstraint(columnNames = JpaOrderBase.ORDER_ID)})
public class JpaOrder extends JpaOrderBase {
	private static final long serialVersionUID = -3117593133828636987L;

	protected static final String SEQ_ORDERS = "seqOrders";
	
	private Long id;

	private List<OrderItemData<OrderData>> orderItems;

	@Override
	@Id
	@GeneratedValue(generator = SEQ_ORDERS)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = JpaOrderItem.class)
	@JoinColumn(name = "orders_id")
	public List<OrderItemData<OrderData>> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List orderItems) {
		this.orderItems = orderItems;
	}
}