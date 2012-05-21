package sk.seges.corpis.server.domain.manufacture.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.manufacture.server.model.base.ManufactureItemBase;
import sk.seges.corpis.server.domain.manufacture.server.model.data.ManufactureOrderData;
import sk.seges.corpis.shared.domain.manufacture.EManufactureItemState;

@SuppressWarnings("serial")
@Entity
@SequenceGenerator(name = JpaManufactureItem.SEQ_MANUFACTURE_ITEMS, sequenceName = "seq_manufacture_items", initialValue = 1)
@Table(name = "manufacture_item")
public class JpaManufactureItem extends ManufactureItemBase {

	protected static final String SEQ_MANUFACTURE_ITEMS = "seqManufactureItems";

	@Override
	@Id
	@GeneratedValue(generator = SEQ_MANUFACTURE_ITEMS)
	public Long getId() {
		return super.getId();
	}

	@Override
	@Column
	public Date getCalucatedDate() {
		return super.getCalucatedDate();
	}
	
	@Override
	@Column
	public int getCount() {
		return super.getCount();
	}
		
	@Override
	@Column
	public Date getDate() {
		return super.getDate();
	}
	
	@Override
	@Column
	public int getManufacturedCount() {
		return super.getManufacturedCount();
	}
		
	@Override
	@ManyToOne(targetEntity = JpaManufactureOrder.class)
	public ManufactureOrderData getManufactureOrder() {
		return super.getManufactureOrder();
	}
	
	@Override
	@Column(name = "state")
	public EManufactureItemState getState() {
		return super.getState();
	}
}