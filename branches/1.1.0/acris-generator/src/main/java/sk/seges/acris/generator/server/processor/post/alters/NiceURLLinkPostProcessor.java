package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.LinkTag;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class NiceURLLinkPostProcessor extends AbstractElementPostProcessor {

	public NiceURLLinkPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	@Override
	public boolean supports(Node node) {
		if (node instanceof LinkTag) {
			return (((LinkTag)node).getLink().startsWith("#"));
		}
		return false;
	}

	@Override
	public boolean process(Node node) {
		if (webSettings.getTopLevelDomain() == null) {
			((LinkTag)node).setLink("/" + ((LinkTag)node).getLink().substring(1));
		} else {
			String url = webSettings.getTopLevelDomain();
			if (!url.endsWith("/")) {
				url += "/";
			}
			((LinkTag)node).setLink(url + ((LinkTag)node).getLink().substring(1));
		}
		return true;
	}
}