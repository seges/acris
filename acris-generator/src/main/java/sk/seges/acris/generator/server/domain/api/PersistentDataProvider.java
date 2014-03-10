package sk.seges.acris.generator.server.domain.api;

import sk.seges.corpis.shared.domain.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;

public interface PersistentDataProvider extends IMutableDomainObject<String>, HasWebId {

	String getContent();
	void setContent(String content);

	String getAlias();
	void setAlias(String alias);
}