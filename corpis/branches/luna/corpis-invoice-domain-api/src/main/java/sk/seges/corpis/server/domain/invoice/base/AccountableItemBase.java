package sk.seges.corpis.server.domain.invoice.base;

import sk.seges.corpis.server.domain.invoice.PriceData;
import sk.seges.corpis.server.domain.invoice.UnitData;
import sk.seges.corpis.server.domain.invoice.VatData;
import sk.seges.corpis.shared.domain.invoice.api.AccountableItemData;

public class AccountableItemBase implements AccountableItemData {

	private static final long serialVersionUID = 5917813221965355632L;

	public static final String DESCRIPTION = "description";
	public static final String AMOUNT = "amount";
	public static final String BASE_PRICE = "basePrice";
	public static final String UNIT = "unit";
	public static final String VAT = "vat";

	private String description;
	private Float amount;
	private PriceData price;
	private UnitData<?> unit;
	private VatData<?> vat;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public UnitData<?> getUnit() {
		return unit;
	}

	public void setUnit(UnitData<?> unit) {
		this.unit = unit;
	}

	public VatData<?> getVat() {
		return vat;
	}

	public void setVat(VatData<?> vat) {
		this.vat = vat;
	}

	@Override
	public PriceData getPrice() {
		return price;
	}

	@Override
	public void setPrice(PriceData price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((vat == null) ? 0 : vat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountableItemData other = (AccountableItemData) obj;
		if (amount == null) {
			if (other.getAmount() != null)
				return false;
		} else if (!amount.equals(other.getAmount()))
			return false;
		if (price == null) {
			if (other.getPrice() != null)
				return false;
		} else if (!price.equals(other.getPrice()))
			return false;
		if (description == null) {
			if (other.getDescription() != null)
				return false;
		} else if (!description.equals(other.getDescription()))
			return false;
		if (unit == null) {
			if (other.getUnit() != null)
				return false;
		} else if (!unit.equals(other.getUnit()))
			return false;
		if (vat == null) {
			if (other.getVat() != null)
				return false;
		} else if (!vat.equals(other.getVat()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AccountableItem [amount=" + amount + ", basePrice=" + price + ", description=" + description + ", unit=" + unit + ", vat=" + vat
				+ "]";
	}
}