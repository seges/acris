package sk.seges.sesam.shared.model.mock;

import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.strategy.DBPropertyConverter;
import sk.seges.sesam.model.metadata.strategy.PojoPropertyConverter;

@MetaModel(beanPropertyConverter = {PojoPropertyConverter.class, DBPropertyConverter.class})
public class MultipleConverterEntity {

	private String name;
	private MockHierarchyEntityData parent;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
		
	public MockHierarchyEntityData getParent() {
		return parent;
	}

	public void setParent(MockHierarchyEntityData entity) {
		this.parent = entity;
	}
}