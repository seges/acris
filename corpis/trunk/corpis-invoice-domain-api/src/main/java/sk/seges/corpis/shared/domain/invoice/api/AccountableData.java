package sk.seges.corpis.shared.domain.invoice.api;

import java.util.Date;
import java.util.List;

import sk.seges.corpis.shared.domain.customer.api.CustomerBaseData;
import sk.seges.sesam.domain.IDomainObject;

public interface AccountableData<T> extends IDomainObject<T> {

	CustomerBaseData<?> getCustomer();
	void setCustomer(CustomerBaseData<?> customer);
	
	Date getCreationDate();
	void setCreationDate(Date creationDate);

	CurrencyData getCurrency();
	void setCurrency(CurrencyData currency);

	List<? extends AccountableItemData> getAccountableItems();
}
