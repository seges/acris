package sk.seges.acris.site.server.cache;

import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.Header;
import net.sf.ehcache.constructs.web.PageInfo;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by PeterSimun on 30.11.2014.
 */
public class PreprocessedPageInfo extends PageInfo {

    private final String originalBody;
    private final String responseForCache;

    @Deprecated
    public PreprocessedPageInfo(int statusCode, String contentType, Collection headers, Collection cookies, byte[] body, boolean storeGzipped, long timeToLiveSeconds, String originalBody, String responseForCache) throws AlreadyGzippedException {
        super(statusCode, contentType, headers, cookies, body, storeGzipped, timeToLiveSeconds);
        this.originalBody = originalBody;
        this.responseForCache = responseForCache;
    }

    public PreprocessedPageInfo(int statusCode, String contentType, Collection cookies, byte[] body, boolean storeGzipped, long timeToLiveSeconds, Collection<Header<? extends Serializable>> headers, String originalBody, String responseForCache) throws AlreadyGzippedException {
        super(statusCode, contentType, cookies, body, storeGzipped, timeToLiveSeconds, headers);
        this.originalBody = originalBody;
        this.responseForCache = responseForCache;
    }

    public String getOriginalBody() {
        return originalBody;
    }

    public String getResponseForCache() {
        return responseForCache;
    }
}