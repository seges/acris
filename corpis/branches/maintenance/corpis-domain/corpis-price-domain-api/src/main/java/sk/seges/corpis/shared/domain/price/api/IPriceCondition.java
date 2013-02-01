package sk.seges.corpis.shared.domain.price.api;

import sk.seges.corpis.server.domain.customer.Customer;
import sk.seges.corpis.server.domain.invoice.Product;


public interface IPriceCondition {
	public boolean applies(PriceConditionContext context, String webId, Customer customer, Product product);
}
