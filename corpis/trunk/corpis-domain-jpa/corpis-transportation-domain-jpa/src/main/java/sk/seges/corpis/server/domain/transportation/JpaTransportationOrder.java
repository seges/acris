package sk.seges.corpis.server.domain.transportation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.jpa.JpaAccountable;
import sk.seges.corpis.server.domain.invoice.server.model.data.AccountableData;
import sk.seges.corpis.server.domain.server.model.data.PersonNameData;
import sk.seges.corpis.server.domain.stock.jpa.JpaStockItem;
import sk.seges.corpis.server.domain.stock.server.model.data.StockItemData;
import sk.seges.corpis.server.domain.transportation.server.model.base.TransportationOrderBase;

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
	@ManyToOne(targetEntity = JpaAccountable.class)
	public AccountableData getAccountable() {
		return super.getAccountable();
	}

	@Override
	@Column
	public int getCount() {
		return super.getCount();
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
	@ManyToOne(targetEntity = JpaStockItem.class)
	public StockItemData getStockItem() {
		return super.getStockItem();
	}
}