package sk.seges.acris.generator.server.processor.post.alters;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.htmlparser.Node;

public abstract class AbstractPathPostProcessor extends AbstractContentInfoPostProcessor {

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
		
		URI url;
		try {
			url = new URI(path);
		} catch (URISyntaxException e) {
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
	public boolean process(Node node) {
		String niceUrl = generatorToken.getNiceUrl(); 
		if (File.separatorChar != '/')
			niceUrl = niceUrl.replace(File.separatorChar, '/');

		//count number of directories in the path. If the niceurl/token is
		//en/project than there are 2 directories: en and project so we
		//have to add ../../ prefix into the path
		int count = niceUrl.split("/").length + 1;
		
		if (count <= 1) {
			return true; //no special processing necessary 
		}
		
		String pathPrefix = "";
		
		for (int i = 1; i < count; i++) {
			pathPrefix = pathPrefix + "../";
		}
		
		setPath(node, pathPrefix + getPath(node));
		
		return true;
	}
	
	protected abstract void setPath(Node node, String path);
	protected abstract String getPath(Node node);
}
