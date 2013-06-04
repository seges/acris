package sk.seges.acris.generator.server.processor.htmltags;

import org.htmlparser.tags.CompositeTag;

public class NoScriptTag extends CompositeTag {

	private static final long serialVersionUID = 5326376533619157134L;

	/**
	 * The set of names handled by this tag.
	 */
	private static final String[] mIds = new String[] { "NOSCRIPT" };
    private static final String[] mEndTagEnders = new String[] {"BODY", "HTML"};

	public String[] getIds() {
		return (mIds);
	}
	
	@Override
	public String[] getEndTagEnders() {
		return mEndTagEnders;
	}
}