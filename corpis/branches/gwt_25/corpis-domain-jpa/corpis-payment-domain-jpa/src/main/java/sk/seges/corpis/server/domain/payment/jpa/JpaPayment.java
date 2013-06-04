package sk.seges.corpis.server.domain.payment.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.payment.server.model.base.PaymentBase;

@Entity
@Table(name = "payment")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@SequenceGenerator(name = "seqPayment", sequenceName = "seq_payment", initialValue = 1)
@DiscriminatorColumn(name = "payment_type", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("1")
public class JpaPayment extends PaymentBase {

	private static final long serialVersionUID = -6673202171831912712L;

	protected static final String SEQ_PAYMENT = "seqPayment";

	@Override
	@Id
	@GeneratedValue(generator = "seqPayment")
	@Column
	public Long getId() {
		return super.getId();
	}

//	@Override
//	@OneToOne(cascade = { CascadeType.REFRESH }, mappedBy = "invoice", targetEntity = JpaInvoiceBase.class)
//	public InvoiceData getInvoice() {
//		return super.getInvoice();
//	}

	@Override
	@Column(name = "pay_date")
	public Date getPaymentDate() {
		return super.getPaymentDate();
	}	
}