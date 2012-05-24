package sk.seges.acris.generator.shared.domain.api;

import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;

public interface PersistentDataProvider extends IMutableDomainObject<String>, HasWebId {

	String getContent();

	void setContent(String content);
}