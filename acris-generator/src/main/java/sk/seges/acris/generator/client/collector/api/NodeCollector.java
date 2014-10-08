package sk.seges.acris.generator.client.collector.api;

import com.google.gwt.dom.client.Element;
import sk.seges.acris.generator.client.context.api.GeneratorClientEnvironment;

public interface NodeCollector {

	void collect(Element rootPanel, GeneratorClientEnvironment generatorClientEnvironment);

}