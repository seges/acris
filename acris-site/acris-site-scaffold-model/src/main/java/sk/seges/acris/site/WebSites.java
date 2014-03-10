package sk.seges.acris.site;

import sk.seges.acris.site.shared.domain.api.SiteType;
import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.shared.domain.HasLanguage;
import sk.seges.corpis.shared.domain.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;

/**
 * @author psloboda
 *
 */
@DomainInterface
@BaseObject
interface WebSites extends IMutableDomainObject<Long>, HasWebId, HasLanguage{

	String webId();

	String domain();
	
	SiteType type();

	String rootDir();
}