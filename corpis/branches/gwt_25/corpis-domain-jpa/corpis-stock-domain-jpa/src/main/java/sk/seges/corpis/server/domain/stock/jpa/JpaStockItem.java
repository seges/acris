package sk.seges.corpis.server.domain.stock.jpa;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

import sk.seges.corpis.server.domain.product.jpa.JpaProductItem;
import sk.seges.corpis.server.domain.stock.server.model.data.StockItemData;
import sk.seges.corpis.server.domain.stock.server.model.data.WarehouseData;

@SuppressWarnings("serial")
@Table(name ="stock_item")
public class JpaStockItem extends JpaProductItem implements StockItemData {

	protected static final String SEQ_STOCK_ITEM = "seqStockItems";
	
	private Long id;
	
	private WarehouseData warehouse;
	
	@Override
	@Id
	@GeneratedValue(generator = SEQ_STOCK_ITEM)
	public Long getId() {
		return id;
	}

	@Override
	@Column
	public Integer getCount() {
		return super.getCount();
	}

	@Override
	@ManyToOne(fetch=FetchType.LAZY, targetEntity = JpaWarehouse.class)
	public WarehouseData getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WarehouseData warehouse) {
		this.warehouse = warehouse;
	}
}