package sk.seges.corpis.appscaffold.datainterface.pap;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

/**
 * @author ladislav.gazo
 */
@DomainInterface
public interface RoleModel {
	String name();
	String description();
}
