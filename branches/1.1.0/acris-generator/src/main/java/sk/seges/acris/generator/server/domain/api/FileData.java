package sk.seges.acris.generator.server.domain.api;

import sk.seges.sesam.domain.IMutableDomainObject;

public interface FileData extends IMutableDomainObject<Long> {

	String getpath();

	void setPath(String path);

	String getContent();

	void setContent(String content);
}
