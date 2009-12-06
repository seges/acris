package sk.seges.acris.holder;

import java.io.Serializable;

public interface IBeanBindingHolder<T extends Serializable> {
	public void setBean(T bean);
	public T getBean();
}
