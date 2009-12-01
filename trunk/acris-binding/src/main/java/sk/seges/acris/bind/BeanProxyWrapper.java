package sk.seges.acris.bind;

import java.util.List;

import sk.seges.acris.bind.BeanWrapper;

/**
 * Extension of the bean wrapper used for One-To-Many binding. BeanProxyWrapper
 * holds target bean and provides setting/getting binding object using {@link List}
 * of proxy objects. 
 * @author fat
 *
 * @param <M>
 * @param <T>
 */
public interface BeanProxyWrapper<M, T> extends BeanWrapper<T> {
	//TODO use setContent instead
	public void setTargetWrapper(BeanWrapper<M> targetBeanWrapper);
	public void setProxyValues(List<T> proxyValues);
}
