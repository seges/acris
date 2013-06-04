package sk.seges.acris.pivo;

public class Reference implements IDependency {
	private final String token;
	
	public Reference(String token) {
		this.token = token;
	}
	
	public Object resolve(ChocolateFactory factory) {
		return factory.getChocolate(token);
	}
}
