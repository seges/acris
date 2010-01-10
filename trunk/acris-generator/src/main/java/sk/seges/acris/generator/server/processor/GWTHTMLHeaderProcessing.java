package sk.seges.acris.generator.server.processor;

import org.htmlparser.util.NodeList;


public class GWTHTMLHeaderProcessing extends HTMLHeaderProcessing {
	private static final String GWT_PROPERTY_NAME = "gwt:property";
	private static final String LOCALE_PROPERTY_VALUE ="locale=";

	public GWTHTMLHeaderProcessing(final String filename) {
		super(filename);
	}

	public String readHeaderFromFile(String keywords, String description, String title, String language) {
		NodeList nodes = super.readHeaderNodeFromFile(keywords, description, title);
		
		if (nodes == null) {
			return "";
		}

		addMetaTag(GWT_PROPERTY_NAME, nodes, LOCALE_PROPERTY_VALUE + language);
		String html = nodes.toHtml();
		return html;
	}
}
