package sk.seges.acris.pap.bean.model;

import sk.seges.acris.core.client.annotation.BeanWrapper;

@BeanWrapper
public class SuperBean extends BaseBean {

	@Override
	public String getCity() {
		return null;
	}
	
	@Override
	public ReferencedBean getRef() {
		return null;
	}
}