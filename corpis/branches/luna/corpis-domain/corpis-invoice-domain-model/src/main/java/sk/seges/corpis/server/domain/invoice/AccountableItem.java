package sk.seges.corpis.server.domain.invoice;

import java.io.Serializable;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasDescription;

@DomainInterface
@BaseObject
public interface AccountableItem extends HasPrice, HasDescription, Serializable {

	Float amount();
	Unit unit();
	Vat vat();
}
