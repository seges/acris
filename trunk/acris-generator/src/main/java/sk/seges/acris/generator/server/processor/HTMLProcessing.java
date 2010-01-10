package sk.seges.acris.generator.server.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.PageAttribute;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import sk.seges.acris.io.StringFile;

public class HTMLProcessing {
	private final String filename;

	private static final String NAME_ATTRIBUTE_NAME = "name";
	private static final String CONTENT_ATTRIBUTE_NAME = "content";
	private static final String DESCRIPTION_TAG_NAME = "description";
	private static final String KEYWORDS_TAG_NAME = "keywords";
	private static final String HEAD_TAG_NAME = "head";

	private final Parser parser;

	private NodeFilter headFilter = new TagNameFilter(HEAD_TAG_NAME);

	public HTMLProcessing(final String filename) {
		this.filename = filename;
		try {
			if (filename.startsWith("http://")) {
				URL url;
				try {
					url = new URL(filename);
				} catch (MalformedURLException e) {
					throw new IllegalArgumentException("Entered URL is not valid URI", e);
				}
				URLConnection connection;
				try {
					connection = url.openConnection();
				} catch (IOException e) {
					throw new IllegalArgumentException("Unable to establish connection to the URL " + filename, e);
				}

				parser = new Parser(connection);
			} else {
				parser = new Parser(StringFile.getFileDescriptor(filename).getAbsolutePath());
			}
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	private String readTextFromURL(String filename) throws IOException {
		URL url;
		
		try {
			url = new URL(filename);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Unable to load file: " + filename);
		}

		if (url == null) {
			throw new RuntimeException("File or directory should exists: " + filename);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

		StringBuffer content = new StringBuffer();
		
		String line;

		while ((line = br.readLine()) != null) {
			content.append(line);
		}
	
		br.close();

		return content.toString();
	}
	
	private String readTextFromFile(String filename) throws IOException {

		final StringFile file = StringFile.getFileDescriptor(filename);

		return file.readTextFromFile();
	}

	public String readTextFromResource(String filename) {
		try {
			if (filename.startsWith("http://")) {
				return readTextFromURL(filename);
			} else {
				return readTextFromFile(filename);
			}			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	
	}
	
	public NodeList readHeaderNodeFromFile(String keywords, String description, String title) {
		String content = readTextFromResource(filename);

		if (content == null) {
			return null;
		}

		NodeList nodes;

		try {
			nodes = parser.parse(headFilter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}

		if (description != null) {
			replaceMetaTag(DESCRIPTION_TAG_NAME, nodes, description);
		}
		
		if (keywords != null) {
			replaceMetaTag(KEYWORDS_TAG_NAME, nodes, keywords);
		}
		
		if (title != null) {
			replaceTitle(nodes, title);
		}

		return nodes;
	}

	public String getDoctypeDefinition() {
		return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
	}

	public String getHtmlDefinition(final String lang) {
		return "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"" + lang + "\" xml:lang=\"" + lang + "\">";
	}

	private void replaceTitle(final NodeList nodes, final String title) {
		TitleTag node = getTitleNode();

		if (node != null) {
			removeRecursive(nodes, node);
		}

		TitleTag tag = new TitleTag();
		TextNode textNode = new TextNode(title);
		NodeList nodeList = new NodeList();
		nodeList.add(textNode);
		tag.setChildren(nodeList);

		TagNode tagNode = new TagNode();
		
		PageAttribute pa = new PageAttribute();
		pa.setName("/" + ((Attribute) tag.getAttributesEx().get(0)).getName());
		pa.setValue(null);
		Vector<PageAttribute> vector = new Vector<PageAttribute>();
		vector.add(pa);
		tagNode.setAttributesEx(vector);
		// tagNode.setAttribute(pa);
		tag.setEndTag(tagNode);
		
		nodes.elementAt(0).getChildren().add(tag);
	}

	private void replaceMetaTag(final String tagName, final NodeList nodes, final String contentValue) {
		MetaTag node = getMetaTagNode(tagName);

		if (node != null) {
			removeRecursive(nodes, node);
		}

		addMetaTag(tagName, nodes, contentValue);
	}

	public void addMetaTag(final String tagName, final NodeList nodes, final String contentValue) {
		HeadTag headTag = (HeadTag) nodes.elementAt(0);

		NodeList headChildNodes = headTag.getChildren();

		MetaTag tag = new MetaTag();
		tag.setAttribute(NAME_ATTRIBUTE_NAME, "\"" + tagName + "\"");
		tag.setAttribute(CONTENT_ATTRIBUTE_NAME, contentValue);

		headChildNodes.add(tag);
	}

	private boolean removeRecursive(final NodeList list, final Node node) {
		int index;
		boolean ret = false;

		if (list == null) {
			return ret;
		}

		if (-1 != (index = indexOf(list, node))) {
			list.remove(index);
			ret = true;
		} else if (list.size() > 0) {
			Node[] nodes = new Node[list.size()];
			list.copyToNodeArray(nodes);

			for (Node childNode : nodes) {
				if (removeRecursive(childNode.getChildren(), node)) {
					return true;
				}
			}
		}

		return (ret);
	}

	private int indexOf(NodeList nodes, Node searchNode) {
		MetaTag node;
		int loc = 0;

		final String searchNodeHtml = searchNode.toHtml();

		for (SimpleNodeIterator e = nodes.elements(); e.hasMoreNodes();) {
			Node currentNode = e.nextNode();

			if (currentNode instanceof MetaTag) {
				node = (MetaTag) currentNode;

				if (searchNode instanceof MetaTag) {
					String nodeName = node.getAttribute(NAME_ATTRIBUTE_NAME);
					String nodeContent = node.getAttribute(CONTENT_ATTRIBUTE_NAME);
					
					if (nodeName != null && nodeName.equalsIgnoreCase(((MetaTag) searchNode).getAttribute(NAME_ATTRIBUTE_NAME)) && 
						nodeContent != null && nodeContent.equalsIgnoreCase(((MetaTag) searchNode).getAttribute(CONTENT_ATTRIBUTE_NAME))) {
						return loc;
					}
				}
			} else {
				final String currentNodeHtml = currentNode.toHtml();

				if (currentNodeHtml.equalsIgnoreCase(searchNodeHtml)) {
					return loc;
				}
			}

			loc++;
		}
		return -1;
	}

	private HeadTag getHeadNode() {
		NodeList nodes;

		parser.reset();

		try {
			nodes = parser.parse(headFilter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}

		if (nodes.size() == 0) {
			return null;
		}

		return (HeadTag) nodes.elementAt(0);
	}

	private MetaTag getMetaTagNode(String name) {

		if (name == null) {
			return null;
		}

		HeadTag headTag = getHeadNode();

		NodeList metaTags = headTag.searchFor(MetaTag.class, true);

		SimpleNodeIterator nodesIterator = metaTags.elements();

		while (nodesIterator.hasMoreNodes()) {
			MetaTag metaTag = (MetaTag) nodesIterator.nextNode();
			if (name.equalsIgnoreCase(metaTag.getAttribute(NAME_ATTRIBUTE_NAME))) {
				return metaTag;
			}
		}

		return null;
	}

	private TitleTag getTitleNode() {
		HeadTag headTag = getHeadNode();

		NodeList titleTags = headTag.searchFor(TitleTag.class, true);

		if (titleTags.size() == 0) {
			return null;
		}

		return (TitleTag) titleTags.elementAt(0);
	}
}