package sk.seges.acris.generator.server.processor.post.alters;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.htmlparser.Node;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.utils.AnchorUtils;

public abstract class AbstractPathAlterPostProcessor extends AbstractAlterPostProcessor {

	private static final Logger log = Logger.getLogger(AbstractPathAlterPostProcessor.class);

	protected boolean compareIgnoreCaseNullSafe(String text1, String text2) {
		if (text1 == null && text2 == null) {
			return true;
		}

		if (text1 == null || text2 == null) {
			return false;
		}

		return (text1.trim().toLowerCase().equals(text2.trim().toLowerCase()));
	}

	protected boolean isPathRelative(String path) {

		if (path == null) {
			log.warn("Checking for null path. Probably invalid HTML tag is processed.");
			return false;
		}

		URI url;
		try {
			url = new URI(path);
		} catch (URISyntaxException e) {
			if (log.isDebugEnabled()) {
				log.debug("Invalid path " + path + ". Considering it as relative.");
			}
			return true;
		}
		boolean result = url.isAbsolute();

		if (result) {
			return false;
		}

		//if doesn't contains scheme maybe its starting with www identifier
		return !path.toLowerCase().startsWith("www");
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {

		String pathPrefix = AnchorUtils.getRelativePrefix(generatorEnvironment.getGeneratorToken());
		
		//no special processing required
		if (pathPrefix.length() == 0) {
			return true;
		}

		setPath(node, pathPrefix + getPath(node));

		return true;
	}

	protected abstract void setPath(Node node, String path);

	protected abstract String getPath(Node node);
}