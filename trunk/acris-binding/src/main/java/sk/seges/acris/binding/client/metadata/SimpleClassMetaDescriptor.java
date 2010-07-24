package sk.seges.acris.binding.client.metadata;

import java.io.Serializable;

public final class SimpleClassMetaDescriptor implements Serializable {

	private static final long serialVersionUID = 1283862800432915151L;

	private String className;
	private transient Class<?> clazz;

	public SimpleClassMetaDescriptor() {
	}

	/**
	 * Beware of using this method after RPC synchronization. I'll tell you the secret it will be null after that
	 * operation because it is transient
	 * 
	 * @return entity class
	 */
	public Class<?> getEntityClass() {
		return clazz;
	}

	public String getEntityClassName() {
		return className;
	}

	public void setEntityClass(Class<?> clazz) {
		this.clazz = clazz;
		this.className = clazz.getName();
	}
}