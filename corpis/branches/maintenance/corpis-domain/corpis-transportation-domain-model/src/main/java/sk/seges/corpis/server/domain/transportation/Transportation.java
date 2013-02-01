package sk.seges.corpis.server.domain.transportation;

import java.util.Date;
import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.PersonName;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface Transportation extends IDomainObject<Long> {

	Date transportationDate();
	
	PersonName person();
	Vehicle vehicle();
	
	List<TransportationUnit> units();

}