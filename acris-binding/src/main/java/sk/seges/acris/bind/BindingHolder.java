package sk.seges.acris.bind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;

import sk.seges.sesam.domain.IDomainObject;

import com.google.gwt.user.client.ui.Widget;

public class BindingHolder<T extends Serializable> implements IBindingHolder<T> {
	protected BindingGroup bindingGroup = new BindingGroup();
    private UpdateStrategy updateStrategy;
    private BeanWrapper<T> beanWrapper;

    private List<BindingFieldInfo> bindingFieldInfos = new ArrayList<BindingFieldInfo>();
    
    public T getBean() {
    	return beanWrapper.getContent();
    }
    
    public BindingHolder(UpdateStrategy updateStrategy, BeanWrapper<T> beanWrapper) {
    	this.updateStrategy = updateStrategy;
    	this.beanWrapper = beanWrapper;
    }

    public BindingGroup addBindingGroup(String sourceProperty, Widget targetWidget, String targetProperty, BeanProxyWrapper<? extends IDomainObject<?>, ? extends IDomainObject<?>> sourceObject) {
        return addBindingGroupForObject(sourceProperty, targetWidget, targetProperty, sourceObject);
    }
    
    public BindingGroup addBindingGroup(String sourceProperty, Widget targetWidget, String targetProperty, BeanWrapper<? extends IDomainObject<?>> sourceObject) {
        return addBindingGroupForObject(sourceProperty, targetWidget, targetProperty, sourceObject);
    }
    
    private BindingGroup addBindingGroupForObject(String sourceProperty, Widget targetWidget, String targetProperty, Object sourceObject) {
    	BindingFieldInfo bindingFieldInfo = new BindingFieldInfo();
    	bindingFieldInfo.setSourceProperty(sourceProperty);
    	bindingFieldInfo.setTargetProperty(targetProperty);
    	bindingFieldInfo.setTargetWidget(targetWidget);
    	bindingFieldInfo.setSourceObject(sourceObject);
    	bindingFieldInfos.add(bindingFieldInfo);

	    BindingGroup bindingGroup = new BindingGroup();

	    bindingGroup.addBinding(Bindings.createAutoBinding(updateStrategy, sourceObject,
	    		BeanProperty.create(sourceProperty), targetWidget, BeanProperty.create(targetProperty)));
	    
	    return bindingGroup; 
    }
        
    public void addBinding(String sourceProperty, Widget targetWidget, String targetProperty) {
    	addBindingFieldInfo(sourceProperty, targetWidget, targetProperty);
    	
    	bindingGroup.addBinding(createBinding(sourceProperty, targetWidget, targetProperty));
	}

	private AutoBinding<Object, Object, Object, Object> createBinding(String sourceProperty, Widget targetWidget,
			String targetProperty) {
		return Bindings.createAutoBinding(updateStrategy, beanWrapper,
	    		BeanProperty.create(sourceProperty), targetWidget, BeanProperty.create(targetProperty));
	}
    
    private BindingFieldInfo addBindingFieldInfo(String sourceProperty, Widget targetWidget, String targetProperty) {
    	BindingFieldInfo bindingFieldInfo = new BindingFieldInfo();
    	bindingFieldInfo.setSourceProperty(sourceProperty);
    	bindingFieldInfo.setTargetProperty(targetProperty);
    	bindingFieldInfo.setTargetWidget(targetWidget);
    	bindingFieldInfos.add(bindingFieldInfo);
	    
	    return bindingFieldInfo;
    }

    @SuppressWarnings("unchecked")
	private void clearBindings() {
	    List<Binding> bindings = bindingGroup.getBindings();
	    
	    for (Binding binding : bindings) {
	    	bindingGroup.removeBinding(binding);
	    }
    }

    private void reloadBindings() {
    	for (BindingFieldInfo bindingFieldInfo : bindingFieldInfos) {
    		Binding<?,?,?,?> binding = null;
    		if (bindingFieldInfo.getSourceObject() == null) {
			    binding = Bindings.createAutoBinding(updateStrategy, beanWrapper,
			    		BeanProperty.create(bindingFieldInfo.getSourceProperty()), 
			    		bindingFieldInfo.getTargetWidget(), 
			    		BeanProperty.create(bindingFieldInfo.getTargetProperty()));
			    bindingGroup.addBinding(binding);
    		} else {
    			binding = Bindings.createAutoBinding(updateStrategy, bindingFieldInfo.getSourceObject(),
			    		BeanProperty.create(bindingFieldInfo.getSourceProperty()), 
			    		bindingFieldInfo.getTargetWidget(), 
			    		BeanProperty.create(bindingFieldInfo.getTargetProperty()));
    		    BindingGroup bindingGroup = new BindingGroup();
			    bindingGroup.addBinding(binding);
			    bindingGroup.bind();
    		}
    	}	
    }
    
    public void bind() {
	    bindingGroup.bind();
    }
    
    public void rebind() {
		if (bindingGroup.getBindings().size() > 0) {
			bindingGroup.unbind();
			clearBindings();
			reloadBindings();
			bind();
		}
    }

	public void setBean(T bean) {
		boolean reloadBindings = false;
		if (bindingGroup.getBindings().size() > 0) {
			bindingGroup.unbind();
			reloadBindings = true;
			clearBindings();
		}
		
		beanWrapper.setContent(bean);

		if (reloadBindings) {
			reloadBindings();
			bind();
		}
	}
}