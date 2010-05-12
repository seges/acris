package sk.seges.acris.binding.client.samples.mocks;

import java.io.Serializable;

import sk.seges.acris.binding.client.annotations.BeanWrapper;

@BeanWrapper
public class Company implements Serializable {
	
	private static final long serialVersionUID = -117411635160866461L;

	public static final String NAME_ATTRIBUTE = "name";
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
