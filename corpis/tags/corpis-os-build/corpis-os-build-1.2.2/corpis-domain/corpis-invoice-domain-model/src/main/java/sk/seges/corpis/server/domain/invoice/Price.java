package sk.seges.corpis.server.domain.invoice;

import java.io.Serializable;
import java.math.BigDecimal;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
@BaseObject
public interface Price extends Serializable {

	BigDecimal value();
	Currency currency();
}