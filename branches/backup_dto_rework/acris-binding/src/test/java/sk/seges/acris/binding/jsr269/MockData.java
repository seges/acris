package sk.seges.acris.binding.jsr269;

import sk.seges.acris.binding.client.annotations.BeanWrapper;

@BeanWrapper
public interface MockData<E> {

	boolean hasSomething(String something);

}
