/**
 * 
 */
package sk.seges.corpis.ie.server.domain;

/**
 * @author ladislav.gazo
 */
public class CSVHandlerContext {
	private final int row;
	private final String webId;
	private final String language;
	private final Long parentContentId;
	private final Long templateId;

	public CSVHandlerContext(int row, String webId, String language, Long parentContentId, Long templateId) {
		super();
		this.row = row;
		this.webId = webId;
		this.language = language;
		this.parentContentId = parentContentId;
		this.templateId = templateId;
	}

	public int getRow() {
		return row;
	}

	public String getWebId() {
		return webId;
	}

	public String getLanguage() {
		return language;
	}

	public Long getParentContentId() {
		return parentContentId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	@Override
	public String toString() {
		return "CSVHandlerContext [language=" + language + ", parentContentId=" + parentContentId + ", row="
				+ row + ", templateId=" + templateId + ", webId=" + webId + "]";
	}
}
