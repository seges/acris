package sk.seges.acris.generator.server.processor.post.appenders;

import java.util.Vector;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.lexer.PageAttribute;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.alters.AbstractPathAlterPostProcessor;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.ScriptUtils;

public class AcrisExternalScriptAppenderPostProcessor extends AbstractAppenderPostProcessor {

	public static final String EXTERNAL_SCRIPTS_CLASS_NAME = "acris-external-scripts";
	
	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof Div) {
			String styleClass = ((Div)node).getAttribute(CLASS_ATTRIBUTE_NAME);
			return (styleClass != null && styleClass.toLowerCase().contains(EXTERNAL_SCRIPTS_CLASS_NAME));
		}
		
		return false;
	}

	protected HeadTag constructHeadTag() {
		HeadTag headNode = new HeadTag();
		headNode.setTagName(headNode.getTagName().toLowerCase());
		
		HeadTag endHeadTag = new HeadTag();
		endHeadTag.setTagName("/" + endHeadTag.getTagName().toLowerCase());
		
		headNode.setEndTag(endHeadTag);
		
		return headNode;
	}
	
	protected boolean hasScriptBySource(HeadTag head, String src) {
		
		if (src == null) {
			return false;
		}
		
		NodeList children = head.getChildren();
		
		if (children == null) {
			return false;
		}
		
		int size = children.size();
		
		for (int i = 0; i < size; i++) {
			Node elementAt = children.elementAt(i);
			if (elementAt instanceof ScriptTag) {
				String path = ScriptUtils.getPath((ScriptTag)elementAt);

				if (path != null && path.toLowerCase().equals(src.toLowerCase())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {

		HeadTag headNode = generatorEnvironment.getNodesContext().getHeadNode();
		
		if (headNode == null) {
			generatorEnvironment.getNodesContext().setHeadNode(headNode = constructHeadTag());
			NodesUtils.prependChild(generatorEnvironment.getNodesContext().getHtmlNode(), headNode);			
		}
		
		NodeList children = node.getChildren();
		int size = children.size();
		
		for (int i = 0; i < size; i++) {
			Node scriptTag = children.elementAt(i);
			
			if (scriptTag instanceof ScriptTag) {
				String path = ScriptUtils.getPath(((ScriptTag)scriptTag));
				
				if (!hasScriptBySource(headNode, path)) {

                    boolean contains = false;

                    if (AbstractPathAlterPostProcessor.isPathRelative(path)) {
                        String relativePath = AbstractPathAlterPostProcessor.getRelativePath(path, generatorEnvironment);
                        if (relativePath != null) {
                            contains = hasScriptBySource(headNode, relativePath);
                        }
                    }

                    if (!contains) {
                        TextNode paddingNode = new TextNode("\t");
                        NodesUtils.appendChild(headNode, paddingNode);
                        NodesUtils.appendChild(headNode, scriptTag);
                    }
				}
			}
		}
		
		return true;
	}
	
	private Tag cloneTag(Tag tag) {
		if (tag == null) {
			return null;
		}
		
		if (tag instanceof ScriptTag) {
			return cloneScriptTag((ScriptTag) tag);
		}
		
		throw new RuntimeException("Unsupported tag: " + tag.toHtml());
	}
	
	@SuppressWarnings("unchecked")
	private ScriptTag cloneScriptTag(ScriptTag scriptTag) {
		ScriptTag result = new ScriptTag();
		result.setAttributesEx(new Vector<Object>());
		for (Object attribute: scriptTag.getAttributesEx()) {
			result.getAttributesEx().add(cloneAttribute((Attribute)attribute));
		}
		
		result.setEmptyXmlTag(scriptTag.isEmptyXmlTag());
		result.setScriptCode(scriptTag.getScriptCode());
		result.setEndTag(cloneTag(scriptTag.getEndTag()));
		
		return result;
	}
	
	private Attribute cloneAttribute(Attribute attribute) {
		Attribute result;
		if (attribute instanceof PageAttribute) {
			PageAttribute pageAttribute = new PageAttribute();
			
			pageAttribute.setNameEndPosition(((PageAttribute)attribute).getNameEndPosition());
			pageAttribute.setNameStartPosition(((PageAttribute)attribute).getNameStartPosition());

			pageAttribute.setPage(((PageAttribute)attribute).getPage());
			pageAttribute.setValueEndPosition(((PageAttribute)attribute).getValueEndPosition());
			pageAttribute.setValueStartPosition(((PageAttribute)attribute).getValueStartPosition());

			result = pageAttribute;
		} else {
			result = new Attribute();
		}

		result.setName(attribute.getName());
		result.setAssignment(attribute.getAssignment());
		result.setQuote(attribute.getQuote());
		result.setRawValue(attribute.getRawValue()); //is this necessary?
		result.setValue(attribute.getValue());
		
		return result;
	}
}