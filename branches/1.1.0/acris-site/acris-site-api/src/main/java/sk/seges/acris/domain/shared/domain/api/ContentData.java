package sk.seges.acris.domain.shared.domain.api;

import sk.seges.corpis.shared.domain.api.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;

public interface ContentData<T> extends IMutableDomainObject<T>, HasWebId {

	String getKeywords();

	void setKeywords(String keywords);

	String getDescription();

	void setDescription(String description);

	String getTitle();

	void setTitle(String title);

	String getNiceUrl();

	void setNiceUrl(String niceUlr);

	String getContentDetached();

	void setContentDetached(String content);

	boolean isDefaultContent();

	void setDefaultContent(boolean isDefaultContent);
}