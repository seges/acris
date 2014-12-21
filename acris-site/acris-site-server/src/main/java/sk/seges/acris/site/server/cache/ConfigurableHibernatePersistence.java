package sk.seges.acris.site.server.cache;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernatePersistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.Map;

/**
 * Created by PeterSimun on 27.11.2014.
 */
public class ConfigurableHibernatePersistence extends HibernatePersistence {

    private final Interceptor interceptor;

    public ConfigurableHibernatePersistence(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map map) {
        Ejb3Configuration cfg = new Ejb3Configuration();
        Ejb3Configuration configured = cfg.configure( info, map );
        postprocessConfiguration(info, map, configured);
        return configured != null ? configured.buildEntityManagerFactory() : null;
    }

    @SuppressWarnings("unchecked")
    protected void postprocessConfiguration(PersistenceUnitInfo info, Map map, Ejb3Configuration configured) {
        if (this.interceptor != null) {
            if (configured.getInterceptor()==null || EmptyInterceptor.class.equals(configured.getInterceptor().getClass())) {
                configured.setInterceptor(this.interceptor);
            } else {
                throw new IllegalStateException("Hibernate interceptor already set in persistence.xml (" + configured.getInterceptor() + ")");
            }
        }
    }
}