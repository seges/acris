package sk.seges.acris.core.shared.common;


public interface HasType<E extends IType> {

	E getType();

	void setType(E type);
}