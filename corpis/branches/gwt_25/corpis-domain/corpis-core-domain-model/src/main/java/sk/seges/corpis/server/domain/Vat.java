package sk.seges.corpis.server.domain;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Vat extends IMutableDomainObject<Short> {

	Short vat();
	
	Date validFrom();

}