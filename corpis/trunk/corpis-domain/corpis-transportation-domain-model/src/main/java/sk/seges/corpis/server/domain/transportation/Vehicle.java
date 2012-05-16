package sk.seges.corpis.server.domain.transportation;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface Vehicle extends IDomainObject<String> {}