package sk.seges.corpis.appscaffold.jpamodel.pap;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

/**
 * @author ladislav.gazo
 */
@DomainInterface
public interface RoleModel {
	Object id();
	String name();
	String description();
}
