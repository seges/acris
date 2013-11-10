package sk.seges.acris.domain.server.domain.base;

import java.sql.Blob;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import sk.seges.acris.domain.server.domain.api.ContentBlobData;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.server.domain.api.ContentPkData;
import sk.seges.acris.site.server.domain.base.ContentPkBase;
import sk.seges.acris.site.shared.domain.api.ContentForUtils;

public class ContentBase implements ContentData, ContentBlobData, ContentForUtils {

	private static final long serialVersionUID = 1744660288284574333L;

	private ContentPkBase id;

	private String title;

	private String keywords;

	private String description;

	private String label;

	private String stylePrefix;

	private String niceUrl;

	private String pageName;

	private String token;

	private Integer index;

	private String ref;

	private List<ContentBase> subContents = new LinkedList<ContentBase>();

	private ContentBase parent;

	private Date created;

	private Date modified;

	private String position;

	private String group = "";

	private Boolean defaultlyLoaded;

	private String defaultStyleClass;

	private String menuItems;

	private String decoration;

	private String params;

	private String serverParams;
	
	private Integer version = 0;

	private Boolean hasChildren = false;

	private Blob content;

	@Override
	public ContentPkBase getId() {
		return id;
	}
	
	@Override
	public Long getIdForACL() {
		return Long.valueOf(getId().toString());
	}
	
	public void setIdForACL(Long idForACL) {
		//TODO: DO something here
	}
	
	@Override
	public void setId(ContentPkData id) {
		this.id = (ContentPkBase) id;
	}

	@Override
	public String getWebId() {
		return this.id != null ? this.id.getWebId() : null;
	}

	@Override
	public void setWebId(String webId) {
		if (this.id != null) {
			id.setWebId(webId);
		}
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getKeywords() {
		return keywords;
	}

	@Override
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getNiceUrl() {
		return niceUrl;
	}

	@Override
	public void setNiceUrl(String niceUrl) {
		this.niceUrl = niceUrl;
	}

	@Override
	public String getPageName() {
		return pageName;
	}

	@Override
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public List<ContentBase> getSubContents() {
		return subContents;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setSubContents(List<? extends ContentData> subContents) {
		this.subContents = (List<ContentBase>) subContents;
	}

	@Override
	public ContentBase getParent() {
		return parent;
	}

	@Override
	public void setParent(ContentData parent) {
		this.parent = (ContentBase) parent;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public Integer getIndex() {
		return index;
	}

	@Override
	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public String getRef() {
		return ref;
	}

	@Override
	public void setRef(String ref) {
		this.ref = ref;
	}

	@Override
	public String getStylePrefix() {
		return stylePrefix;
	}

	@Override
	public void setStylePrefix(String stylePrefix) {
		this.stylePrefix = stylePrefix;
	}

	@Override
	public Date getCreated() {
		return created;
	}

	@Override
	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public Date getModified() {
		return modified;
	}

	@Override
	public void setModified(Date modified) {
		this.modified = modified;
	}

	@Override
	public String getPosition() {
		return position;
	}

	@Override
	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public Boolean getDefaultlyLoaded() {
		return defaultlyLoaded;
	}

	@Override
	public void setDefaultlyLoaded(Boolean defaultlyLoaded) {
		this.defaultlyLoaded = defaultlyLoaded;
	}

	@Override
	public String getDefaultStyleClass() {
		return defaultStyleClass;
	}

	@Override
	public void setDefaultStyleClass(String defaultStyleClass) {
		this.defaultStyleClass = defaultStyleClass;
	}

	@Override
	public String getMenuItems() {
		return menuItems;
	}

	@Override
	public void setMenuItems(String menuItems) {
		this.menuItems = menuItems;
	}

	@Override
	public String getDecoration() {
		return decoration;
	}

	@Override
	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}

	@Override
	public Boolean getHasChildren() {
		return hasChildren;
	}

	@Override
	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@Override
	public Integer getVersion() {
		return version;
	}

	@Override
	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String getParams() {
		return params;
	}

	@Override
	public void setParams(String params) {
		this.params = params;
	}
	
	@Override
	public String getServerParams() {
		return serverParams;
	}
	
	@Override
	public void setServerParams(String serverParams) {
		this.serverParams = serverParams;
	}

	@Override
	public Blob getContent() {
		return content;
	}

	@Override
	public void setContent(Blob content) {
		this.content = content;
	}
}
