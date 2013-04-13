package sk.seges.corpis.server.domain.payment.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sk.seges.corpis.server.domain.customer.jpa.JpaCustomerCore;
import sk.seges.corpis.server.domain.customer.server.model.data.CustomerCoreData;
import sk.seges.corpis.server.domain.payment.server.model.base.BankAccountBase;
import sk.seges.corpis.server.domain.payment.server.model.data.BankAccountData;
import sk.seges.corpis.server.domain.payment.server.model.data.BankData;

@Entity
@Table(name = "bank_account", uniqueConstraints = @UniqueConstraint(columnNames = {
		JpaBankAccount.BANK_JOIN, BankAccountData.BANK_ACCOUNT }))
@SequenceGenerator(name = JpaBankAccount.SEQ, sequenceName = "seq_bank_account", initialValue = 1)
public class JpaBankAccount extends BankAccountBase {

	private static final long serialVersionUID = 7591683343596826189L;
	
	protected static final String SEQ = "seqBankAccount";
	protected static final String BANK_JOIN = "bank_id";

	@Id
	@GeneratedValue(generator = SEQ)
	public Long getId() {
		return super.getId();
	}

	@ManyToOne(targetEntity = JpaBank.class)
	@JoinColumn(name = BANK_JOIN)
	public BankData getBank() {
		return super.getBank();
	}

	@Column(length = 20, nullable = false)
	@Size(min = 0, max = 20)
	@NotNull
	public String getBankAccount() {
		return super.getBankAccount();
	}
	
	@ManyToOne(targetEntity = JpaCustomerCore.class)
	public CustomerCoreData getCustomer() {
		return super.getCustomer();
	}
	
	@Column(length = 30)
	@Size(min = 0, max = 30)
	public String getIban() {
		return super.getIban();
	}

}
