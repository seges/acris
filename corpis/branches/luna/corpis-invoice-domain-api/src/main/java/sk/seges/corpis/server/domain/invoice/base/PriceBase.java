package sk.seges.corpis.server.domain.invoice.base;

import java.math.BigDecimal;

import sk.seges.corpis.server.domain.invoice.PriceData;
import sk.seges.corpis.shared.domain.invoice.api.CurrencyData;

@SuppressWarnings("serial")
public class PriceBase implements PriceData {

	private BigDecimal value = new BigDecimal(0);
	private CurrencyData currency;

	@Override
	public BigDecimal getValue() {
		return value;
	}

	@Override
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public CurrencyData getCurrency() {
		return currency;
	}

	@Override
	public void setCurrency(CurrencyData currency) {
		this.currency = currency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		PriceData other = (PriceData) obj;
		if (currency == null) {
			if (other.getCurrency() != null)
				return false;
		} else if (!currency.equals(other.getCurrency()))
			return false;
		if (value == null) {
			if (other.getValue() != null)
				return false;
		} else if (!value.equals(other.getValue()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Price [currency=" + currency + ", value=" + value + "]";
	}
}