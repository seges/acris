package sk.seges.acris.site.server.cache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.seges.acris.common.util.Pair;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.shared.model.converter.EntityProviderContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PeterSimun on 25.11.2014.
 */
public class MutableCacheManager extends CacheManager implements CacheHandler {

    public static final String REMOVE_PREFIX = "___REMOVE_ME__";

    private Map<String, List<String>> inverseCacheReference = new HashMap<String, List<String>>();

    public static final String WEB_ID_DELIMITER = "___";

    private static final Logger LOG = LoggerFactory.getLogger(CacheManager.class);

    private final EntityProviderContext entityProviderContext;

    protected MutableCacheManager(URL configurationFileURL, EntityProviderContext entityProviderContext) {
        super(configurationFileURL);
        this.entityProviderContext = entityProviderContext;
    }

    protected MutableCacheManager(String configurationFileName, EntityProviderContext entityProviderContext) {
        super(configurationFileName);
        this.entityProviderContext = entityProviderContext;
    }

    private static Pair<Integer, Integer> getResponseIdIndexes(String originalResponse) {
        int index = originalResponse.indexOf(REMOVE_PREFIX);
        return new Pair(index, originalResponse.substring(index + REMOVE_PREFIX.length()).indexOf(REMOVE_PREFIX));
    }

    public static String getResponseForClient(String originalResponse) {
        Pair<Integer, Integer> idIndexes = getResponseIdIndexes(originalResponse);
        return originalResponse.substring(0, idIndexes.getFirst()) + originalResponse.substring(idIndexes.getSecond() + REMOVE_PREFIX.length());
    }

    public static String getResponseForCache(String originalResponse) {
        Pair<Integer, Integer> idIndexes = getResponseIdIndexes(originalResponse);
        return originalResponse.substring(idIndexes.getFirst() + REMOVE_PREFIX.length(), idIndexes.getSecond());
    }

    public Ehcache put(Element element){
        String key = (String) element.getObjectKey();
        Ehcache cache = getTargetCacheByKey(key);
        cache.put(element);

        PreprocessedPageInfo pageInfo = (PreprocessedPageInfo) element.getValue();

        String idList = pageInfo.getResponseForCache();

        if (idList != null && idList.length() > 0) {
            String idListEntries[] = idList.split(",");

            for (String idListEntry : idListEntries) {
                List<String> cachedEntry = inverseCacheReference.get(idListEntry);
                if (cachedEntry == null) {
                    cachedEntry = new ArrayList<String>();
                    inverseCacheReference.put(idListEntry, cachedEntry);
                }

                cachedEntry.add(key);
            }
        }

        return cache;
    }

    private String createCacheName(String webId){
        return webId + WEB_ID_DELIMITER + getName();
    }

    private String determineWebId(Object key) {
        String keyString = String.valueOf(key);
        int index = keyString.indexOf(WEB_ID_DELIMITER);
        if(index < 0){
            return null;
        }
        return keyString.substring(0, index);
    }

    public Ehcache getTargetCacheByKey(String key){
        String webId = determineWebId(key);
        String cacheName = createCacheName(webId);
        Ehcache cache = getEhcache(cacheName);
        if(cache == null) {
            cache = addCacheIfAbsent(cacheName);
        }
        return cache;
    }

    public Element get(String key){
        Ehcache cache = getTargetCacheByKey(key);
        return cache.get(key);
    }

    @Override
    public void invalidate(Object entity) {

        if (!(entity instanceof IDomainObject)) {
            //we do cache only entities that implements IDomainObject
            return;
        }

        List<String> cacheReferences = inverseCacheReference.get(entity.getClass().getCanonicalName() + "/" + ((IDomainObject<Long>) entity).getId().hashCode());

        if (cacheReferences != null) {
            for (String cacheReference : cacheReferences) {
                Element element = get(cacheReference);

                if (element != null) {
                    //cached result, remove it
                    getTargetCacheByKey(cacheReference).remove(cacheReference);
                }
            }
        }

        List<Class<?>> dtoClasses = entityProviderContext.get().getDtoClassForDomain(entity.getClass());

        if (dtoClasses != null || dtoClasses.size() == 0) {
            for (Class<?> dtoClass: dtoClasses) {
                cacheReferences = inverseCacheReference.get(dtoClass.getCanonicalName() + "/" + ((IDomainObject<Long>) entity).getId().hashCode());

                if (cacheReferences != null) {
                    for (String cacheReference : cacheReferences) {
                        Element element = get(cacheReference);

                        if (element != null) {
                            //cached result, remove it
                            getTargetCacheByKey(cacheReference).remove(cacheReference);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void clearCache(String webId, String locale) {
        Ehcache cache = getEhcache(webId);
        cache.removeAll();
    }

    private static volatile MutableCacheManager singleton;

    public static MutableCacheManager create(URL configurationFileURL, EntityProviderContext entityProviderContext) throws CacheException {
        if (singleton != null) {
            return singleton;
        }
        synchronized (MutableCacheManager.class) {
            if (singleton == null) {
                LOG.debug("Creating new CacheManager with config URL: {}", configurationFileURL);
                singleton = new MutableCacheManager(configurationFileURL, entityProviderContext);
            }
            return singleton;
        }
    }

    public static MutableCacheManager create(String configurationFileName, EntityProviderContext entityProviderContext) throws CacheException {
        if (singleton != null) {
            return singleton;
        }
        synchronized (MutableCacheManager.class) {
            if (singleton == null) {
                LOG.debug("Creating new CacheManager with config file: {}", configurationFileName);
                singleton = new MutableCacheManager(configurationFileName, entityProviderContext);
            }
            return singleton;
        }
    }
}