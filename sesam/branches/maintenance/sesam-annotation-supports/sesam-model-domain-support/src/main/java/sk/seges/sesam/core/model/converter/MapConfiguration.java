package sk.seges.sesam.core.model.converter;

import java.util.Map;

import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.shared.model.converter.common.MapConverter;

@TransferObjectMapping(domainInterface = Map.class, dtoInterface = Map.class, converter = MapConverter.class)
public interface MapConfiguration { }
