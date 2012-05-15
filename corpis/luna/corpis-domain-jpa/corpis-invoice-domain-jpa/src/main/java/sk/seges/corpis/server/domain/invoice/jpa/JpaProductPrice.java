package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import sk.seges.corpis.server.domain.invoice.server.model.base.ProductPriceBase;
import sk.seges.corpis.server.domain.invoice.server.model.data.ProductPriceConditionData;

@Entity
@Table(name = "product_prices")
@SequenceGenerator(name = JpaProductPrice.SEQ_PRODUCT_PRICE, sequenceName = "seq_prices", initialValue = 1)
public class JpaProductPrice extends ProductPriceBase {

	private static final long serialVersionUID = 7607037711836969276L;

	protected static final String SEQ_PRODUCT_PRICE = "seqPrices";

	@Override
	@Valid
	@Embedded
	public JpaPrice getPrice() {
		return (JpaPrice) super.getPrice();
	}

	@Override
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = JpaProductPriceCondition.class)
	public ProductPriceConditionData getPriceCondition() {
		return super.getPriceCondition();
	}

	@Override
	@Column
	public Short getPriority() {
		return super.getPriority();
	}

	@Override
	@Id
	@GeneratedValue(generator = SEQ_PRODUCT_PRICE)
	public Long getId() {
		return super.getId();
	}
}