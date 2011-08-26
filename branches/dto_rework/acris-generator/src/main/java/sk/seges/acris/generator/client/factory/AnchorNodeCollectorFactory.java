package sk.seges.acris.generator.client.factory;

import sk.seges.acris.generator.client.collector.AnchorNodeCollector;
import sk.seges.acris.generator.client.collector.api.NodeCollector;
import sk.seges.acris.generator.client.factory.api.NodeCollectorFactory;

public class AnchorNodeCollectorFactory implements NodeCollectorFactory {

	@Override
	public NodeCollector create() {
		return new AnchorNodeCollector();
	}
}
