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

    private Ejb3Configuration createConfiguration(PersistenceUnitInfo info, Map map) {
        Ejb3Configuration cfg = new Ejb3Configuration();
        Ejb3Configuration configured = cfg.configure( info, map );
        postProcessConfiguration(configured);
        return configured;
    }

    private Ejb3Configuration createConfiguration(String persistenceUnitName, Map properties) {
        Ejb3Configuration cfg = new Ejb3Configuration();
        Ejb3Configuration configured = cfg.configure( persistenceUnitName, properties );
        postProcessConfiguration(configured);
        return configured;
    }

    @Override
    public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
        Ejb3Configuration configured = createConfiguration(persistenceUnitName, properties);
        return configured != null ? configured.buildEntityManagerFactory() : null;
    }

    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map map) {
        Ejb3Configuration configured = createConfiguration(info, map);
        return configured != null ? configured.buildEntityManagerFactory() : null;
    }

    @SuppressWarnings("unchecked")
    protected void postProcessConfiguration(Ejb3Configuration configured) {
        if (this.interceptor != null) {
            if (configured.getInterceptor()==null || EmptyInterceptor.class.equals(configured.getInterceptor().getClass())) {
                configured.setInterceptor(this.interceptor);
            } else {
                throw new IllegalStateException("Hibernate interceptor already set in persistence.xml (" + configured.getInterceptor() + ")");
            }
        }
    }
}