package sk.seges.corpis.server.domain.product;

import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.shared.domain.product.EAssignmentTagsType;
import sk.seges.corpis.shared.domain.product.ESystemTagsType;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Tag extends IMutableDomainObject<Long> {

	List<TagName> tagNames();
	
	Tag parent();
	
	String webId();
	
	Long priority();

	ESystemTagsType type();
	
	EAssignmentTagsType assignmentType();
}