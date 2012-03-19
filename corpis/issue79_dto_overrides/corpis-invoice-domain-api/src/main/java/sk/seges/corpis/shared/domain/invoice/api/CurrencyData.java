package sk.seges.corpis.shared.domain.invoice.api;

import sk.seges.sesam.domain.IMutableDomainObject;

public interface CurrencyData extends IMutableDomainObject<Short> {

	String getCode();

	void setCode(String code);
}