package sk.seges.sesam.model.metadata.strategy;

/**
 * @author ladislav.gazo
 */
public enum MetamodelMethodStrategy {
	/**
	 * Require methods to be prefixed with get/set, name of the meta field will
	 * be without the prefix
	 */
	GETTER_SETTER,
	/** Names of methods will not be stripped and will be taken as such */
	PURE
}
