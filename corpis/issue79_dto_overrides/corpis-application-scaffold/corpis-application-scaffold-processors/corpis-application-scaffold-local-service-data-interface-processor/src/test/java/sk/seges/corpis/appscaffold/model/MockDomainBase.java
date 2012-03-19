package sk.seges.corpis.appscaffold.model;


public class MockDomainBase<T> implements MockDomainData<T> {

	private String type;
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

}
