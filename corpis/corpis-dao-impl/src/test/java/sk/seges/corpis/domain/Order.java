package sk.seges.corpis.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
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
public class Order implements IDomainObject<Long> {
	private static final long serialVersionUID = 2582167653817649406L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	private User user;
	@Temporal(TemporalType.TIMESTAMP)
	private Date ordered;
	@Temporal(TemporalType.TIMESTAMP)
	private Date delivered;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Location deliveryLocation;
	private String orderId;

	@OneToMany
	private List<OrderItem> items = new LinkedList<OrderItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getOrdered() {
		return ordered;
	}

	public void setOrdered(Date ordered) {
		this.ordered = ordered;
	}

	public Location getDeliveryLocation() {
		return deliveryLocation;
	}

	public void setDeliveryLocation(Location location) {
		this.deliveryLocation = location;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
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

	@Override
	public String toString() {
		return "Order [id=" + id + ", location=" + (deliveryLocation == null ? "n/a" : deliveryLocation)
				+ ", ordered=" + ordered + ", delivered = " + delivered + ", user=" + user + ", orderId = " + orderId + "]";
	}
}
