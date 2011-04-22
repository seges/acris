package sk.seges.acris.generator.server.processor.utils;

import java.util.HashSet;
import java.util.Set;

import sk.seges.acris.generator.server.manager.api.OfflineWebSettings;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public class PostProcessorActivator {
	
	protected Set<String> inactiveProcessors;
	protected Set<String> inactiveIndexProcessors;
	
	public PostProcessorActivator(OfflineWebSettings offlineWebSettings) {
		this.inactiveProcessors = offlineWebSettings.getInactiveProcessors();
		if (this.inactiveProcessors == null) {
			this.inactiveProcessors = new HashSet<String>();
		}
		this.inactiveIndexProcessors = offlineWebSettings.getInactiveIndexProcessors();
		if (this.inactiveIndexProcessors == null) {
			this.inactiveIndexProcessors = new HashSet<String>();
		}
	}
	
	public boolean isActive(AbstractElementPostProcessor postProcessor, boolean index) {

		Set<String> processors = index ? inactiveIndexProcessors : inactiveProcessors;

		for (String inactiveProcessor: processors) {
			if (inactiveProcessor.equals(postProcessor.getClass().getSimpleName())) {
				return false;
			}
		}

		return true;
	}
}