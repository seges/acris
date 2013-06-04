/**
 * 
 */
package sk.seges.acris.pivo;

/**
 * @author eldzi
 */
public class Alias extends AbstractObjectFactory {
	private final String ref;
		
	public Alias(ChocolateFactory factory, String ref) {
		super(factory);
		this.ref = ref;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T create() {
		return (T)factory.getChocolate(ref);
	}
	
	/* (non-Javadoc)
	 * @see sk.seges.acris.pivo.AbstractObjectFactory#construct(java.lang.Object[])
	 */
	@Override
	protected Object construct(Object... values) {
		throw new PivoException("Not possible to construct something in alias " + ref);
	}

	@Override
	protected final Scope getScope() {
		return Scope.PROTOTYPE;
	}
}
