package sk.seges.corpis.appscaffold.datainterface.pap;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

/**
 * @author ladislav.gazo
 */
@DomainInterface
public interface ThemeModel<T> {
	T id();
	String name();
	String webId();
}
