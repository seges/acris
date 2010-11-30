package sk.seges.acris.generator.server.domain.api;

import sk.seges.sesam.domain.IMutableDomainObject;

public interface FileData extends IMutableDomainObject<String> {

	String getContent();

	void setContent(String content);
}
