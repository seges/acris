package sk.seges.acris.site.server.cache.configuration.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.FilterChainProxy;
import sk.seges.acris.security.server.AcrisSecurityDtoEntityProvider;
import sk.seges.acris.server.AcrisSiteDtoEntityProvider;
import sk.seges.acris.site.server.cache.CacheFilter;
import sk.seges.acris.site.server.cache.MutableCacheManager;
import sk.seges.sesam.shared.model.converter.EntityProviderContext;

/**
 * Created by PeterSimun on 23.11.2014.
 */
@Configuration
@ComponentScan(basePackages = "sk.seges.acris.site.server.cache.configuration.spring")
public class CacheFilterTestConfiguration {

    class MockConverterContextEntityProviderContext extends EntityProviderContext {

        public EntityProviderContext get() {
            MockConverterContextEntityProviderContext result = new MockConverterContextEntityProviderContext();
            result.registerEntityProvider(new AcrisSiteDtoEntityProvider());
            result.registerEntityProvider(new AcrisSecurityDtoEntityProvider());
            return result;
        }
    }

    @Bean
    public EntityProviderContext entityProviderContext() {
        return new MockConverterContextEntityProviderContext();
    }

    @Bean
    public MutableCacheManager cacheManager(EntityProviderContext entityProviderContext) {
        return MutableCacheManager.create(getClass().getClassLoader().getResource("test-ehcache.xml"), entityProviderContext);
    }

    @Bean
    public CacheFilter cacheFilter(MutableCacheManager cacheManager) {
        return new CacheFilter(cacheManager);
    }

    @Bean
    public FilterChainProxy chainProxy() {
       return new FilterChainProxy();
    }
}