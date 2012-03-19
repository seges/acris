package sk.seges.sesam.shared.model.mock;

import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
public interface MockHierarchyEntityData {

	MockHierarchyEntityData getParent();
	void setParent(MockHierarchyEntityData entity);
	
	String getName();
	void setName(String name);
}
