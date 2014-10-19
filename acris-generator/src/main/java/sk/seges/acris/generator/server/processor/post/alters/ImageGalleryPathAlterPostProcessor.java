package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.post.annihilators.AcrisInlineScriptAnnihilatorPostProcessor;

public class ImageGalleryPathAlterPostProcessor extends AbstractPathAlterPostProcessor {

	private static final String SKIN_DIR = "skinDir";
	private static final String GALLERY_IDENTIFIER = "myGalleries";

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	protected void setPath(Node node, String path) {
				
		for (Node childNode : node.getChildren().toNodeArray()) {
			if (childNode instanceof TagNode) {
				node = childNode;
				break;
			}
		}
		String html = node.toHtml();
		
		int index = html.indexOf(SKIN_DIR);
		int paramStartIndex = html.indexOf("'", index);
		int paramEndIndex = html.indexOf("'", paramStartIndex + 1);
		
		node.getParent().getChildren().add(new TextNode(html.substring(0, paramStartIndex + 1) + path + html.substring(paramEndIndex)));
		node.getParent().getChildren().remove(node);
	}

	@Override
	protected String getPath(Node node) {
		String html = node.toHtml();
		
		int index = html.indexOf(SKIN_DIR);
		
		if (index == -1) {
			return null;
		}
		
		html = html.substring(index + SKIN_DIR.length());
		
		int colon = html.indexOf(":");
		index = html.indexOf("'");
		
		if (colon > index || index == -1) {
			return null;
		}
		
		html = html.substring(index + 1);

		index = html.indexOf("'");

		if (index == -1) {
			return null;
		}
		
		return html.substring(0, index);
	}

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof Div) {
			String styleClass = ((Div)node).getAttribute(CLASS_ATTRIBUTE_NAME);
			if (styleClass == null || !styleClass.toLowerCase().equals(AcrisInlineScriptAnnihilatorPostProcessor.ACRIS_INLINE_SCRIPTS)) {
				return false;
			}

			String html = node.toHtml();
			
			int index = html.indexOf(SKIN_DIR);
			
			if (index == -1) {
				return false;
			}

			return (html.indexOf(GALLERY_IDENTIFIER) != -1);
		}
		
		return false;
	}
}
