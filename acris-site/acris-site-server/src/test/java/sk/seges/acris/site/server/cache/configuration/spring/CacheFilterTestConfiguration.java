package sk.seges.acris.site.server.cache.configuration.spring;

import net.sf.ehcache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.FilterChainProxy;
import sk.seges.acris.site.server.cache.CacheFilter;
import sk.seges.acris.site.server.cache.MutableCacheManager;
import sk.seges.corpis.server.model.converter.provider.AbstractContextualConverterProvider;
import sk.seges.sesam.shared.model.converter.EntityProviderContext;

/**
 * Created by PeterSimun on 23.11.2014.
 */
@Configuration
@ComponentScan(basePackages = "sk.seges.acris.site.server.cache.configuration.spring")
public class CacheFilterTestConfiguration {

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