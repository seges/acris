package sk.seges.sesam.pap.model.model.api.dto;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.pap.model.model.api.domain.DomainTypeVariable;

public interface DtoTypeVariable extends DtoType, MutableTypeVariable {

	DomainTypeVariable getDomain();

}