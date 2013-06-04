package sk.seges.sesam.pap.model.model.api.domain;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.pap.model.model.api.dto.DtoTypeVariable;

public interface DomainTypeVariable extends DomainType, MutableTypeVariable {

	DtoTypeVariable getDto();

}