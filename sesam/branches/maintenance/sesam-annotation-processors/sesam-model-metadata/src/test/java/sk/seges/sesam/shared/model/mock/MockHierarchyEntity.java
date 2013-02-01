package sk.seges.sesam.shared.model.mock;

import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
public class MockHierarchyEntity implements MockHierarchyEntityData {

	private String name;
	private MockHierarchyEntityData parent;
	
	@Override
	public MockHierarchyEntityData getParent() {
		return parent;
	}

	@Override
	public void setParent(MockHierarchyEntityData entity) {
		this.parent = entity;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}