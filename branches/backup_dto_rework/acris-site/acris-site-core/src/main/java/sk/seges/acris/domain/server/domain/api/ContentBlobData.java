package sk.seges.acris.domain.server.domain.api;

import java.sql.Blob;

import sk.seges.sesam.domain.IMutableDomainObject;

public interface ContentBlobData<T> extends IMutableDomainObject<T> {

	Blob getContent();

	void setContent(Blob content);
}
