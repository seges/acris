package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.domain.shared.domain.api.ContentPkData;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = ContentPkData.class, configuration = ContentPkDTOConfiguration.class)
public interface ContentPkDataDTOConfiguration {
}