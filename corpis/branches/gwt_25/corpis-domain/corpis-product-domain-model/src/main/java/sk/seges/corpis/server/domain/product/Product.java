package sk.seges.corpis.server.domain.product;

import java.util.Date;
import java.util.List;
import java.util.Set;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Description;
import sk.seges.corpis.server.domain.Name;
import sk.seges.corpis.server.domain.Vat;
import sk.seges.corpis.server.domain.customer.CustomerCore;
import sk.seges.corpis.server.domain.search.SupValue;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Product extends IMutableDomainObject<Long> {

	String extId();
	String externalId();
	List<? extends Name> names();
	List<? extends Description> descriptions();
	String description();
	Float weight();
	Integer unitsPerPackage();
	List<? extends ProductPrice> prices();
	Set<? extends ProductPrice> fees();
	Vat vat();
	List<? extends Product> childVariants();
	List<? extends ProductCategory> categories();
	Set<? extends Tag> tags();
	String webId();
	Set<? extends SupValue> sups();
	CustomerCore manufacturer();
	CustomerCore seller();
	Product variant();
	List<? extends Product> relatedProducts();
	Integer count();
	String thumbnailPath();
	Date importedDate();
	Long priority();
	Boolean deleted();
}