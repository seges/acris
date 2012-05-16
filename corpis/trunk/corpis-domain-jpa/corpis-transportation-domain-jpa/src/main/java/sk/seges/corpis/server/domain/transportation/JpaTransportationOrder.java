package sk.seges.corpis.server.domain.transportation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.jpa.JpaAccountableItem;
import sk.seges.corpis.server.domain.invoice.server.model.data.AccountableItemData;
import sk.seges.corpis.server.domain.server.model.data.PersonNameData;
import sk.seges.corpis.server.domain.transportation.server.model.base.TransportationOrderBase;
import sk.seges.corpis.server.domain.transportation.server.model.data.TransportationData;

@Entity
@Table(name = "transportation_order")
@SequenceGenerator(name = JpaTransportationOrder.SEQ_TRANSPORTATION_ORDER, sequenceName = "seq_transportation_order", initialValue = 1)
@SuppressWarnings("serial")
public class JpaTransportationOrder extends TransportationOrderBase {

	protected static final String SEQ_TRANSPORTATION_ORDER = "seqTransporationOrder";

	@Override
	@Id
	@GeneratedValue(generator = SEQ_TRANSPORTATION_ORDER)
	public Long getId() {
		return super.getId();
	}	

	@Override
	@ManyToOne(targetEntity = JpaAccountableItem.class)
	public AccountableItemData getAccountableItem() {
		return super.getAccountableItem();
	}

	@Override
	@Column
	public Date getOrderDate() {
		return super.getOrderDate();
	}

	@Override
	@Embedded
	public PersonNameData getPerson() {
		return super.getPerson();
	}
	
	@Override
	@OneToOne(targetEntity = JpaTransportation.class)
	public TransportationData getTransportation() {
		return super.getTransportation();
	}
}