package sk.seges.acris.json.server.migrate;

import java.util.Arrays;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.BooleanNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;

/**
 * Template class for all Jackson-based transformation scripts. Contains various
 * helper methods to ease writing scripts.
 * 
 * @author ladislav.gazo
 * 
 * @param <T>
 *            Root node type
 */
public abstract class JacksonTransformationScript<T extends JsonNode> {
	public void execute(T node) {
		process(node);
	}

	protected abstract void process(T node);

	/**
	 * Renames field in an object node.
	 * 
	 * @param parent
	 *            Object node where to rename the field.
	 * @param srcField
	 *            Original field name.
	 * @param dstField
	 *            Desired field name.
	 */
	protected void rename(ObjectNode parent, String srcField, String dstField) {
		JsonNode value = parent.path(srcField);
		parent.put(dstField, value);
		parent.remove(srcField);
	}

	/**
	 * Adds child node to a parent in case it does not exist already. New child
	 * is taken as a childPrototype parameter. In case it exists, nothing
	 * happens.
	 * 
	 * @param <N>
	 *            Type of the child node.
	 * @param parent
	 *            Parent node where to look for the field.
	 * @param field
	 *            Field name in object node.
	 * @param childPrototype
	 *            Prepared instance of a child node in case it doesn't exist.
	 * @return If the field exists in the parent, the node for the field is
	 *         returned, otherwise childPrototype is returned.
	 */
	@SuppressWarnings("unchecked")
	protected <N extends JsonNode> N addNonExistent(ObjectNode parent, String field, N childPrototype) {
		JsonNode child = parent.get(field);
		if (child != null && !childPrototype.getClass().getName().equals(child.getClass().getName())) {
			throw new RuntimeException("Field " + field + " exists and its class " + child.getClass().getName() + " is not same as expected " + childPrototype.getClass().getName() + ", cannot continue");
		} else if (child != null) {
			return (N) child;
		} else if (child == null) {
			parent.put(field, childPrototype);
		}

		return childPrototype;
	}

	/**
	 * @see {@link #addNonExistent}
	 */
	protected ObjectNode addNonExistentObjectNode(ObjectNode parent, String field) {
		return addNonExistent(parent, field, parent.objectNode());
	}

	/**
	 * @see {@link #addNonExistent}
	 */
	protected TextNode addNonExistentTextNode(ObjectNode parent, String field, String text) {
		return addNonExistent(parent, field, parent.textNode(text));
	}

	/**
	 * @see {@link #addNonExistent}
	 */
	protected BooleanNode addNonExistentBooleanNode(ObjectNode parent, String field, boolean b) {
		return addNonExistent(parent, field, parent.booleanNode(b));
	}

	/**
	 * @see {@link #addNonExistent}
	 */
	protected ArrayNode addNonExistentArrayNode(ObjectNode parent, String field, JsonNode[] items) {
		ArrayNode arrayNode = parent.arrayNode();
		arrayNode.addAll(Arrays.asList(items));
		return addNonExistent(parent, field, arrayNode);
	}

	/**
	 * Removes a field from object node if it exists.
	 * 
	 * @param <N>
	 *            Expected value type.
	 * @param parent
	 * @param field
	 * @return Value of the removed field or null if the field does not exist.
	 */
	@SuppressWarnings("unchecked")
	protected <N extends JsonNode> N removeIfExists(ObjectNode parent, String field) {
		JsonNode formerLayoutParamsField = parent.get(field);

		if (formerLayoutParamsField != null) {
			parent.remove(field);
		}
		return (N) formerLayoutParamsField;
	}
}
