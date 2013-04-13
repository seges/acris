/**
 * 
 */
package sk.seges.corpis.server.domain.jpa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import sk.seges.corpis.server.domain.server.model.base.PriceBase;

/**
 * @author eldzi
 */
@Embeddable
public class JpaPrice extends PriceBase {
	private static final long serialVersionUID = -1630656583722890660L;
	
	public JpaPrice() {
		
	}
	
	public JpaPrice(BigDecimal value) {
		setValue(value);
	}
	
	@NotNull
	@Column(nullable = false, precision = 50, scale = 20)
	public BigDecimal getValue() {
		return super.getValue();
	}

	@NotNull
	@ManyToOne
	public JpaCurrency getCurrency() {
		return (JpaCurrency) super.getCurrency();
	}	
}
