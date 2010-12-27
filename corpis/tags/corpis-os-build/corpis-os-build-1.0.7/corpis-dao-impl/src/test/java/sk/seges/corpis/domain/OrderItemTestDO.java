package sk.seges.corpis.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.sesam.domain.IDomainObject;

@Entity
@Table(name = "order_items")
public class OrderItemTestDO implements IDomainObject<Long> {
	private static final long serialVersionUID = -436866380493116237L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private Integer count;
	private BigDecimal price;
	private String description;
	@ManyToOne
	private VATTestDO vat;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public VATTestDO getVat() {
		return vat;
	}
	public void setVat(VATTestDO vat) {
		this.vat = vat;
	}
	@Override
	public String toString() {
		return "OrderItem [count=" + count + ", description=" + description
				+ ", id=" + id + ", price=" + price + ", vat=" + vat + "]";
	}
}
