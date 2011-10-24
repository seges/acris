package sk.seges.acris.json.client.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExtensionPoint implements Extension {

	private Map<Class<? extends Extension>, Extension> nonRepeatingExtensionMap = new LinkedHashMap<Class<? extends Extension>, Extension>();

	/**
	 * Collection of repeating extensions. Uses {@link LinkedHashMap} in order to provide a predictable generation
	 * output order based upon insertion order.
	 */
	private Map<Class<? extends Extension>, List<Extension>> repeatingExtensionMap = new LinkedHashMap<Class<? extends Extension>, List<Extension>>();

	/**
	 * Simple constructor to create a new (empty) ExtensionPoint.
	 */
	public ExtensionPoint() {
	}

	/**
	 * Simple copy constructor that does a shallow copy of extension and manifest data from an existing ExtensionPoint
	 * to the constructed instance.
	 */
	protected ExtensionPoint(ExtensionPoint sourcePoint) {

		// WARNING: ANY NON-STATIC FIELDS ADDED ABOVE NEED TO BE COPIED HERE.
		nonRepeatingExtensionMap = sourcePoint.nonRepeatingExtensionMap;
		repeatingExtensionMap = sourcePoint.repeatingExtensionMap;
	}

	/**
	 * Declares the set of expected Extension types for an ExtensionPoint within the target extension profile. The base
	 * implementation does not declare any extensions, but can be overridden by specific types of ExtensionPoints that
	 * always contain a well-defined set of extensions.
	 * 
	 * @param extProfile
	 *            the ExtensionProfile to initialize.
	 */
	public void declareExtensions(ExtensionProfile extProfile) {
		// The default implementation does not register any extensions.
	}

	/** Returns whether the non-repeating extension is present. */
	public final <T extends Extension> boolean hasExtension(Class<T> extensionClass) {
		return nonRepeatingExtensionMap.containsKey(extensionClass);
	}

	/** Returns whether the repeating extension is present. */
	@SuppressWarnings("unchecked")
	public final <T extends Extension> boolean hasRepeatingExtension(Class<T> extensionClass) {
		List<T> ret = (List<T>) repeatingExtensionMap.get(extensionClass);
		return ret != null && !ret.isEmpty();
	}

	/** Retrieves a non-repeating extension or {@code null} if not present. */
	@SuppressWarnings("unchecked")
	public <T extends Extension> T getExtension(Class<T> extensionClass) {
		return (T) nonRepeatingExtensionMap.get(extensionClass);
	}

	/**
	 * Returns an unmodifiable collection of non-repeating extensions in this ExtensionPoint.
	 * 
	 * @return Collection of non-repeating extensions.
	 */
	public Collection<Extension> getExtensions() {
		return Collections.unmodifiableCollection(nonRepeatingExtensionMap.values());
	}

	/** Retrieves a repeating extension list (an empty list if not present). */
	@SuppressWarnings("unchecked")
	public <T extends Extension> List<T> getRepeatingExtension(Class<T> extensionClass) {

		List<T> ret = (List<T>) repeatingExtensionMap.get(extensionClass);
		if (ret == null) {
			ret = new ArrayList<T>();
			repeatingExtensionMap.put(extensionClass, (List<Extension>) ret);
		}
		return ret;
	}

	/**
	 * Returns an unmodifiable collection of lists of repeating extensions in this ExtensionPoint. The Extensions that
	 * are of the same type are grouped together in lists within the collection.
	 * 
	 * @return Collection of lists of repeating extensions.
	 */
	public Collection<List<Extension>> getRepeatingExtensions() {
		return Collections.unmodifiableCollection(repeatingExtensionMap.values());
	}

	/** Internal helper method. */
	protected boolean addExtension(Extension ext, Class<? extends Extension> extClass) {

		if (nonRepeatingExtensionMap.containsKey(extClass)) {
			return false;
		}

		nonRepeatingExtensionMap.put(extClass, ext);
		return true;
	}

	/** Adds an extension object. */
	public void addExtension(Extension ext) {
		addExtension(ext, ext.getClass());
	}

	/** Sets an extension object. If one exists of this type, it's replaced. */
	public void setExtension(Extension ext) {
		nonRepeatingExtensionMap.remove(ext.getClass());
		addExtension(ext, ext.getClass());
	}

	/** Internal helper method. */
	protected void addRepeatingExtension(Extension ext, Class<? extends Extension> extClass) {

		List<Extension> extList = repeatingExtensionMap.get(extClass);
		if (extList == null) {
			extList = new ArrayList<Extension>();
		}

		extList.add(ext);
		repeatingExtensionMap.put(extClass, extList);
	}

	/** Adds a repeating extension object. */
	public void addRepeatingExtension(Extension ext) {
		addRepeatingExtension(ext, ext.getClass());
	}

	/** Removes an extension object. */
	public void removeExtension(Extension ext) {
		nonRepeatingExtensionMap.remove(ext.getClass());
	}

	/** Removes an extension object based on its class. */
	public void removeExtension(Class<? extends Extension> extensionClass) {
		nonRepeatingExtensionMap.remove(extensionClass);
	}

	/** Removes a repeating extension object. */
	public void removeRepeatingExtension(Extension ext) {

		List<Extension> extList = repeatingExtensionMap.get(ext.getClass());
		if (extList == null) {
			return;
		}

		extList.remove(ext);
	}
}