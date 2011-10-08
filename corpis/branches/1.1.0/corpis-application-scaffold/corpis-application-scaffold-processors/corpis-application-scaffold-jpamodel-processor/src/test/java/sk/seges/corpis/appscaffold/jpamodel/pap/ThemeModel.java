package sk.seges.corpis.appscaffold.jpamodel.pap;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.appscaffold.shared.annotation.domain.BusinessKey;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

/**
 * @author ladislav.gazo
 */
@MetaModel
@DomainInterface
public interface ThemeModel<T> {
	T id();
	@BusinessKey
	String name();
	@BusinessKey
	String webId();
}
