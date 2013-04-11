package sk.seges.corpis.server.domain.invoice;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface Vat extends IDomainObject<Short> {

	Short vat();
	Date validFrom();

}