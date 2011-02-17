package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.TitleTag;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.site.shared.service.IWebSettingsService;

/**
 * Post processor modifies title in the header regarding the content
 * 
 * @author Peter Simun (simun@seges.sk)
 */
public class TitlePostProcessor extends AbstractContentMetaDataPostProcessor {

	public TitlePostProcessor(IWebSettingsService webSettingsService, ContentDataProvider contentInfoProvider) {
		super(webSettingsService, contentInfoProvider);
	}

	@Override
	public boolean process(Node node) {
		NodesUtils.setTitle((TitleTag) node, getContent().getTitle());
		return true;
	}

	@Override
	public boolean supports(Node node) {
		return (node instanceof TitleTag);
	}
}