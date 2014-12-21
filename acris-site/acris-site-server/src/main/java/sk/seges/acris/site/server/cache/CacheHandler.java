package sk.seges.acris.site.server.cache;

/**
 * Created by PeterSimun on 25.11.2014.
 */
public interface CacheHandler {

    void invalidate(Object entity);

    /**
     * Clears cache for the specific web ID
     * @param webId webId to be purged
     * @param locale not used right now
     */
    void clearCache(String webId, String locale);

    //void purge(String webId, String locale);
    //void purge();
}
