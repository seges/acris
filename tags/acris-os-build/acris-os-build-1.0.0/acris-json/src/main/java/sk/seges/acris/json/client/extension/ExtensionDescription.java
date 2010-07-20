package sk.seges.acris.json.client.extension;

public class ExtensionDescription {

	private Class<? extends Extension> extensionClass;

	private Class<? extends ExtensionPoint> extendedClass;

	private String pointName;

	/**
	 * Specifies whether the extension type can be repeated within its parent extension point.
	 */
	private boolean repeatable = false;

	public static ExtensionDescription getDefaultDescription(Class<? extends Extension> extensionClass) {
		ExtensionDescription extensionDescription = new ExtensionDescription();
		extensionDescription.setExtensionClass(extensionClass);
		return extensionDescription;
	}

	public void setRepeatable(boolean repeatable) {
		this.repeatable = repeatable;
	}

	final public boolean isRepeatable() {
		return repeatable;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public Class<? extends Extension> getExtensionClass() {
		return extensionClass;
	}

	public void setExtensionClass(Class<? extends Extension> extensionClass) {
		this.extensionClass = extensionClass;
	}

	public Class<? extends ExtensionPoint> getExtendedClass() {
		return extendedClass;
	}

	public void setExtendedClass(Class<? extends ExtensionPoint> extendedClass) {
		this.extendedClass = extendedClass;
	}
}