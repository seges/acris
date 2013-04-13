package sk.seges.corpis.server.domain.product.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import sk.seges.corpis.server.domain.customer.jpa.JpaCustomerCore;
import sk.seges.corpis.server.domain.customer.server.model.data.CustomerCoreData;
import sk.seges.corpis.server.domain.product.server.model.base.PriceConditionBase;
import sk.seges.corpis.server.domain.product.server.model.data.PriceConditionData;
import sk.seges.corpis.server.domain.product.server.model.data.ProductData;
import sk.seges.corpis.shared.domain.price.api.IPriceCondition;
import sk.seges.corpis.shared.domain.price.api.PriceConditionContext;
import sk.seges.sesam.domain.IDomainObject;

@Entity
@Table(name = "price_conditions", uniqueConstraints = { @UniqueConstraint(columnNames={PriceConditionData.CUSTOMER, PriceConditionData.PRODUCT, PriceConditionData.WEB_ID})})
@SequenceGenerator(name = JpaPriceCondition.SEQ_PRICE_CONDITIONS, sequenceName = "SEQ_PRICE_CONDITIONS", initialValue = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CONDITION_TYPE", discriminatorType = DiscriminatorType.INTEGER)
public class JpaPriceCondition extends PriceConditionBase {
	
	private static final long serialVersionUID = 1428890443795569843L;
	
	protected static final String SEQ_PRICE_CONDITIONS = "seqPriceCondition";

	@Id
	@GeneratedValue(generator = SEQ_PRICE_CONDITIONS)
	public Long getId() {
		return super.getId();
	}

	@Column(name = PriceConditionData.VALUE)
	public Double getValue() {
		return super.getValue();
	}
	
	@ManyToOne(cascade = { CascadeType.ALL }, targetEntity = JpaCustomerCore.class)
	public CustomerCoreData getCustomer() {
		return super.getCustomer();
	}
	
	@ManyToOne(targetEntity = JpaProduct.class)
	public ProductData getProduct() {
		return super.getProduct();
	}
	
	@Column(name = PriceConditionData.WEB_ID)
	@NotNull
	public String getWebId() {
		return super.getWebId();
	}

	@Transient
	@Override
	public String getName() {
		return "";
	}
	
	@Override
	public boolean applies(PriceConditionContext context, String webId, CustomerCoreData customer, ProductData product) {
		return (getWebId().equals(webId) && (getCustomer() == null || equalsById(getCustomer(), customer)) && 
				(getProduct() == null || equalsById(getProduct(), product)));
	}
	
	protected boolean equalsById(IDomainObject<?> obj1, IDomainObject<?> obj2) {
		return ((obj1 == null && obj2 == null) || (obj1 != null && obj2 != null && obj1.getId().equals(obj2.getId())));
	}
}
