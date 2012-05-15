package sk.seges.corpis.server.domain.invoice.jpa;

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

import sk.seges.corpis.server.domain.invoice.server.model.base.ProductPriceConditionBase;
import sk.seges.corpis.shared.domain.invoice.PriceConditionContext;

@Entity
@Table(name = "price_condition")
@SequenceGenerator(name = JpaProductPriceCondition.SEQ_PRODUCT_PRICE_CONDITIONS, sequenceName = "SEQ_PRICE_CONDITIONS", initialValue = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "condition_type", discriminatorType = DiscriminatorType.INTEGER)
public class JpaProductPriceCondition extends ProductPriceConditionBase {

	private static final long serialVersionUID = -2126478094945090914L;

	protected static final String SEQ_PRODUCT_PRICE_CONDITIONS = "seqPriceConditions";

	@Override
	public boolean applies(PriceConditionContext context) {
		return false;
	}

	@Override
	@Column(unique = true)
	public String getConditionDescription() {
		return super.getConditionDescription();
	}

	@Override
	@Id
	@GeneratedValue(generator = SEQ_PRODUCT_PRICE_CONDITIONS)
	public Long getId() {
		return super.getId();
	}	
}