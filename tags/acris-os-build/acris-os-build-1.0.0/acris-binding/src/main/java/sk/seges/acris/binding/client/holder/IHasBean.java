package sk.seges.acris.binding.client.holder;

import java.io.Serializable;

public interface IHasBean<T extends Serializable> {
    /**
     * Bind values from bean to widgets or reload existing binding if there is
     * any.
     * 
     * @param bean
     *            values holder
     */
    public void setBean(T bean);

    /**
     * @return bean with bound values. Reflects values specified in UI widgets.
     */
    public T getBean();
}