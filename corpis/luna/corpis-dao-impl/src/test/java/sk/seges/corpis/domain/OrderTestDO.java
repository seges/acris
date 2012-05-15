package sk.seges.corpis.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import sk.seges.sesam.domain.IDomainObject;

@Entity
@Table(name = "orders")
public class OrderTestDO implements IDomainObject<Long> {
	private static final long serialVersionUID = 2582167653817649406L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	private UserTestDO user;
	@Temporal(TemporalType.TIMESTAMP)
	private Date ordered;
	@Temporal(TemporalType.TIMESTAMP)
	private Date delivered;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private LocationTestDO deliveryLocation;
	private String orderId;

	@OneToMany
	private List<OrderItemTestDO> items = new LinkedList<OrderItemTestDO>();

	@Embedded
	private MailTemplateTestEmbeddable mailTemplate;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserTestDO getUser() {
		return user;
	}

	public void setUser(UserTestDO user) {
		this.user = user;
	}

	public Date getOrdered() {
		return ordered;
	}

	public void setOrdered(Date ordered) {
		this.ordered = ordered;
	}

	public LocationTestDO getDeliveryLocation() {
		return deliveryLocation;
	}

	public void setDeliveryLocation(LocationTestDO location) {
		this.deliveryLocation = location;
	}

	public List<OrderItemTestDO> getItems() {
		return items;
	}

	public void setItems(List<OrderItemTestDO> items) {
		this.items = items;
	}

	public Date getDelivered() {
		return delivered;
	}

	public void setDelivered(Date delivered) {
		this.delivered = delivered;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public MailTemplateTestEmbeddable getMailTemplate() {
		return mailTemplate;
	}

	public void setMailTemplate(MailTemplateTestEmbeddable mailTemplate) {
		this.mailTemplate = mailTemplate;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", location=" + (deliveryLocation == null ? "n/a" : deliveryLocation) + ", ordered="
				+ ordered + ", delivered = " + delivered + ", user=" + user + ", orderId = " + orderId + "]";
	}

}
