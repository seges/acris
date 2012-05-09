package sk.seges.corpis.server.domain.stock.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.jpa.JpaProduct;
import sk.seges.corpis.server.domain.invoice.server.model.data.ProductData;
import sk.seges.corpis.server.domain.stock.server.model.base.StockItemBase;
import sk.seges.corpis.server.domain.stock.server.model.data.WarehouseData;

@SuppressWarnings("serial")
@Entity
@Table(name = "stock_item")
@SequenceGenerator(name = JpaStockItem.SEQ_STOCK_ITEM, sequenceName = "seq_stock_items", initialValue = 1)
public class JpaStockItem extends StockItemBase {

	protected static final String SEQ_STOCK_ITEM = "seqStockItems";

	@Override
	@Id
	@GeneratedValue(generator = SEQ_STOCK_ITEM)
	public Long getId() {
		return super.getId();
	}

	@Override
	@Column
	public int getCount() {
		return super.getCount();
	}

	@Override
	@ManyToOne(fetch=FetchType.LAZY, targetEntity = JpaWarehouse.class)
	public WarehouseData getWarehouse() {
		return super.getWarehouse();
	}

	@Override
	@ManyToOne(fetch=FetchType.EAGER, targetEntity = JpaProduct.class)
	public ProductData getProduct() {
		return super.getProduct();
	}
}