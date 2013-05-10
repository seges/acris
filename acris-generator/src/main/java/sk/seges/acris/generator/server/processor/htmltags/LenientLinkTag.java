package sk.seges.acris.generator.server.processor.htmltags;

import org.htmlparser.tags.LinkTag;

public class LenientLinkTag extends LinkTag {

	private static final long serialVersionUID = -5679666217206476720L;

	/**
	 * The set of tag names that indicate the end of this tag.
	 */
	private static final String[] mEnders = new String[] { "A", "TD", "TR", "FORM", "LI" };

	/**
	 * The set of end tag names that indicate the end of this tag.
	 */
	private static final String[] mEndTagEnders = new String[] { "TD", "TR", "FORM", "LI", "BODY", "HTML" };

	@Override
	public String[] getEndTagEnders() {
		return mEndTagEnders;
	}

	@Override
	public String[] getEnders() {
		return mEnders;
	}

}
