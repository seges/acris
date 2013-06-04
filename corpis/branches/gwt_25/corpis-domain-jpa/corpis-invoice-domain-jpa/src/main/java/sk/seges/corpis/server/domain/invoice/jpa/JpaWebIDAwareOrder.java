/**
 * 
 */
package sk.seges.corpis.server.domain.invoice.jpa;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.corpis.server.domain.invoice.server.model.data.LoyaltyCardData;
import sk.seges.corpis.server.domain.invoice.server.model.data.OrderData;
import sk.seges.corpis.server.domain.invoice.server.model.data.OrderItemData;
import sk.seges.corpis.shared.domain.customer.ECustomerDiscountType;

/**
 * @author eldzi
 */
@Entity
@Table(name = "webid_aware_order", uniqueConstraints = @UniqueConstraint(columnNames = { JpaWebIDAwareOrder.WEB_ID,JpaOrderBase.ORDER_ID }) )
@SequenceGenerator(name = JpaWebIDAwareOrder.SEQ_ORDERS, sequenceName = "seq_orders", initialValue = 1) 
public class JpaWebIDAwareOrder extends JpaOrderBase implements HasWebId, OrderData {
	private static final long serialVersionUID = 5948016788551732181L;
	
	protected static final String SEQ_ORDERS = "seqOrders";

	public static final String WEB_ID = "webId";

	private Long id;
	
	private String webId;

	private List<OrderItemData> orderItems;

	private LoyaltyCardData loyaltyCard;
	
	private ECustomerDiscountType orderType;
	
	private BigDecimal discountValue;

	@Override
	@Id
	@GeneratedValue(generator = SEQ_ORDERS)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order", targetEntity = JpaWebIDAwareOrderItem.class)
	public List<OrderItemData> getOrderItems() {
		return orderItems;
	}

	@Override
	public void setOrderItems(List<OrderItemData> orderItems) {
		this.orderItems = orderItems;
	}

	@ManyToOne(targetEntity = JpaLoyaltyCard.class)
	@JoinColumn(name = "loyalty_card_fk")
	public LoyaltyCardData getLoyaltyCard() {
		return loyaltyCard;
	}
	
	public void setLoyaltyCard(LoyaltyCardData loyaltyCard) {
		this.loyaltyCard = loyaltyCard;
	}
	
	@Column
	@Enumerated(EnumType.STRING)
	public ECustomerDiscountType getOrderType() {
		return orderType;
	}
	
	public void setOrderType(ECustomerDiscountType orderType) {
		this.orderType = orderType;
	}
	
	@Column
	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	
	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((webId == null) ? 0 : webId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JpaWebIDAwareOrder other = (JpaWebIDAwareOrder) obj;
		if (webId == null) {
			if (other.webId != null)
				return false;
		} else if (!webId.equals(other.webId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WebIDAwareOrder [id=" + id + ", webId=" + webId + ", toString()=" + super.toString() + "]";
	}
}
