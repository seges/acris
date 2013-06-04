package sk.seges.corpis.server.domain.stock.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.product.jpa.JpaProduct;
import sk.seges.corpis.server.domain.stock.server.model.data.StockItemData;
import sk.seges.corpis.server.domain.stock.server.model.data.WarehouseData;

@Entity
@SuppressWarnings("serial")
@Table(name ="stock_item")
public class JpaStockItem extends JpaProduct implements StockItemData {

	private WarehouseData warehouse;

	@Override
	@ManyToOne(fetch=FetchType.LAZY, targetEntity = JpaWarehouse.class)
	public WarehouseData getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WarehouseData warehouse) {
		this.warehouse = warehouse;
	}
}