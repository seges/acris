package sk.seges.corpis.pap.model.configuration;

import sk.seges.corpis.pap.model.dto.MockEntityPkDTO;
import sk.seges.corpis.pap.model.entity.MockEntityPk;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = MockEntityPk.class, dtoClass = MockEntityPkDTO.class)
@Mapping(MappingType.AUTOMATIC)
public interface MockContentPkConfiguration {}