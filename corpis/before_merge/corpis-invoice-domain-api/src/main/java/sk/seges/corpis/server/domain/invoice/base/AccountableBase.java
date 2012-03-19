package sk.seges.corpis.server.domain.invoice.base;

import java.util.Date;

import sk.seges.corpis.shared.domain.customer.api.CustomerBaseData;
import sk.seges.corpis.shared.domain.invoice.api.AccountableData;
import sk.seges.corpis.shared.domain.invoice.api.CurrencyData;

@SuppressWarnings("serial")
public abstract class AccountableBase<T> implements AccountableData<T> {

	protected CustomerBaseData<?> customer;
	protected Date creationDate;
	protected CurrencyData currency;

	public CustomerBaseData<?> getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerBaseData<?> customer) {
		this.customer = customer;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public CurrencyData getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyData currency) {
		this.currency = currency;
	}
}