package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.site.server.domain.jpa.JpaWebSites;
import sk.seges.acris.site.shared.domain.api.SiteType;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

/**
 * @author psloboda
 * 
 */
@TransferObjectMapping(domainClass = JpaWebSites.class)
@Mapping(Mapping.MappingType.EXPLICIT)
@GenerateHashcode(generate = false)
@GenerateEquals(generate = false)
public interface WebSitesDTOConfiguration {
	void id();

	void domain();
	void language();
	void type();
	void webId();

}
