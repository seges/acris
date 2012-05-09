package sk.seges.corpis.server.domain;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
@BaseObject
public interface Name  {

	String language();
	
	String value();

}
