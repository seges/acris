package sk.seges.acris.domain.shared.domain.api;

import sk.seges.corpis.shared.domain.api.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;

public interface ContentPkData<T> extends IMutableDomainObject<T>, HasWebId {

	String getLanguage();

	void setLanguage(String language);
}
