package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = ContentData.class, configuration = ContentDTOConfiguration.class)
public interface ContentDataDTOConfiguration {
}