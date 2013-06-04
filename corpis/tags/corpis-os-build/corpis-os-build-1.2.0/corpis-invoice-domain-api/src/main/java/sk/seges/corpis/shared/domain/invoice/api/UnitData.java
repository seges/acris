package sk.seges.corpis.shared.domain.invoice.api;

import sk.seges.sesam.domain.IMutableDomainObject;

public interface UnitData<K> extends IMutableDomainObject<K> {

	String getLabelKey();

	void setLabelKey(String labelKey);

	UnitType getType();

	void setType(UnitType type);
}
