package sk.seges.acris.generator.server.processor.factory;

import org.htmlparser.Tag;


public class NodeFactory {
	
	public static <T extends Tag> T getTagWithClosing(Class<T> clazz) {

		T tag;
		T endTag;
		
		try {
			tag = (T)clazz.newInstance();
			endTag = (T)clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}

		endTag.setTagName("/" + tag.getTagName());
		tag.setEndTag(endTag);
		
		return tag;
	}
}
