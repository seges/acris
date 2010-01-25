package sk.seges.acris.generator.server.rewriterules;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Qualifier(value="sun-pattern-cond")
public class SunConditionalNiceURLGenerator extends AbstractNiceURLGenerator {

	protected String getDefaultRewriteRule() {
//		return "<If not restarted and $uri =~ \"^/\" and $uri !~ \"^/.+\">" + NEW_LINE
//				+ "		NameTrans fn=\"restart\" uri=\"" + ModulesDefinition.INDEX_URL + "\"" + NEW_LINE + "</If>"+NEW_LINE;
		return null;
	}

	
	protected String getRewriteRule(String fromURL, String toURL) {
//		return   "<ElseIf not restarted and $uri =~ \"^" + ".*" + fromURL + "\">" + NEW_LINE
//			   + "		NameTrans fn=\"restart\" uri=\"" + toURL + "\"" + NEW_LINE + "</ElseIf>"+NEW_LINE;
		return null;
	}

	protected String getFinalRewriteRule() {
//		return "<ElseIf not restarted and $uri =~ \"^/(.*)$\" and $uri !~ \"^/" + ModulesDefinition.APP_NAME + "\" " +
//									 "and $uri !~ \"^/" + ModulesDefinition.SERVER_APP_NAME + "\" " +
//									 "and $uri !~ \"^/" + ModulesDefinition.ADMIN_APP_NAME + "\">" + NEW_LINE
//			 + "		NameTrans fn=\"restart\" uri=\"" + "/" + ModulesDefinition.APP_NAME + "/" + ModulesDefinition.MODULE_NAME + "/" + "$1\"" + NEW_LINE + "</ElseIf>"+NEW_LINE;
		return null;
	}
}
