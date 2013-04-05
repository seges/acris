/**
 * 
 */
package sk.seges.corpis.server.domain.invoice.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import sk.seges.corpis.server.domain.customer.jpa.JpaCustomerBase;
import sk.seges.corpis.server.domain.invoice.server.model.base.AccountableBase;

/**
 * @author eldzi
 */
@MappedSuperclass
public abstract class JpaAccountable extends AccountableBase {

	private static final long serialVersionUID = 7608869409434126440L;

	@ManyToOne(fetch = FetchType.LAZY)
	public JpaCustomerBase getCustomer() {
		return (JpaCustomerBase) super.getCustomer();
	}

	@Column(name = "creation_date")
	public Date getCreationDate() {
		return super.getCreationDate();
	}

	@ManyToOne
	public JpaCurrency getCurrency() {
		return (JpaCurrency) super.getCurrency();
	}
}