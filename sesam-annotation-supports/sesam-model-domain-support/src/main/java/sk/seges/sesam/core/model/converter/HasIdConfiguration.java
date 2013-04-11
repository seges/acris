package sk.seges.sesam.core.model.converter;

import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.shared.domain.api.HasId;

@TransferObjectMapping(domainInterface = IDomainObject.class, dtoInterface = HasId.class, generateConverter = false)
public interface HasIdConfiguration {}