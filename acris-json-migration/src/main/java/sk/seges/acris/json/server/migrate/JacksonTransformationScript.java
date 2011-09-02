package sk.seges.acris.json.server.migrate;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * @author ladislav.gazo
 *
 * @param <T> Root node type
 */
public abstract class JacksonTransformationScript<T extends JsonNode> {
	public void execute(T node) {
		process(node);
	}

	protected abstract void process(T node);
	
	protected void rename(ObjectNode parent, String srcField, String dstField) {
		JsonNode value = parent.path(srcField);
		parent.put(dstField, value);
		parent.remove(srcField);
	}
}
