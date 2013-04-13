package sk.seges.corpis.shared.domain.price.api;

import sk.seges.corpis.server.domain.customer.server.model.data.CustomerCoreData;
import sk.seges.corpis.server.domain.product.server.model.data.ProductData;


public interface IPriceCondition {
	
	boolean applies(PriceConditionContext context, String webId, CustomerCoreData customer, ProductData product);

	String getName();
}