package sk.seges.corpis.server.domain.invoice;

import java.util.List;
import java.util.Set;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Name;
import sk.seges.corpis.server.domain.customer.Customer;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface Product extends IDomainObject<Long> {

	String extId();

	List<Name> names();

	String description();

	Float weight();

	Integer unitsPerPackage();

	List<ProductPrice> prices();

	Set<ProductPrice> fees();

	Vat vat();

	List<ProductCategory> categories();

	Set<Tag> tags();

	String webId();

	Customer manufacturer();

	Customer seller();

	Product variant();
	
	List<Product> relatedProducts();
}