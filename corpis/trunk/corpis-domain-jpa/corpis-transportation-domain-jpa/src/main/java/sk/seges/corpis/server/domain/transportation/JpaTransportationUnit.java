package sk.seges.corpis.server.domain.transportation;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.server.model.data.AddressData;
import sk.seges.corpis.server.domain.server.model.data.PersonNameData;
import sk.seges.corpis.server.domain.stock.jpa.JpaStockItem;
import sk.seges.corpis.server.domain.stock.server.model.data.StockItemData;
import sk.seges.corpis.server.domain.transportation.server.model.base.TransportationUnitBase;

@SuppressWarnings("serial")
@Entity
@Table(name = "transportation_unit")
@SequenceGenerator(name = JpaTransportationUnit.SEQ_TRANSPORTATION_UNIT, sequenceName = "seq_transportation_unit", initialValue = 1)
public class JpaTransportationUnit extends TransportationUnitBase {

	protected static final String SEQ_TRANSPORTATION_UNIT = "seqTransporationUnit";

	@Override
	@Id
	@GeneratedValue(generator = SEQ_TRANSPORTATION_UNIT)
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@Column
	public int getCount() {
		return super.getCount();
	}

	@Override
	@Embedded
	public AddressData getDeliveryAddress() {
		return super.getDeliveryAddress();
	}

	@Override
	@Embedded
	public PersonNameData getDeliveryPerson() {
		return super.getDeliveryPerson();
	}

	@Override
	@OneToMany(targetEntity = JpaStockItem.class)
	public StockItemData getStockItem() {
		return super.getStockItem();
	}
}