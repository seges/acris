package sk.seges.acris.generator.server.processor.post.alters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.tags.LinkTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.utils.AnchorUtils;
import sk.seges.acris.generator.shared.domain.GeneratorToken;

public class NiceURLLinkAlterPostProcessor extends AbstractAlterPostProcessor {

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof LinkTag) {
			String link = ((LinkTag)node).getLink();
			return isInternal(link) || !isUrl(link);
		}
		return false;
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		LinkTag linkNode = (LinkTag)node;
		linkNode.setLink(AnchorUtils.getAnchorTargetHref(getLink(linkNode.getLink(), generatorEnvironment), generatorEnvironment));
		
		return true;
	}

	private boolean isInternal(String link) {
		return link.startsWith("#");
	}
	
	private boolean isUrl(String link) {
		String regex = "^(https?://)?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
	        + "(([0-9]{1,3}\\.){3}[0-9]{1,3}|([0-9a-z_!~*'()-]+\\.)*"
	        + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\.[a-z]{2,6})"
	        + "(:[0-9]{1,4})?((/?)|(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";	
		
		//String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(link);
        return matcher.matches();
	}
	
	protected String getLink(String link, GeneratorEnvironment generatorEnvironment) {
		GeneratorToken generatorToken = generatorEnvironment.getGeneratorToken();
		
		if (isInternal(link)) {
			if (generatorToken.isDefaultToken() && link.toLowerCase().equals("#" + generatorToken.getNiceUrl().toLowerCase())) {
				return "";
			}
			return link.substring(1);
		}

		return link;
	}
}