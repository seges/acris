package sk.seges.acris.site.server.cache;


import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.GenericResponseWrapper;
import net.sf.ehcache.constructs.web.Header;
import net.sf.ehcache.constructs.web.PageInfo;
import net.sf.ehcache.constructs.web.filter.SimpleCachingHeadersPageCachingFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.seges.acris.security.server.core.request.session.GWTRPCSessionHttpServletRequestWrapper;
import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public class CacheFilter extends SimpleCachingHeadersPageCachingFilter {

	private static final String CODESVR = "gwt.codesvr";
	public static final String WEB_ID_DELIMITER = "___";
    private static final Logger LOG = LoggerFactory.getLogger(CacheFilter.class);
    private static final long ONE_YEAR_IN_MILLISECONDS = 60 * 60 * 24 * 365 * 1000L;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    
    private final CacheManager cacheManager;
        
	public CacheFilter(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	protected boolean filterNotDisabled(HttpServletRequest httpRequest) {
 		if (!httpRequest.getMethod().toUpperCase().equals("GET") || (httpRequest.getHeader("referer") != null && httpRequest.getHeader("referer").contains(CODESVR))) {
 			return false;
 		}
 		HttpSession session = httpRequest.getSession();
 		if (session != null) {
 			LoginToken token = (LoginToken)session.getAttribute(LoginConstants.LOGIN_TOKEN_NAME);
 			if (token != null) {
 				if (token.isAdmin()) {
 					return false;
 				}
 			}
 		}
		return super.filterNotDisabled(httpRequest);
	}
	
	@Override
	protected String calculateKey(HttpServletRequest httpRequest) {
		StringBuffer stringBuffer = new StringBuffer();
		if(httpRequest instanceof GWTRPCSessionHttpServletRequestWrapper) {
			GWTRPCSessionHttpServletRequestWrapper sessionHttpServletRequestWrapper = (GWTRPCSessionHttpServletRequestWrapper) httpRequest;
			stringBuffer.append(sessionHttpServletRequestWrapper.getWebId() + WEB_ID_DELIMITER);
		}
		stringBuffer.append(httpRequest.getMethod()).append(httpRequest.getRequestURI())
				.append(httpRequest.getQueryString());
		String key = stringBuffer.toString();
		return key;
	}
	
	@Override
	public void doInit(FilterConfig filterConfig) throws CacheException {		
		synchronized (this.getClass()) {
            	setCacheNameIfAnyConfigured(filterConfig);
            	cacheManager.setName(getCacheName());
        }
	}

    @Override
	protected PageInfo buildPageInfo(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws Exception {
        // Look up the cached page
        final String key = calculateKey(request);
        PageInfo pageInfo = null;        
		Element element = get(key);
		if (element == null || element.getObjectValue() == null) {
			try {
				// Page is not cached - build the response, cache it, and
				// send to client
				pageInfo = buildPage(request, response, chain);
				if (pageInfo.isOk()) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("PageInfo ok. Adding to cache " + getTargetCacheByKey(key).getName() + " with key " + key);
					}
					put(new Element(key, pageInfo));
				} else {
					if (LOG.isDebugEnabled()) {
						LOG.debug("PageInfo was not ok(200). Putting null into cache " + getTargetCacheByKey(key).getName()
								+ " with key " + key);
					}
					put(new Element(key, null));
				}
			} catch (final Throwable throwable) {
				LOG.error("Putting into cache " + getTargetCacheByKey(key).getName() + " failed with key " + key);
				put(new Element(key, null));
				throw new Exception(throwable);
			}
		} else {
			pageInfo = (PageInfo) element.getObjectValue();
		}
        return pageInfo;
    }
    
    @Override
    protected PageInfo buildPage(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws AlreadyGzippedException, Exception {
    	// Invoke the next entity in the chain
        final ByteArrayOutputStream outstr = new ByteArrayOutputStream();
        final GenericResponseWrapper wrapper = new GenericResponseWrapper(
                response, outstr);
        chain.doFilter(request, wrapper);
        wrapper.flush();

        Ehcache cache = getTargetCacheByRequest(request); 
        long timeToLiveSeconds = cache.getCacheConfiguration()
                .getTimeToLiveSeconds();

        // Create the page info
        PageInfo pageInfo = new PageInfo(wrapper.getStatus(), wrapper.getContentType(),
                wrapper.getCookies(), outstr.toByteArray(), true,
                timeToLiveSeconds, wrapper.getAllHeaders());

        final List<Header<? extends Serializable>> headers = pageInfo.getHeaders();

        long ttlMilliseconds = calculateTimeToLiveMilliseconds(cache);
        
        //Remove any conflicting headers
        for (final Iterator<Header<? extends Serializable>> headerItr = headers.iterator(); headerItr.hasNext();) {
            final Header<? extends Serializable> header = headerItr.next();
            
            final String name = header.getName();
            if ("Last-Modified".equalsIgnoreCase(name) || 
                    "Expires".equalsIgnoreCase(name) || 
                    "Cache-Control".equalsIgnoreCase(name) || 
                    "ETag".equalsIgnoreCase(name)) {
                headerItr.remove();
            }
        }
        
        //add expires and last-modified headers
        
        //trim the milliseconds off the value since the header is only accurate down to the second
        long lastModified = pageInfo.getCreated().getTime();
        lastModified = TimeUnit.MILLISECONDS.toSeconds(lastModified);
        lastModified = TimeUnit.SECONDS.toMillis(lastModified);
        
        headers.add(new Header<Long>("Last-Modified", lastModified));
        headers.add(new Header<Long>("Expires", System.currentTimeMillis() + ttlMilliseconds));
        headers.add(new Header<String>("Cache-Control", "max-age=" + ttlMilliseconds / MILLISECONDS_PER_SECOND));
        headers.add(new Header<String>("ETag", generateEtag(ttlMilliseconds)));
        
        return pageInfo;
    }
    
	private long calculateTimeToLiveMilliseconds(Ehcache cache) {    	
        if (cache.isDisabled()) {
            return -1;
        } else {
            CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
            if (cacheConfiguration.isEternal()) {
                return ONE_YEAR_IN_MILLISECONDS;
            } else {
                return cacheConfiguration.getTimeToLiveSeconds() * MILLISECONDS_PER_SECOND;
            }
        }
    }
    
    @Override
	protected CacheManager getCacheManager() {
        return cacheManager;
    }
        
    private Ehcache put(Element element){
		Object key = element.getObjectKey();
        Ehcache cache = getTargetCacheByKey(key);
        cache.put(element);
        return cache;
    }
    
    private String createCacheName(String webId){
    	return webId + WEB_ID_DELIMITER + cacheManager.getName();
    }

    private Ehcache getTargetCacheByRequest(HttpServletRequest request){ 
    	String key = calculateKey(request);
    	return getTargetCacheByKey(key);
    }
    
    private Ehcache getTargetCacheByKey(Object key){    	
    	String webId = determineWebId(key);
    	String cacheName = createCacheName(webId);
    	Ehcache cache = cacheManager.getEhcache(cacheName);
    	if(cache == null) {
            cache = cacheManager.addCacheIfAbsent(cacheName);
    	}
    	return cache;
    }
    
	private String determineWebId(Object key) {
		String keyString = String.valueOf(key);
		int index = keyString.indexOf(CacheFilter.WEB_ID_DELIMITER);
		if(index < 0){
			return null;
		}
		return keyString.substring(0, index);
	}
	
	private Element get(Object key){	
		Ehcache cache = getTargetCacheByKey(key);
		return cache.get(key);		
	}
    

    /**
     * ETags are required to have double quotes around the value, unlike any other header.
     * <p/>
     * The ehcache eTag is effectively the Expires time, but accurate to milliseconds, i.e.
     * no conversion to the nearest second is done as is done for the Expires tag. It therefore
     * is the most precise indicator of whether the client cached version is the same as the server
     * version.
     * <p/>
     * MD5 is not used to calculate ETag, as it is in some implementations, because it does not
     * add any extra value in this situation, and it has a higher cost.
     *
     * @see "http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html#sec13.3.3"
     */
    private String generateEtag(long ttlMilliseconds) {
        StringBuffer stringBuffer = new StringBuffer();
        Long eTagRaw = System.currentTimeMillis() + ttlMilliseconds;
        String eTag = stringBuffer.append("\"").append(eTagRaw).append("\"").toString();
        return eTag;
    }
	
 }