package sk.seges.corpis.domain.price.jpa;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import sk.seges.corpis.server.domain.customer.Customer;
import sk.seges.corpis.server.domain.customer.server.model.data.CustomerData;
import sk.seges.corpis.server.domain.invoice.Product;
import sk.seges.corpis.server.domain.invoice.server.model.data.ProductData;
import sk.seges.corpis.shared.domain.price.api.IPriceCondition;
import sk.seges.corpis.shared.domain.price.api.PriceConditionContext;
import sk.seges.corpis.shared.domain.price.server.model.base.PriceConditionBase;
import sk.seges.corpis.shared.domain.price.server.model.data.PriceConditionData;
import sk.seges.sesam.domain.IDomainObject;

@Entity
@Table(name = "price_conditions", uniqueConstraints = { @UniqueConstraint(columnNames={PriceConditionData.CUSTOMER, PriceConditionData.PRODUCT, PriceConditionData.WEB_ID})})
@SequenceGenerator(name = JpaPriceCondition.SEQ_PRICE_CONDITIONS, sequenceName = "SEQ_PRICE_CONDITIONS", initialValue = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CONDITION_TYPE", discriminatorType = DiscriminatorType.INTEGER)
public class JpaPriceCondition extends PriceConditionBase implements IDomainObject<Long>, IPriceCondition {
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
	
	@Column(name = PriceConditionData.CUSTOMER)
	public CustomerData getCustomer() {
		return super.getCustomer();
	}
	
	@Column(name = PriceConditionData.PRODUCT)
	public ProductData getProduct() {
		return super.getProduct();
	}
	
	@Column(name = PriceConditionData.WEB_ID)
	@NotNull
	public String getWebId() {
		return super.getWebId();
	}
	
	@Override
	public boolean applies(PriceConditionContext context, String webId, Customer customer, Product product) {
		if (!getWebId().equals(webId) || (getCustomer() != null && !getCustomer().equals(customer)) || 
				(getProduct() != null && !getProduct().equals(product))) {
			return false;
		}
		return true;
	}
}
