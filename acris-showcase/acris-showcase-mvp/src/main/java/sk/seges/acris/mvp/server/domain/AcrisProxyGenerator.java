package sk.seges.acris.mvp.server.domain;

import javax.annotation.PostConstruct;

import net.sf.gilead.proxy.ProxyManager;
import net.sf.gilead.proxy.xml.AdditionalCode;
import net.sf.gilead.proxy.xml.AdditionalCodeReader;

import org.springframework.stereotype.Component;

import sk.seges.acris.security.rpc.user_management.domain.GenericUser;


@Component
public class AcrisProxyGenerator {

	@PostConstruct
	public void generateProxies() {
		try {
			AdditionalCode additionalCode = AdditionalCodeReader.readFromFile(ProxyManager.JAVA_5_LAZY_POJO);

			ProxyManager.getInstance().generateProxyClass(GenericUser.class, additionalCode);

		} catch (Exception e) {
			throw new RuntimeException("Failed to load proxy class", e);
		}

	}
}
