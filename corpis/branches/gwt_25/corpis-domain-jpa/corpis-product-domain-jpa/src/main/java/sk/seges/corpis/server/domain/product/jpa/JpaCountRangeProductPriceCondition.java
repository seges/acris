package sk.seges.corpis.server.domain.product.jpa;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.server.domain.invoice.server.model.data.OrderItemData;
import sk.seges.corpis.server.domain.product.server.model.data.CountRangeProductPriceConditionData;
import sk.seges.corpis.shared.domain.price.api.PriceConditionContext;

@Entity
@DiscriminatorValue("2")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"lowerBoundary", "upperBoundary"})})
public class JpaCountRangeProductPriceCondition extends JpaProductPriceCondition implements CountRangeProductPriceConditionData {

	private static final long serialVersionUID = -394994292273092661L;

	public static final String CTX_ITEM = "item";

    private Integer lowerBoundary;
    private Integer upperBoundary;

    public JpaCountRangeProductPriceCondition() {
    	
    }
    
    public JpaCountRangeProductPriceCondition(Integer lowerBoundary, Integer upperBoundary) {
    	this.lowerBoundary = lowerBoundary;
    	this.upperBoundary = upperBoundary;
    }
        
    @Column(name = "lower_boundary")
    public Integer getLowerBoundary() {
		return lowerBoundary;
	}
    
    @Column(name = "upper_boundary")
    public Integer getUpperBoundary() {
		return upperBoundary;
	}

	@Override
	public void setLowerBoundary(Integer lowerBoundary) {
		this.lowerBoundary = lowerBoundary;
	}

	@Override
	public void setUpperBoundary(Integer upperBoundary) {
		this.upperBoundary = upperBoundary;
	}

    @Override
    public boolean applies(PriceConditionContext context) {
    	OrderItemData orderItem = context.get(CTX_ITEM);
        return isWithinRange(orderItem.getAmount());
    }

    private boolean isWithinRange(Float count) {
        if(null == count) {
            throw new IllegalArgumentException("Can't validate null count");
        }
        boolean result = false;
        if(null == lowerBoundary && null == upperBoundary) {
            result = true;
        } else if(null == lowerBoundary) {
            result = count <= upperBoundary;
        } else if(null == upperBoundary) {
            result = count >= lowerBoundary;
        } else {
            result = count >= lowerBoundary && count <= upperBoundary;
        }

        return result;
    }
}