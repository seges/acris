package sk.seges.corpis.shared.domain.invoice.api;

import java.io.Serializable;

import sk.seges.corpis.shared.domain.api.HasDescription;

public interface AccountableItemData extends HasPrice, HasDescription, Serializable {

	Float getAmount();

	void setAmount(Float amount);

	UnitData<?> getUnit();

	void setUnit(UnitData<?> unit);

	VatData<?> getVat();

	void setVat(VatData<?> vat);
}