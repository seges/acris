package sk.seges.corpis.server.domain.payment.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.server.domain.payment.server.model.base.PaymentMethodBase;
import sk.seges.corpis.server.domain.payment.server.model.data.PaymentMethodData;
import sk.seges.corpis.shared.domain.EPaymentType;

@Entity
@Table(name = "payment_method", uniqueConstraints = @UniqueConstraint(columnNames = { PaymentMethodData.WEB_ID,
		PaymentMethodData.PAYMENT_TYPE }))
@SequenceGenerator(name = JpaPaymentMethod.SEQ_PAYMENT, sequenceName = "seq_payment_method", initialValue = 1)
public class JpaPaymentMethod extends PaymentMethodBase {

	private static final long serialVersionUID = 287108762974445073L;

	protected static final String SEQ_PAYMENT = "seqPaymentMethod";

	public JpaPaymentMethod() {
		setPaymentType(EPaymentType.values()[0]);
	}

	@Override
	@Id
	@GeneratedValue(generator = SEQ_PAYMENT)
	public Long getId() {
		return super.getId();
	}

	@Override
	public String getWebId() {
		return super.getWebId();
	}

	@Override
	@Column
	public EPaymentType getPaymentType() {
		return super.getPaymentType();
	}
}