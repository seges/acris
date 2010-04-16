package sk.seges.acris.recorder.server.service;

import javax.annotation.PostConstruct;

import net.sf.gilead.proxy.ProxyManager;
import net.sf.gilead.proxy.xml.AdditionalCode;
import net.sf.gilead.proxy.xml.AdditionalCodeReader;

import org.springframework.stereotype.Service;

import sk.seges.acris.recorder.rpc.domain.SessionLog;
import sk.seges.acris.security.rpc.session.ClientSession;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.acris.security.rpc.user_management.domain.UserPreferences;

@Service
public class ProxyGenerator {
    
    @PostConstruct
    public void generateProxies() {
        try {
//            AdditionalCode additionalCode = AdditionalCodeReader.readFromFile(Gwt15ProxyGenerator.ADDITIONAL_CODE);
			AdditionalCode additionalCode = AdditionalCodeReader.readFromFile(ProxyManager.JAVA_5_LAZY_POJO);  
			
            ProxyManager.getInstance().generateProxyClass(SessionLog.class, additionalCode);
//            ProxyManager.getInstance().generateProxyClass(Program.class, additionalCode);
//            ProxyManager.getInstance().generateProxyClass(User.class, additionalCode);
            ProxyManager.getInstance().generateProxyClass(GenericUser.class, additionalCode);
            ProxyManager.getInstance().generateProxyClass(UserPreferences.class, additionalCode);
            ProxyManager.getInstance().generateProxyClass(ClientSession.class, additionalCode);
//            ProxyManager.getInstance().generateProxyClass(LazyGrantedAuthority.class, additionalCode);

        } catch (Exception e) {
            throw new RuntimeException("failed to load proxy class", e);
        }
    }
}