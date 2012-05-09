package sk.seges.corpis.server.domain.stock.jpa;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.stock.server.model.base.WarehouseBase;
import sk.seges.corpis.server.domain.stock.server.model.data.StockItemData;

@SuppressWarnings("serial")
@Entity
@Table(name = "warehouse")
@SequenceGenerator(name = JpaWarehouse.SEQ_WAREHOUSE, sequenceName = "seq_warehouse", initialValue = 1)
public class JpaWarehouse extends WarehouseBase {

	protected static final String SEQ_WAREHOUSE = "seqWarehouses";

	@Override
	@Id
	@GeneratedValue(generator = SEQ_WAREHOUSE)
	@Column(name = "ID")
	public Integer getId() {
		return super.getId();
	}

	@Override
	@Column(name = "NAME")
	public String getName() {
		return super.getName();
	}

	@Override
	@Column(name = "ADDRESS")
	public String getAddress() {
		return super.getAddress();
	}

	@Override
	@Column(name = "EMAIL")
	public String getEmail() {
		return super.getEmail();
	}

	@Override
	@Column(name = "PHONE")
	public String getPhone() {
		return super.getPhone();
	}

	@Override
	@Column(name = "FAX")
	public String getFax() {
		return super.getFax();
	}

	@Override
	@Column(name = "ACTIVE")
	public Boolean getActive() {
		return super.getActive();
	}

	@Override
	@Column(name = "TOWN")
	public String getTown() {
		return super.getTown();
	}

	@Override
	@Column(name = "CONTACT_PERSON")
	public String getContactPerson() {
		return super.getContactPerson();
	}

	@Override
	@Column(name = "PSC")
	public String getPsc() {
		return super.getPsc();
	}

	@Override
	@OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY, targetEntity = JpaStockItem.class)
	public List<StockItemData> getStockItems() {
		return super.getStockItems();
	}
}