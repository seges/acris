package sk.seges.acris.site.server.cache;


import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
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
import java.util.concurrent.TimeUnit;

public class CacheFilter extends SimpleCachingHeadersPageCachingFilter {

	private static final String CODESVR = "__gwt.codesvr";
    private static final Logger LOG = LoggerFactory.getLogger(CacheFilter.class);
    private static final long ONE_YEAR_IN_MILLISECONDS = 60 * 60 * 24 * 365 * 1000L;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    
    private final MutableCacheManager cacheManager;
        
	public CacheFilter(MutableCacheManager cacheManager) {
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
			stringBuffer.append(sessionHttpServletRequestWrapper.getWebId() + MutableCacheManager.WEB_ID_DELIMITER);
		}
		stringBuffer.append(httpRequest.getMethod()).append(httpRequest.getRequestURI())
				.append(httpRequest.getQueryString());
		return stringBuffer.toString();
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

    	// Invoke the next entityprovider in the chain
        final ByteArrayOutputStream outstr = new ByteArrayOutputStream();
        GenericResponseWrapper wrapper = new GenericResponseWrapper(
                response, outstr);
        chain.doFilter(new CompressFreeHttpServletRequestWrapper(request), wrapper);
        wrapper.flush();

        //boolean shouldCompress = RPCServletUtils.shouldGzipResponseContent(request, outputString);

//        if (RPCServletUtils.shouldGzipResponseContent(request, outputString)) {
//            GZIPOutputStream gzipOutputStream = null;
//            ByteArrayOutputStream output = null;
//            try {
//                output = new ByteArrayOutputStream(outstr.toByteArray().length);
//                gzipOutputStream = new GZIPOutputStream(output);
//                gzipOutputStream.write(outstr.toByteArray());
//                gzipOutputStream.finish();
//                gzipOutputStream.flush();
//
//                wrapper = new GenericResponseWrapper(response, output);
//                wrapper.setHeader(HttpHeaders.CONTENT_ENCODING, CompressFreeHttpServletRequestWrapper.CONTENT_ENCODING_GZIP);
//                outstr.reset();
//                outstr.write(output.toByteArray());
//            } catch (IOException e) {
//                LOG.info("Unable to gzip response", e);
//            } finally {
//                if (null != gzipOutputStream) {
//                    gzipOutputStream.close();
//                }
//                if (null != output) {
//                    output.close();
//                }
//            }
//        }

        Ehcache cache = getTargetCacheByRequest(request); 
        long timeToLiveSeconds = cache.getCacheConfiguration().getTimeToLiveSeconds();

        String outputString = new String( outstr.toByteArray(), Charset.defaultCharset());

        // Create the page info
        PreprocessedPageInfo pageInfo = new PreprocessedPageInfo(wrapper.getStatus(), wrapper.getContentType(),
                wrapper.getCookies(), cacheManager.getResponseForClient(outputString).getBytes(), true,
                timeToLiveSeconds, wrapper.getAllHeaders(), outputString, cacheManager.getResponseForCache(outputString));

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
        

    private Ehcache getTargetCacheByRequest(HttpServletRequest request){ 
    	String key = calculateKey(request);
    	return cacheManager.getTargetCacheByKey(key);
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