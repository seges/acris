package sk.seges.corpis.server.domain.stock.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.jpa.JpaAccountableItem;
import sk.seges.corpis.server.domain.invoice.server.model.data.AccountableItemData;
import sk.seges.corpis.server.domain.stock.server.model.base.StockItemReservationBase;
import sk.seges.corpis.server.domain.stock.server.model.data.StockItemData;

@SuppressWarnings("serial")
@Entity
@Table(name = "stock_item_reservation")
@SequenceGenerator(name = JpaStockItemReservation.SEQ_STOCK_ITEM_RESERVATION, sequenceName = "seq_stock_item_reservation", initialValue = 1)
public class JpaStockItemReservation extends StockItemReservationBase {

	protected static final String SEQ_STOCK_ITEM_RESERVATION = "seqStockItemReservation";

	@Override
	@Id
	@GeneratedValue(generator = SEQ_STOCK_ITEM_RESERVATION)
	public Long getId() {
		return super.getId();
	}

	@Override
	@ManyToOne(fetch=FetchType.LAZY, targetEntity = JpaAccountableItem.class)
	public AccountableItemData getAccountableItem() {
		return super.getAccountableItem();
	}

	@Override
	@Column
	public int getCount() {
		return super.getCount();
	}

	@Override
	@Column
	public Date getReservationDate() {
		return super.getReservationDate();
	}

	@Override
	@Column(name = "stock_item")
	@ManyToOne( fetch=FetchType.EAGER, targetEntity = JpaStockItem.class)
	public StockItemData getStockItem() {
		return super.getStockItem();
	}
}