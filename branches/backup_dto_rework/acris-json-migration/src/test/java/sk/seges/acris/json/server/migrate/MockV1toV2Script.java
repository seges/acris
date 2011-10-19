package sk.seges.acris.json.server.migrate;

import org.codehaus.jackson.node.ObjectNode;

/**
 * @author ladislav.gazo
 *
 */
public class MockV1toV2Script extends JacksonTransformationScript<ObjectNode> {

	@Override
	public void process(ObjectNode node) {
		rename(node, "name", "title");
	}

}
