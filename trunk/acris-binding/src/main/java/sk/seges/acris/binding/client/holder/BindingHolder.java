package sk.seges.acris.binding.client.holder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;

import sk.seges.acris.binding.client.annotations.BindingFieldInfo;
import sk.seges.acris.binding.client.wrappers.BeanProxyWrapper;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;

import com.google.gwt.user.client.ui.Widget;

public class BindingHolder<T extends Serializable> implements IBindingHolder<T> {
	
	protected List<BindingGroup> asyncbindingGroups = new ArrayList<BindingGroup>();
	protected BindingGroup rootBinding = new BindingGroup();
	
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
	    asyncbindingGroups.add(bindingGroup);
	    return bindingGroup; 
    }
        
    /**
     * Add one-to-one binding to the root binding group. Be sure you are not add x-to-many binding using
     * this method. For x-to-many binding you should you use addBindingGroup
     */
    public void addBinding(String sourceProperty, Widget targetWidget, String targetProperty) {
    	addBindingFieldInfo(sourceProperty, targetWidget, targetProperty);
    	
    	rootBinding.addBinding(createBinding(sourceProperty, targetWidget, targetProperty));
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

    public void bind() {
    	rootBinding.bind();
    }
    
    public void rebind() {
		if (bindingFieldInfos.size() > 0) {
			rootBinding.unbind();
			rootBinding.bind();
			
			for (BindingGroup binding : asyncbindingGroups) {
				binding.unbind();
				binding.bind();
			}
		}
    }

	public void setBean(T bean) {
		boolean reloadBindings = false;
		if (bindingFieldInfos.size() > 0) {
			rootBinding.unbind();
			for (BindingGroup binding : asyncbindingGroups) {
				binding.unbind();
			}
			reloadBindings = true;
		}
		
		beanWrapper.setContent(bean);

		if (reloadBindings) {
			rootBinding.bind();
			for (BindingGroup binding : asyncbindingGroups) {
				binding.bind();
			}
		}
	}
}