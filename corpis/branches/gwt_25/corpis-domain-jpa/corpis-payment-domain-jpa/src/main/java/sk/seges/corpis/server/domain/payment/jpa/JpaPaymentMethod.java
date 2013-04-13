package sk.seges.corpis.server.domain.payment.jpa;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.server.domain.jpa.JpaPrice;
import sk.seges.corpis.server.domain.payment.server.model.base.PaymentMethodBase;
import sk.seges.corpis.server.domain.payment.server.model.data.PaymentMethodData;
import sk.seges.corpis.server.domain.server.model.data.CountryData;
import sk.seges.corpis.server.domain.server.model.data.PriceData;
import sk.seges.corpis.server.domain.server.model.data.VatData;
import sk.seges.corpis.shared.domain.EPaymentType;

@Entity
@Table(name = "payment_method", uniqueConstraints = @UniqueConstraint(columnNames = { PaymentMethodData.WEB_ID,
		PaymentMethodData.PAYMENT_TYPE, JpaPaymentMethod.COUNTRY_JOIN }))
@SequenceGenerator(name = JpaPaymentMethod.SEQ_PAYMENT, sequenceName = "seq_payment_method", initialValue = 1)
public class JpaPaymentMethod extends PaymentMethodBase {

	private static final long serialVersionUID = 287108762974445073L;

	protected static final String SEQ_PAYMENT = "seqPaymentMethod";
	protected static final String COUNTRY_JOIN = "country_id";

	public JpaPaymentMethod() {
		setPrice(new JpaPrice());
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
	
	@Override
	@Embedded
	@AttributeOverride(name = PriceData.VALUE, column = @Column(nullable = true))
	public PriceData getPrice() {
		return super.getPrice();
	}
	
	@Override
	@ManyToOne
	@JoinColumn(name = COUNTRY_JOIN)
	public CountryData getCountry() {
		return super.getCountry();
	}
	
	@Override
    @ManyToOne(fetch=FetchType.EAGER)
	public VatData getVat() {
		return super.getVat();
	}
}
