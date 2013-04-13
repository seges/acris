package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.server.model.base.LoyaltyCardBase;

@Entity
@Table(name = "loyalty_card")
@SequenceGenerator(name = JpaLoyaltyCard.LOYALTY_CARD_SEQ, sequenceName = JpaLoyaltyCard.LOYALTY_CARD_SEQ, initialValue = 1)
public class JpaLoyaltyCard extends LoyaltyCardBase {

	private static final long serialVersionUID = 1339367273899359304L;
	
	protected static final String LOYALTY_CARD_SEQ = "loyaltyCardSeq";

	@Override
	@Id
	@GeneratedValue(generator = JpaLoyaltyCard.LOYALTY_CARD_SEQ)
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@Column(name = "webid")
	public String getWebId() {
		return super.getWebId();
	}

	@Override
	@Column(name = "number", unique = true)
	public String getNumber() {
		return super.getNumber();
	}

	@Override
	@Column(name = "rebate")
	public Double getRebate() {
		return super.getRebate();
	}

	@Override
	@Column(name = "active")
	public Boolean getActive() {
		return super.getActive();
	}
}