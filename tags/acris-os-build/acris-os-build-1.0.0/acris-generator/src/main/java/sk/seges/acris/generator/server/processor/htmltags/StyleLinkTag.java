package sk.seges.acris.generator.server.processor.htmltags;

import org.htmlparser.nodes.TagNode;

public class StyleLinkTag extends TagNode {

	private static final long serialVersionUID = 3252145833444936273L;

	/**
	 * The set of names handled by this tag.
	 */
	private static final String[] mIds = new String[] { "LINK" };

	public String[] getIds() {
		return (mIds);
	}

	public String getRel() {
		return (getAttribute("REL"));
	}

	public String getType() {
		return (getAttribute("TYPE"));
	}

	public String getHref() {
		return (getAttribute("HREF"));
	}

	public void setHref(String href) {
		setAttribute("HREF", href);
	}
}