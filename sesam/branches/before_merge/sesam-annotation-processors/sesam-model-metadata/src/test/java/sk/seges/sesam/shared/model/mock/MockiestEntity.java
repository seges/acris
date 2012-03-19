package sk.seges.sesam.shared.model.mock;

import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
public class MockiestEntity extends MockHierarchyEntity {

	private String nick;
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	@Override
	public MockHierarchyEntity getParent() {
		return (MockHierarchyEntity) super.getParent();
	}

	public void setParent(MockHierarchyEntity entity) {
		super.setParent(entity);
	}
}