package sk.seges.acris.site.server.cache;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import sk.seges.acris.security.server.core.request.session.GWTRPCSessionHttpServletRequestWrapper;
import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.site.server.cache.model.MockDomainEntity;

/**
 * Created by PeterSimun on 23.11.2014.
 */
public class CacheFilterTest extends AbstractCacheFilterTest {

    private static final String TEST_NAME = "basic";
    private static final String NO_CACHE_TEST_NAME = "nocache";

    @After
    public final void tearDown() {
        cacheManager.clearCache(null, null);
    }

    @Test
    public void testCacheFilter() throws Exception {

        MockServlet servlet = new MockServlet() {
            @Override
            protected String getResponseText() {
                return getTestResource(TEST_NAME, UNPROCESSED_RESPONSE_SUFFIX);
            }
        };

        executeTest(TEST_NAME, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 1, servlet.getExecutionCount());

        executeTest(TEST_NAME, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 1, servlet.getExecutionCount());
    }

    @Test
    public void testDisableCacheFilterByPostMethod() throws Exception {

        MockServlet servlet = new MockServlet() {
            @Override
            protected String getResponseText() {
                return getTestResource(NO_CACHE_TEST_NAME, PROCESSED_RESPONSE_SUFFIX);
            }
        };

        MockHttpServletRequest request = getRequest();
        request.setMethod("post");

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 1, servlet.getExecutionCount());

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should two executions of the servlet", 2, servlet.getExecutionCount());
    }

    @Test
    public void testDisableCacheFilterByAdmin() throws Exception {

        final String WEB_ID = "mock-webid";

        MockServlet servlet = new MockServlet() {
            @Override
            protected String getResponseText() {
                return getTestResource(NO_CACHE_TEST_NAME, PROCESSED_RESPONSE_SUFFIX);
            }
        };

        MockHttpServletRequest request = getRequest();
        request.getSession(true).setAttribute(LoginConstants.LOGIN_TOKEN_NAME, new LoginToken() {
            @Override
            public String getWebId() {
                return WEB_ID;
            }

            @Override
            public boolean isAdmin() {
                return true;
            }
        });

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 1, servlet.getExecutionCount());

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should two executions of the servlet", 2, servlet.getExecutionCount());
    }

    @Test
    public void testInvalidateCache() throws Exception {

        MockServlet servlet = new MockServlet() {
            @Override
            protected String getResponseText() {
                return getTestResource(TEST_NAME, UNPROCESSED_RESPONSE_SUFFIX);
            }
        };

        MockHttpServletRequest request = getRequest();

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 1, servlet.getExecutionCount());

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 1, servlet.getExecutionCount());

        cacheManager.invalidate(MockDomainEntity.class.getCanonicalName(), 20L);

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should two executions of the servlet", 2, servlet.getExecutionCount());

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should two executions of the servlet", 2, servlet.getExecutionCount());
    }
    
    @Test
    public void testCacheMaxSize() throws Exception {
        
        MockServlet servlet = new MockServlet() {
            @Override
            protected String getResponseText() {
                return getTestResource(TEST_NAME, UNPROCESSED_RESPONSE_SUFFIX);
            }
        };

        MockHttpServletRequest request = getRequest();
        
        int cacheSize = cacheManager.getCacheSize(null);
        Assert.assertEquals("Size of cached elements must be 0", 0, cacheSize);
        
		request.setQueryString("query1");
		executeTest(TEST_NAME, request, servlet);
		cacheSize = cacheManager.getCacheSize(null);
		Assert.assertEquals("Size of cached elements must be 1", 1, cacheSize);
        int maxElements = cacheManager.getMaxElementsInMemory(null);
        for(int i = 2; i <= maxElements; i++){
        	 request.setQueryString("query"+i);
             executeTest(TEST_NAME, request, servlet);
             cacheSize = cacheManager.getCacheSize(null);
             Assert.assertEquals("Size of cached elements must be "+i, i, cacheSize);
        }
        
		request.setQueryString("query" + (maxElements + 1));
		executeTest(TEST_NAME, request, servlet);
		cacheSize = cacheManager.getCacheSize(null);
		Assert.assertEquals("Size of cached elements must be "+maxElements, maxElements, cacheSize);        
    }

    @Test
    public void testDifferentSessionId() throws Exception {
        MockServlet servlet = new MockServlet() {
            @Override
            protected String getResponseText() {
                return getTestResource(TEST_NAME, UNPROCESSED_RESPONSE_SUFFIX);
            }
        };
        
        MockHttpServletRequest request = getRequest();
        request.setQueryString("");
        GWTRPCSessionHttpServletRequestWrapper wrapper = new GWTRPCSessionHttpServletRequestWrapper(request);

        //no session id
        executeTest(TEST_NAME, wrapper, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 1, servlet.getExecutionCount());

        executeTest(TEST_NAME, wrapper, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 1, servlet.getExecutionCount());

        //first session id
        request.setQueryString("89877F90C0D0F265D8C21437C067559D");
        
        executeTest(TEST_NAME, wrapper, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 2, servlet.getExecutionCount());

        executeTest(TEST_NAME, wrapper, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 2, servlet.getExecutionCount());
        
        //other session id
        request.setQueryString("9015A02089C15A0E2AC44CEA543F608A");
        
        executeTest(TEST_NAME, wrapper, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 3, servlet.getExecutionCount());

        executeTest(TEST_NAME, wrapper, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", 3, servlet.getExecutionCount());
    }
}