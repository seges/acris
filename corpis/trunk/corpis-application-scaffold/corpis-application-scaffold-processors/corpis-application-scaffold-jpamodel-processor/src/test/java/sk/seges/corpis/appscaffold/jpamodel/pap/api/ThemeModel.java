package sk.seges.corpis.appscaffold.jpamodel.pap.api;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.appscaffold.shared.annotation.domain.BusinessKey;

/**
 * @author ladislav.gazo
 */
@DomainInterface
public interface ThemeModel<T> {
	T id();
	@BusinessKey
	String name();
	@BusinessKey
	String webId();
}
