package sk.seges.acris.site.server.domain.api;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.site.shared.domain.api.SiteType;
import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasLanguage;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

/**
 * @author psloboda
 *
 */
@BeanWrapper
@MetaModel
@DomainInterface
@BaseObject
public interface WebSites extends IMutableDomainObject<Long>, HasWebId, HasLanguage{

	String webId();

	String domain();
	
	SiteType type();
	
	String rootDir();
}
