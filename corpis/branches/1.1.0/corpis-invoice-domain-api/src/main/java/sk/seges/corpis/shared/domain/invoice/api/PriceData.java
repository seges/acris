package sk.seges.corpis.shared.domain.invoice.api;

import java.io.Serializable;
import java.math.BigDecimal;

public interface PriceData extends Serializable {

	public static final String CURRENCY = "currency";
	public static final String VALUE = "value";
	
	BigDecimal getValue();
	void setValue(BigDecimal value);

	CurrencyData getCurrency();
	void setCurrency(CurrencyData currency);

}