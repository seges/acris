package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.site.server.domain.jpa.JpaWebSites;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

/**
 * @author psloboda
 * 
 */
@TransferObjectMapping(domainClass = JpaWebSites.class)
public interface WebSitesDTOConfiguration {}
