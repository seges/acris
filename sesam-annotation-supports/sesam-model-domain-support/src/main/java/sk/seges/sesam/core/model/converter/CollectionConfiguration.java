package sk.seges.sesam.core.model.converter;

import java.util.Collection;

import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.server.model.converter.common.CollectionConverter;

@TransferObjectMapping(domainInterface = Collection.class, dtoInterface = Collection.class, converter = CollectionConverter.class)
public interface CollectionConfiguration {}