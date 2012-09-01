package sk.seges.acris.rpc;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;


public class ProxyServletTest {

    public static final int THREAD_COUNT = 1000;
    private ProxyServlet servlet;

    @Before
    public void setUp() {
        try {
            servlet = new ProxyServlet() {
                @Override
                protected void proxy(HttpServletRequest req, HttpServletResponse resp, Route route)
                        throws ServletException, IOException {
                    String redirectURI = null;
                    Matcher matcher = route.getMatchedSourceURI();
                    synchronized(matcher) {
                        redirectURI = matcher.replaceFirst(route.getTargetURI());
                    }
                    URL url = new URL(req.getScheme(), route.getHost(), route.getPort(), redirectURI);
                    assertEquals("http://localhost:8888/service/image.png", url.toString());
                }
            };
            servlet.init(new ProxyServletConfig());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private int i = 0;
    private int j = 0;
    private void incrementI() {
        i++;
    }

    private void printI() {
        System.out.println("" + i);
    }

    private void incrementJ() {
        j++;
    }

    private void printJ() {
        System.out.println("" + j);
    }


    @Test
    public void testServlet() throws Exception {
        long start = System.currentTimeMillis();
        Thread mainThread = Thread.currentThread();
        ValueHolder<Boolean> lastFinished = new ValueHolder<Boolean>();
        lastFinished.setValue(false);
        for (int ii = 0; ii < THREAD_COUNT; ++ii) {
            new TestThread(ii, mainThread, servlet, lastFinished).start();
        }
        while(true) {
            if(!Boolean.TRUE.equals(lastFinished.getValue())) {
                Thread.currentThread().sleep(100);
            } else {
                break; //FREE!!!!
            }
        }
        printI();
        printJ();

        System.out.println("it took me: " + (System.currentTimeMillis() - start));
    }

    private static class ProxyServletConfig implements ServletConfig {

        @Override
        public String getInitParameter(String name) {
            return "target/test-classes/routes-test.properties";
        }

        @Override
        public Enumeration getInitParameterNames() {
            return new StringTokenizer("");
        }

        @Override
        public ServletContext getServletContext() {
            return new ProxyServletContext();
        }

        @Override
        public String getServletName() {
            return "ProxyServlet";
        }
    }

    private static class ProxyServletContext implements ServletContext {

        @Override
        public Object getAttribute(String name) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Enumeration getAttributeNames() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ServletContext getContext(String uripath) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getContextPath() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getInitParameter(String name) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Enumeration getInitParameterNames() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getMajorVersion() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public String getMimeType(String file) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getMinorVersion() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public RequestDispatcher getNamedDispatcher(String name) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getRealPath(String path) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String path) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public URL getResource(String path) throws MalformedURLException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public InputStream getResourceAsStream(String path) {
            try {
                return new FileInputStream(path);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Set getResourcePaths(String path) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getServerInfo() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Servlet getServlet(String name) throws ServletException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getServletContextName() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Enumeration getServletNames() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Enumeration getServlets() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void log(String msg) {
            // TODO Auto-generated method stub

        }

        @Override
        public void log(Exception exception, String msg) {
            // TODO Auto-generated method stub

        }

        @Override
        public void log(String message, Throwable throwable) {
            // TODO Auto-generated method stub

        }

        @Override
        public void removeAttribute(String name) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setAttribute(String name, Object object) {
            // TODO Auto-generated method stub

        }

    }

    private final class TestThread extends Thread {

        public TestThread(final int precedency, final Thread mainThread, final ProxyServlet servlet, final ValueHolder<Boolean> lastFinished) {
            super(new Runnable() {

                @Override
                public void run() {
                    for (int jj = 0; jj < 1000; ++jj) {
                        try {
                            servlet.service(createRequest(), createResponse());
                            incrementI();
                        } catch (Throwable e) {
                            incrementJ();
                        }
                    }

                    System.out.println("" + precedency + " finishing");
                    if(precedency == THREAD_COUNT - 1) {
                        lastFinished.setValue(true);
                    }
                }


                private ServletRequest createRequest() {
                    HttpServletRequest request = new HttpServletRequest() {

                        @Override
                        public String getAuthType() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getContextPath() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public Cookie[] getCookies() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public long getDateHeader(String name) {
                            // TODO Auto-generated method stub
                            return 0;
                        }

                        @Override
                        public String getHeader(String name) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public Enumeration getHeaderNames() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public Enumeration getHeaders(String name) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public int getIntHeader(String name) {
                            // TODO Auto-generated method stub
                            return 0;
                        }

                        @Override
                        public String getMethod() {
                            return "POST";
                        }

                        @Override
                        public String getPathInfo() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getPathTranslated() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getQueryString() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getRemoteUser() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getRequestURI() {
                            return "/sk.seges.test.Me/service/image.png";
                        }

                        @Override
                        public StringBuffer getRequestURL() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getRequestedSessionId() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getServletPath() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public HttpSession getSession() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public HttpSession getSession(boolean create) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public Principal getUserPrincipal() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public boolean isRequestedSessionIdFromCookie() {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public boolean isRequestedSessionIdFromURL() {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public boolean isRequestedSessionIdFromUrl() {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public boolean isRequestedSessionIdValid() {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public boolean isUserInRole(String role) {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public Object getAttribute(String name) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public Enumeration getAttributeNames() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getCharacterEncoding() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public int getContentLength() {
                            // TODO Auto-generated method stub
                            return 0;
                        }

                        @Override
                        public String getContentType() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public ServletInputStream getInputStream() throws IOException {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getLocalAddr() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getLocalName() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public int getLocalPort() {
                            // TODO Auto-generated method stub
                            return 0;
                        }

                        @Override
                        public Locale getLocale() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public Enumeration getLocales() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getParameter(String name) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public Map getParameterMap() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public Enumeration getParameterNames() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String[] getParameterValues(String name) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getProtocol() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public BufferedReader getReader() throws IOException {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getRealPath(String path) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getRemoteAddr() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getRemoteHost() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public int getRemotePort() {
                            // TODO Auto-generated method stub
                            return 0;
                        }

                        @Override
                        public RequestDispatcher getRequestDispatcher(String path) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getScheme() {
                            return "http";
                        }

                        @Override
                        public String getServerName() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public int getServerPort() {
                            // TODO Auto-generated method stub
                            return 0;
                        }

                        @Override
                        public boolean isSecure() {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public void removeAttribute(String name) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setAttribute(String name, Object o) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
                            // TODO Auto-generated method stub

                        }

                    };
                    return request;
                }

                private ServletResponse createResponse() {
                    HttpServletResponse response = new HttpServletResponse() {

                        @Override
                        public void addCookie(Cookie cookie) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void addDateHeader(String name, long date) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void addHeader(String name, String value) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void addIntHeader(String name, int value) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public boolean containsHeader(String name) {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public String encodeRedirectURL(String url) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String encodeRedirectUrl(String url) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String encodeURL(String url) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String encodeUrl(String url) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public void sendError(int sc) throws IOException {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void sendError(int sc, String msg) throws IOException {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void sendRedirect(String location) throws IOException {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setDateHeader(String name, long date) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setHeader(String name, String value) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setIntHeader(String name, int value) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setStatus(int sc) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setStatus(int sc, String sm) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void flushBuffer() throws IOException {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public int getBufferSize() {
                            // TODO Auto-generated method stub
                            return 0;
                        }

                        @Override
                        public String getCharacterEncoding() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getContentType() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public Locale getLocale() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public ServletOutputStream getOutputStream() throws IOException {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public PrintWriter getWriter() throws IOException {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public boolean isCommitted() {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public void reset() {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void resetBuffer() {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setBufferSize(int size) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setCharacterEncoding(String charset) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setContentLength(int len) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setContentType(String type) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setLocale(Locale loc) {
                            // TODO Auto-generated method stub

                        }

                    };
                    return response;
                }

            });
        }
    }
}
