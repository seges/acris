package sk.seges.acris.core.shared.common;


public interface HasStatus<E extends IStatus> {

	E getStatus();

	void setStatus(E status);

}
