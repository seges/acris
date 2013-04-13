package sk.seges.corpis.server.domain.payment.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import sk.seges.corpis.server.domain.payment.server.model.base.BankBase;

@Entity
@Table(name = "bank")
public class JpaBank extends BankBase {

	private static final long serialVersionUID = 1430587109294119553L;

	@Id
	public Long getId() {
		return super.getId();
	}
	
	@Column(length = 50)
	public String getBankName() {
		return super.getBankName();
	}
	
	@Column(length = 20)
	@Size(min = 0, max = 20)
	public String getSwift() {
		return super.getSwift();
	}
}