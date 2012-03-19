package sk.seges.sesam.pap.metadata.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sk.seges.sesam.model.metadata.strategy.api.ModelPropertyConverter;
import sk.seges.sesam.pap.metadata.model.MetaModelContext;

public class MetaCache {

	private Map<ModelPropertyConverter, Set<String>> classConstantsCache;
	
	public enum MetaElementType {
		TYPE, PROPERTY;
	}

	public MetaCache() {
		classConstantsCache = new HashMap<ModelPropertyConverter, Set<String>>();
	}
	
	public boolean isProcessed(MetaModelContext context, MetaElementType type) {
		switch (type) {
		case PROPERTY:
			Set<String> set = classConstantsCache.get(context.getConverter());
			if (set == null) {
				return false;
			}
			return set.contains(context.getPath());
		default:
			if (context.containsProcessingElement(context.getProcessingElement())) {
				return true;
			}
			if (context.containsProperty(context.getProperty())) {
				return true;
			}
			return false;
		}
	}
	
	public void setProcessed(MetaModelContext context) {
		
		Set<String> set = classConstantsCache.get(context.getConverter());
		
		if (set == null) {
			set = new HashSet<String>();
			classConstantsCache.put(context.getConverter(), set);
		}
		
		if (context.getProperty() != null) {
//			if (context.getPrefix() != null && context.getPrefix().length() > 0) {
//				set.add(context.getPrefix().substring(0, context.getPrefix().length() - 1));
//			}
//		} else {
			set.add(context.getPath());
		}
	}
}