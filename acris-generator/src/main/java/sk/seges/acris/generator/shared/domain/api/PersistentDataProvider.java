package sk.seges.acris.generator.shared.domain.api;

import sk.seges.corpis.shared.domain.api.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;

public interface PersistentDataProvider extends IMutableDomainObject<String>, HasWebId {

	String getContent();

	void setContent(String content);
}