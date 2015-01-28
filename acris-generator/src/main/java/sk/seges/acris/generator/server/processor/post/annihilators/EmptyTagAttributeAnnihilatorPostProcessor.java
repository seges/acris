package sk.seges.acris.generator.server.processor.post.annihilators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Tag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.post.alters.AbstractAlterPostProcessor;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public class EmptyTagAttributeAnnihilatorPostProcessor extends AbstractAlterPostProcessor {

	@Override
    public OfflineMode getOfflineMode() {
        return OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof Tag) {
			Tag tag = (Tag)node;
			return tag.getAttributesEx() != null && tag.getAttributesEx().size() > 1;
		}
		return false;
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		Tag tag = (Tag)node;
	
		List<String> attributesToRemove = new ArrayList<String>();
		
		@SuppressWarnings("unchecked")
		Iterator<Attribute> iterator = tag.getAttributesEx().iterator();
		iterator.next();
		while (iterator.hasNext()) {
			Attribute attribute = iterator.next();
			if (attribute.getName() != null && attribute.getName().length() > 0 && attribute.getValue() != null && attribute.getValue().length() == 0) {
				attributesToRemove.add(attribute.getName());
			}
		}

		for (String attribute: attributesToRemove) {
			tag.removeAttribute(attribute);
		}
		
		return true;
	}
}