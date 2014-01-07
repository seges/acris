package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.site.server.domain.jpa.JpaFTPWebSettings;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = JpaFTPWebSettings.class)
@Mapping(Mapping.MappingType.EXPLICIT)
@GenerateHashcode(generate = false)
@GenerateEquals(generate = false)
public interface FTPWebSettingsDTOConfiguration {

	void id();

}
