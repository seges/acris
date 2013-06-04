package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.site.server.domain.jpa.JpaFTPWebSettings;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = JpaFTPWebSettings.class)
public interface FTPWebSettingsDTOConfiguration {}
