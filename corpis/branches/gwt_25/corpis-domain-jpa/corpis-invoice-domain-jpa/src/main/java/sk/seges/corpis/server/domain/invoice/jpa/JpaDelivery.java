package sk.seges.corpis.server.domain.invoice.jpa;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
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

import sk.seges.corpis.server.domain.invoice.server.model.base.DeliveryBase;
import sk.seges.corpis.server.domain.invoice.server.model.data.DeliveryData;
import sk.seges.corpis.server.domain.jpa.JpaPrice;
import sk.seges.corpis.server.domain.server.model.data.CountryData;
import sk.seges.corpis.server.domain.server.model.data.PriceData;
import sk.seges.corpis.server.domain.server.model.data.VatData;
import sk.seges.corpis.shared.domain.invoice.ETransports;

@Entity
@Table(name = "delivery", uniqueConstraints = @UniqueConstraint(columnNames = { DeliveryData.WEB_ID,
		DeliveryData.TRANSPORT_TYPE, JpaDelivery.COUNTRY_JOIN, DeliveryData.PRICE_CONDITION,
		DeliveryData.PRICE_CONDITION_WITH_V_A_T, DeliveryData.AMOUNT_CONDITION, DeliveryData.WEIGHT_CONDITION }))
@SequenceGenerator(name = JpaDelivery.SEQ_DELIVERY, sequenceName = "seq_delivery", initialValue = 1)
public class JpaDelivery extends DeliveryBase {

	private static final long serialVersionUID = -1965331674453503605L;
	
	protected static final String SEQ_DELIVERY = "seqDeliver";
	protected static final String COUNTRY_JOIN = "country_id";

	public JpaDelivery() {
		setPrice(new JpaPrice());
		setTransportType(ETransports.values()[0]);
		setPriceConditionWithVAT(false);
	}

	@Id
	@GeneratedValue(generator = SEQ_DELIVERY)
	public Long getId() {
		return super.getId();
	}

	@Column
	@Override
	public String getWebId() {
		return super.getWebId();
	}
	
	@Column
	@Override
	public ETransports getTransportType() {
		return super.getTransportType();
	}
	
	@ManyToOne
	@JoinColumn(name = COUNTRY_JOIN)
	@Override
	public CountryData getCountry() {
		return super.getCountry();
	}
	
	@Embedded
	@AttributeOverride(name = PriceData.VALUE, column = @Column(nullable = true))
	@Override
	public PriceData getPrice() {
		return super.getPrice();
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@Override
	public VatData getVat() {
		return super.getVat();
	}

	@Override
	@Column(precision = 50, scale = 20)
	public BigDecimal getPriceCondition() {
		return super.getPriceCondition();
	}

	@Override
	@Column
	public Boolean getPriceConditionWithVAT() {
		return super.getPriceConditionWithVAT();
	}
	
	@Override
	@Column
	public Float getAmountCondition() {
		return super.getAmountCondition();
	}
	
	@Override
	@Column
	public Float getWeightCondition() {
		return super.getWeightCondition();
	}
}