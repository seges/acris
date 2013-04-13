package sk.seges.corpis.server.domain.product.jpa;

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
import javax.validation.constraints.Size;

import sk.seges.corpis.server.domain.jpa.JpaPrice;
import sk.seges.corpis.server.domain.product.server.model.base.ProductPriceBase;
import sk.seges.corpis.server.domain.product.server.model.data.ProductPriceConditionData;

@Entity
@Table(name = "product_prices")
@SequenceGenerator(name = JpaProductPrice.SEQ_PRODUCT_PRICE, sequenceName = "seq_prices", initialValue = 1)
public class JpaProductPrice extends ProductPriceBase {

	private static final long serialVersionUID = 7607037711836969276L;

	protected static final String SEQ_PRODUCT_PRICE = "seqPrices";

	protected static final int EXT_ID_MIN_LENGTH = 1;
	protected static final int EXT_ID_MAX_LENGTH = 30;

	@Override
	@Valid
	@Embedded
	public JpaPrice getPrice() {
		return (JpaPrice) super.getPrice();
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
	
	@Size(min = EXT_ID_MIN_LENGTH, max = EXT_ID_MAX_LENGTH)
	@Column(name = EXTERNAL_ID, length = EXT_ID_MAX_LENGTH, nullable = true)
	public String getExternalId() {
		return super.getExternalId();
	};

	@Override
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = JpaProductPriceCondition.class)
	public ProductPriceConditionData getPriceCondition() {
		return super.getPriceCondition();
	}
}