package sk.seges.acris.domain.server.domain.api;

import java.sql.Blob;

public interface ContentBlobData {

	Blob getContent();

	void setContent(Blob content);
}
