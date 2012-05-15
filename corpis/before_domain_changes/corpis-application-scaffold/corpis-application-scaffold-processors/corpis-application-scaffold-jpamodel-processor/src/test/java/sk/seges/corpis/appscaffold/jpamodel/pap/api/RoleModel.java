package sk.seges.corpis.appscaffold.jpamodel.pap.api;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

/**
 * @author ladislav.gazo
 */
@DomainInterface
public interface RoleModel<T> {
	T id();
	String name();
	String description();
}
