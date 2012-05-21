package sk.seges.corpis.server.domain.manufacture;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface ManufactureItem extends IDomainObject<Long> {

	Date date();

	Date calucatedDate();
	
	ManufactureOrder manufactureOrder();
	
	int count();
	int manufacturedCount();
}