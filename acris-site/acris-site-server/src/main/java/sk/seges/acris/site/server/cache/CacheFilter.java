package sk.seges.acris.site.server.cache;


import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.web.GenericResponseWrapper;
import net.sf.ehcache.constructs.web.Header;
import net.sf.ehcache.constructs.web.filter.SimpleCachingHeadersPageCachingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.seges.acris.security.server.core.request.session.GWTRPCSessionHttpServletRequestWrapper;
import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

public class CacheFilter extends SimpleCachingHeadersPageCachingFilter {

    public static final String EHCACHE_ENABLED = "ehcache";

	private static final String CODESVR = "gwt.codesvr";
    private static final Logger LOG = LoggerFactory.getLogger(CacheFilter.class);

    private final MutableCacheManager cacheManager;
        
	public CacheFilter(MutableCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	protected boolean filterNotDisabled(HttpServletRequest httpRequest) {

        if (!httpRequest.getMethod().toUpperCase().equals("GET") || (httpRequest.getHeader("referer") != null && httpRequest.getHeader("referer").contains(CODESVR))) {
            httpRequest.setAttribute(EHCACHE_ENABLED, false);
            return false;
 		}
 		HttpSession session = httpRequest.getSession();
 		if (session != null) {
 			LoginToken token = (LoginToken)session.getAttribute(LoginConstants.LOGIN_TOKEN_NAME);
 			if (token != null && token.isAdmin()) {
                httpRequest.setAttribute(EHCACHE_ENABLED, false);
                return false;
 			}
 		}
		boolean result = super.filterNotDisabled(httpRequest);
        httpRequest.setAttribute(EHCACHE_ENABLED, result);
        return result;
	}

    @Override
	protected String calculateKey(HttpServletRequest httpRequest) {
		StringBuilder stringBuilder = new StringBuilder();

        if (httpRequest instanceof GWTRPCSessionHttpServletRequestWrapper) {
			GWTRPCSessionHttpServletRequestWrapper sessionHttpServletRequestWrapper = (GWTRPCSessionHttpServletRequestWrapper) httpRequest;
			stringBuilder.append(sessionHttpServletRequestWrapper.getWebId()).append(MutableCacheManager.WEB_ID_DELIMITER);
		} else {
            stringBuilder.append("null").append(MutableCacheManager.WEB_ID_DELIMITER);
        }

        stringBuilder.append(httpRequest.getMethod()).append(MutableCacheManager.WEB_ID_DELIMITER).append(httpRequest.getRequestURI())
				.append(MutableCacheManager.WEB_ID_DELIMITER).append(httpRequest.getQueryString());

        return stringBuilder.toString();
	}
	
	@Override
	public void doInit(FilterConfig filterConfig) throws CacheException {		
		synchronized (this.getClass()) {
            	setCacheNameIfAnyConfigured(filterConfig);
            	cacheManager.setName(getCacheName());
        }
	}

    @Override
	protected PreprocessedPageInfo buildPageInfo(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws Exception {
        // Look up the cached page
        final String key = calculateKey(request);
        PreprocessedPageInfo pageInfo;
		Element element = cacheManager.get(key);
		if (element == null || element.getObjectValue() == null) {
			try {
				// Page is not cached - build the response, cache it, and
				// send to client
				pageInfo = buildPage(request, response, chain);
				if (pageInfo.isOk()) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("PageInfo ok. Adding to cache " + cacheManager.getTargetCacheByKey(key).getName() + " with key " + key);
					}
                    cacheManager.put(new Element(key, pageInfo));
				} else {
					if (LOG.isDebugEnabled()) {
						LOG.debug("PageInfo was not ok(200). Putting null into cache " + cacheManager.getTargetCacheByKey(key).getName()
								+ " with key " + key);
					}
                    cacheManager.put(new Element(key, null));
				}
			} catch (final Throwable throwable) {
				LOG.error("Putting into cache " + cacheManager.getTargetCacheByKey(key).getName() + " failed with key " + key);
                cacheManager.put(new Element(key, null));
				throw new Exception(throwable);
			}
		} else {
			pageInfo = (PreprocessedPageInfo) element.getObjectValue();
		}
        return pageInfo;
    }
    
    @Override
    protected PreprocessedPageInfo buildPage(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws Exception {

    	// Invoke the next entity in the chain
        final ByteArrayOutputStream outstr = new ByteArrayOutputStream();
        GenericResponseWrapper wrapper = new GenericResponseWrapper(
                response, outstr);
        chain.doFilter(new CompressFreeHttpServletRequestWrapper(request), wrapper);
        wrapper.flush();

        Ehcache cache = getTargetCacheByRequest(request); 
        long timeToLiveSeconds = cache.getCacheConfiguration().getTimeToLiveSeconds();

        String outputString = new String( outstr.toByteArray(), Charset.defaultCharset());

        // Create the page info
        PreprocessedPageInfo pageInfo = new PreprocessedPageInfo(wrapper.getStatus(), wrapper.getContentType(),
                wrapper.getCookies(), MutableCacheManager.getResponseForClient(outputString).getBytes(), true,
                timeToLiveSeconds, wrapper.getAllHeaders(), MutableCacheManager.getResponseForCache(outputString));

        final List<Header<? extends Serializable>> headers = pageInfo.getHeaders();

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

        return pageInfo;
    }

    @Override
	protected CacheManager getCacheManager() {
        return cacheManager;
    }

    private Ehcache getTargetCacheByRequest(HttpServletRequest request){ 
    	String key = calculateKey(request);
    	return cacheManager.getTargetCacheByKey(key);
    }

 }