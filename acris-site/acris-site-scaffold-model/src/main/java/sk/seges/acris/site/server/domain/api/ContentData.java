package sk.seges.acris.site.server.domain.api;

import java.util.Date;
import java.util.List;

import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@BeanWrapper
@MetaModel
public interface ContentData extends IMutableDomainObject<ContentPkData>, HasWebId {

	String getTitle();

	void setTitle(String title);

	String getKeywords();

	void setKeywords(String keywords);

	String getDescription();

	void setDescription(String description);

	String getNiceUrl();

	void setNiceUrl(String niceUrl);

	String getPageName();

	void setPageName(String pageName);

	String getToken();

	void setToken(String token);

	List<? extends ContentData> getSubContents();

	void setSubContents(List<? extends ContentData> subContents);

	ContentData getParent();

	void setParent(ContentData parent);

	String getLabel();

	void setLabel(String label);

	Integer getIndex();

	void setIndex(Integer index);

	String getRef();

	void setRef(String ref);

	String getStylePrefix();

	void setStylePrefix(String stylePrefix);

	Date getCreated();

	void setCreated(Date created);

	Date getModified();

	void setModified(Date modified);

	String getPosition();

	void setPosition(String position);

	String getGroup();

	void setGroup(String group);

	Boolean getDefaultlyLoaded();

	void setDefaultlyLoaded(Boolean defaultlyLoaded);

	String getDefaultStyleClass();

	void setDefaultStyleClass(String defaultStyleClass);

	String getMenuItems();

	void setMenuItems(String menuItems);

	String getDecoration();

	void setDecoration(String decoration);

	Boolean getHasChildren();

	void setHasChildren(Boolean hasChildren);

	Integer getVersion();

	void setVersion(Integer version);

	String getParams();

	void setParams(String params);
	
	String getServerParams();
	
	void setServerParams(String serverParams);
}