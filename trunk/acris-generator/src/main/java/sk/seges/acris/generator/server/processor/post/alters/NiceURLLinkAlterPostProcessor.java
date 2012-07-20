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

	public static final Pattern WEB  = Pattern.compile(
	        new StringBuilder()
	                .append("((?:(http|https|Http|Https|rtsp|Rtsp):")
	                .append("\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)")
	                        .append("\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_")
	                        .append("\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?")
	                        .append("((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+")   // named host
	                        .append("(?:")   // plus top level domain
	                        .append("(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])")
	                        .append("|(?:biz|b[abdefghijmnorstvwyz])")
	                        .append("|(?:cat|com|coop|c[acdfghiklmnoruvxyz])")
	                        .append("|d[ejkmoz]")
	                        .append("|(?:edu|e[cegrstu])")
	                        .append("|f[ijkmor]")
	                        .append("|(?:gov|g[abdefghilmnpqrstuwy])")
	                        .append("|h[kmnrtu]")
	                        .append("|(?:info|int|i[delmnoqrst])")
	                        .append("|(?:jobs|j[emop])")
	                        .append("|k[eghimnrwyz]")
	                        .append("|l[abcikrstuvy]")
	                        .append("|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])")
	                        .append("|(?:name|net|n[acefgilopruz])")
	                        .append("|(?:org|om)")
	                        .append("|(?:pro|p[aefghklmnrstwy])")
	                        .append("|qa")
	                        .append("|r[eouw]")
	                        .append("|s[abcdeghijklmnortuvyz]")
	                        .append("|(?:tel|travel|t[cdfghjklmnoprtvwz])")
	                        .append("|u[agkmsyz]")
	                        .append("|v[aceginu]")
	                        .append("|w[fs]")
	                        .append("|y[etu]")
	                        .append("|z[amw]))")
	                        .append("|(?:(?:25[0-5]|2[0-4]") // or ip address
	                        .append("[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]")
	                        .append("|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]")
	                        .append("[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}")
	                        .append("|[1-9][0-9]|[0-9])))")
	                        .append("(?:\\:\\d{1,5})?)") // plus option port number
	                        .append("(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~")  // plus option query params
	                        .append("\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?")
	                        .append("(?:\\b|$)").toString()
	                );

	private boolean isUrl(String link) {
		return WEB.matcher(link).matches();
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