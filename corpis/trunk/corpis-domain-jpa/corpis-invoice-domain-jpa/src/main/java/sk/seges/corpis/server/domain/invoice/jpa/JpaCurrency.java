package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.server.model.base.CurrencyBase;

@Entity
@Table(name = "currencies")
public class JpaCurrency extends CurrencyBase {
	private static final long serialVersionUID = -7897429807056078485L;

	public static final String CODE = "code";

	@Id
	public Short getId() {
		return super.getId();
	}

	/**
	 * @return 3-letter code as defined in ISO 4217
	 */
	@Column(unique = true, length = 3)
	public String getCode() {
		return super.getCode();
	}
}
