package sk.seges.acris.domain.server.domain.base;

import java.sql.Blob;

import sk.seges.acris.domain.server.domain.api.ContentBlobData;
import sk.seges.acris.domain.shared.domain.base.ContentBase;
import sk.seges.acris.domain.shared.domain.base.ContentPkBase;

public class ContentBlobBase extends ContentBase implements ContentBlobData<ContentPkBase> {

	private static final long serialVersionUID = 1744660288284574333L;

	private Blob content;

	@Override
	public Blob getContent() {
		return content;
	}

	@Override
	public void setContent(Blob content) {
		this.content = content;
	}
}
