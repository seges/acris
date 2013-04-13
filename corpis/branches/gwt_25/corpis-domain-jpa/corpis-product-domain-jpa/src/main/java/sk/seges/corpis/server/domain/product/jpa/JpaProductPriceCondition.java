package sk.seges.corpis.server.domain.product.jpa;

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

import sk.seges.corpis.server.domain.product.server.model.base.ProductPriceConditionBase;
import sk.seges.corpis.shared.domain.price.api.PriceConditionContext;

@Entity
@Table(name = "product_price_condition")
@SequenceGenerator(name = JpaProductPriceCondition.SEQ_PRODUCT_PRICE_CONDITIONS, sequenceName = "seq_price_conditions", initialValue = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "condition_type", discriminatorType = DiscriminatorType.INTEGER)
public class JpaProductPriceCondition extends ProductPriceConditionBase {

	private static final long serialVersionUID = 5505808455672968891L;
	
	protected static final String SEQ_PRODUCT_PRICE_CONDITIONS = "seqPriceConditions";

	@Override
	@Id
	@GeneratedValue(generator = SEQ_PRODUCT_PRICE_CONDITIONS)
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@Column(name = "description", unique = true)
	public String getConditionDescription() {
		return super.getConditionDescription();
	}
	
	@Override
	public boolean applies(PriceConditionContext context) {
		return false;
	}
}
