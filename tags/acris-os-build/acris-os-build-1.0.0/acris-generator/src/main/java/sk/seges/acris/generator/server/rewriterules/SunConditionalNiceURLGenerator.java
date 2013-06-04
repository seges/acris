package sk.seges.acris.generator.server.rewriterules;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Qualifier(value="sun-pattern-cond")
public class SunConditionalNiceURLGenerator extends AbstractNiceURLGenerator {

	protected String getDefaultRewriteRule() {
		return "<If not restarted and $uri =~ \"^/\" and $uri !~ \"^/.+\">" + NEW_LINE
				+ "		NameTrans fn=\"restart\" uri=\"\\\"" + NEW_LINE + "</If>"+NEW_LINE;
	}

	
	protected String getRewriteRule(String fromURL, String toURL) {
		return   "<ElseIf not restarted and $uri =~ \"^" + fromURL + "\">" + NEW_LINE
			   + "		NameTrans fn=\"restart\" uri=\"" + toURL + "\"" + NEW_LINE + "</ElseIf>"+NEW_LINE;
	}

	protected String getFinalRewriteRule() {
		return NEW_LINE;
	}
}
