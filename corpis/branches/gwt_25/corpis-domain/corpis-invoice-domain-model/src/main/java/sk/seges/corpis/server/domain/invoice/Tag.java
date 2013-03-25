package sk.seges.corpis.server.domain.invoice;

import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface Tag extends IDomainObject<Long> {

	List<TagName> tagNames();
	
	Tag parent();
	
	String webId();
	
	Long priority();

	Integer index();
}