package sk.seges.acris.generator.server.rewriterules;


public class SunConditionalNiceURLGenerator extends AbstractNiceURLGenerator {

	public SunConditionalNiceURLGenerator(String redirectFilePath, Boolean redirectCondition, Boolean redirectSingleFile, String legacyRedirectFilePath,
			Boolean legacyRedirectSingleFile) {
		super(redirectFilePath, redirectCondition, redirectSingleFile, legacyRedirectFilePath, legacyRedirectSingleFile);
	}


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
