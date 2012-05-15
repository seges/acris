package sk.seges.corpis.server.domain.manufacture.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.jpa.JpaProduct;
import sk.seges.corpis.server.domain.invoice.server.model.data.ProductData;
import sk.seges.corpis.server.domain.manufacture.server.model.base.ManufactureItemBase;

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
	public Date getCalucatedEndDate() {
		return super.getCalucatedEndDate();
	}
	
	@Column
	public Date getCalucatedStartDate() {
		return super.getCalucatedStartDate();
	}
	
	@Override
	@Column
	public int getCount() {
		return super.getCount();
	}
		
	@Override
	@Column
	public Date getEndDate() {
		return super.getEndDate();
	}
	
	@Override
	@Column
	public int getManufacturedCount() {
		return super.getManufacturedCount();
	}
		
	@Override
	@Column
	public Date getStartDate() {
		return super.getStartDate();
	}
	
	@Override
	@ManyToOne(targetEntity=JpaProduct.class)
	@JoinColumn(name = "product")
	public ProductData getProduct() {
		return super.getProduct();
	}
}