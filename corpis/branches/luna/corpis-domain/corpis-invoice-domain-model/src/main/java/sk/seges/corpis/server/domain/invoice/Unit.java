package sk.seges.corpis.server.domain.invoice;

import java.io.Serializable;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
@BaseObject
public interface Unit extends Serializable {
	
	String labelKey();
	EUnitType type();
}