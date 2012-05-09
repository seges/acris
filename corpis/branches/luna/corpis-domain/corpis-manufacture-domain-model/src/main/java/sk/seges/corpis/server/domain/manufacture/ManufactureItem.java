package sk.seges.corpis.server.domain.manufacture;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.invoice.Product;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface ManufactureItem extends IDomainObject<Long> {

	Date startDate();
	Date endDate();

	Date calucatedStartDate();
	Date calucatedEndDate();
	
	Product product();
	
	int count();
	int manufacturedCount();
}