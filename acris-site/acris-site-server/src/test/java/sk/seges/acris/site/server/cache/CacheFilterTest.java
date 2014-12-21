package sk.seges.acris.site.server.cache;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

/**
 * Created by PeterSimun on 23.11.2014.
 */
public class CacheFilterTest extends AbstractCacheFilterTest {

    private static final String TEST_NAME = "basic";

    @Test
    public void testCacheFilter() throws Exception {

        MockServlet servlet = new MockServlet() {
            @Override
            protected String getResponseText() {
                return getTestResource(TEST_NAME, UNCOMPRESSED_RESPONSE_SUFFIX);
            }
        };

        executeTest(TEST_NAME, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", servlet.getExecutionCount(), 1);

        executeTest(TEST_NAME, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", servlet.getExecutionCount(), 1);
    }

    @Test
    public void testDisableCacheFilterByPostMethod() throws Exception {

        MockServlet servlet = new MockServlet() {
            @Override
            protected String getResponseText() {
                return getTestResource(TEST_NAME, UNCOMPRESSED_RESPONSE_SUFFIX);
            }
        };

        MockHttpServletRequest request = getRequest();
        request.setMethod("post");

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", servlet.getExecutionCount(), 1);

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should two executions of the servlet", servlet.getExecutionCount(), 2);
    }

    @Test
    public void testDisableCacheFilterByReferrer() throws Exception {

        MockServlet servlet = new MockServlet() {
            @Override
            protected String getResponseText() {
                return getTestResource(TEST_NAME, UNCOMPRESSED_RESPONSE_SUFFIX);
            }
        };

        MockHttpServletRequest request = getRequest();
        request.addHeader("referer", "gwt.codesvr=localhost:9997"); //TODO typo = referrer

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", servlet.getExecutionCount(), 1);

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should two executions of the servlet", servlet.getExecutionCount(), 2);
    }

    @Test
    public void testDisableCacheFilterByAdmin() throws Exception {

        MockServlet servlet = new MockServlet() {
            @Override
            protected String getResponseText() {
                return getTestResource(TEST_NAME, UNCOMPRESSED_RESPONSE_SUFFIX);
            }
        };

        MockHttpServletRequest request = getRequest();
        request.getSession(true).setAttribute(LoginConstants.LOGIN_TOKEN_NAME, new LoginToken() {
            @Override
            public String getWebId() {
                return "mock-webid";
            }

            @Override
            public boolean isAdmin() {
                return true;
            }
        });

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should be only one execution of the servlet", servlet.getExecutionCount(), 1);

        executeTest(TEST_NAME, request, servlet);
        Assert.assertEquals("There should two executions of the servlet", servlet.getExecutionCount(), 2);
    }
}