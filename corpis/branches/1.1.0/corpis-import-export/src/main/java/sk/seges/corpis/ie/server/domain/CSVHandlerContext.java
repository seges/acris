/**
 * 
 */
package sk.seges.corpis.ie.server.domain;

/**
 * @author ladislav.gazo
 */
public class CSVHandlerContext extends CopyOfCSVHandlerContext {
	private static final String WEB_ID = "webId";
	private static final String LANGUAGE = "language";
	private static final String PARENT_CONTENT_ID = "parentContentId";
	private static final String TEMPLATE_ID = "templateId";

	public CSVHandlerContext(int row, String webId, String language, Long parentContentId, Long templateId) {
		super();
		contextMap.put(WEB_ID, webId);
		contextMap.put(LANGUAGE, language);
		contextMap.put(PARENT_CONTENT_ID, parentContentId);
		contextMap.put(TEMPLATE_ID, templateId);
	}

	public String getWebId() {
		return get(WEB_ID);
	}

	public String getLanguage() {
		return get(LANGUAGE);
	}

	public Long getParentContentId() {
		return get(PARENT_CONTENT_ID);
	}

	public Long getTemplateId() {
		return get(TEMPLATE_ID);
	}

	@Override
	public String toString() {
		return "CSVHandlerContext [language=" + getLanguage() + ", parentContentId=" + getParentContentId()
				+ ", row=" + getRow() + ", templateId=" + getTemplateId() + ", webId=" + getWebId() + "]";
	}
}
