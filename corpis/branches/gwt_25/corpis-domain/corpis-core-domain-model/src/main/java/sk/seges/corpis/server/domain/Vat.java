package sk.seges.corpis.server.domain;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
@BaseObject
public interface Vat {

	Short vat();
	
	Date validFrom();

}