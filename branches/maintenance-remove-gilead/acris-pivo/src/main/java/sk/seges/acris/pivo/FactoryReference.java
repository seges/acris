package sk.seges.acris.pivo;

public class FactoryReference implements IDependency {
	private final String token;
	
	public FactoryReference(String token) {
		this.token = token;
	}
	
	public Object resolve(ChocolateFactory factory) {
		return factory.getFactory(token);
	}
}
