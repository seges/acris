package sk.seges.acris.generator.client.collector.api;

import sk.seges.acris.generator.client.context.api.GeneratorClientEnvironment;

import com.google.gwt.dom.client.Element;

public interface NodeCollector {

	void collect(Element rootPanel, GeneratorClientEnvironment generatorClientEnvironment);

}