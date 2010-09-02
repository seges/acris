package sk.seges.acris.mvp.server.domain;



//@Component
public class AcrisProxyGenerator {

	//@PostConstruct
	public void generateProxies() {
		try {
//			AdditionalCode additionalCode = AdditionalCodeReader.readFromFile(ProxyManager.JAVA_5_LAZY_POJO);
//
//			ProxyManager.getInstance().generateProxyClass(GenericUser.class, additionalCode);

		} catch (Exception e) {
			throw new RuntimeException("Failed to load proxy class", e);
		}

	}
}
