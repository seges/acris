/*
 * Copyright (C) 2008 Lombardi Software
 * http://www.lombardi.com/
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Lombardi Software nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL LOMBARDI
 * SOFTWARE OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package sk.seges.acris.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Proxy servlet forwarding server requests to required host/port/URL. Also forwarding testcase requests. Client part code is as is.
 *
 * Thanks to Lombardi Ltd.
 *
 * @author amoffat Alex Moffat
 * @author eldzi
 */
public class ProxyServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(ProxyServlet.class);

	private static final long serialVersionUID = -5263018737729267586L;
	private static final String ROUTING_FILE = "routingFile";
	private static final String CLASSPATH_ROUTING_FILE = "/sk/seges/acris/rpc/routes.properties";
	private String routingFile = "/WEB-INF/routes.properties";

	private Router router;

    // List of the tests classes
    private List<String> testCases = new ArrayList<String>();

    private ServletConfig servletConfig;

    public void init(ServletConfig servletConfig) throws ServletException {
    	this.servletConfig = servletConfig;

		String customRoutingFile = servletConfig.getInitParameter(ROUTING_FILE);
		if(customRoutingFile != null && !"".equals(customRoutingFile)) {
			routingFile = customRoutingFile.trim();
			if(log.isDebugEnabled())
				log.debug("Using custom routing file = " + routingFile);
		}

		InputStream routingFileStream = servletConfig.getServletContext().getResourceAsStream(routingFile);
		if(routingFileStream == null) {
			routingFileStream = getClass().getResourceAsStream(CLASSPATH_ROUTING_FILE);
			if(log.isInfoEnabled()) {
				log.info("Using default classpath routing file = " + getClass().getResource(CLASSPATH_ROUTING_FILE));
			}
		}
		router = new Router(routingFileStream);

		// TODO: this is deprecated, implement via Regexp in routes
        // Any paramter whose name starts with TestCase is the name of a test case class. These should not be forwarded.
        Enumeration<?> paramNames = servletConfig.getInitParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();
            if (name.startsWith("TestCase")) {
                testCases.add("/" + servletConfig.getInitParameter(name));
            }
        }
        super.init(servletConfig);
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String uri = req.getRequestURI();
        if (req.getQueryString() != null) {
            uri += "?" + req.getQueryString();
        }

        if(log.isDebugEnabled())
        	log.debug("Request URI = " + uri);

        Route route = router.getRoute(uri);

        boolean proxy;
        if (uri.startsWith("/favicon.ico")) {
            proxy = false;
        } else if(!Router.NO_ROUTE.equals(route)) {
        	proxy = true;
        } else {
        	proxy = false;
        }

		//TODO: deprecated... fix with Regex in routes
//		else {
//            for (String testCase : testCases) {
//                if (uri.startsWith(testCase)) {
//                    proxy = false;
//                    break;
//                }
//            }
//        }

        if (proxy) {
            proxy(req, resp, route);
        } else {
        	if(log.isDebugEnabled())
            	log.debug("ProxyServlet serving locally " + uri);
//            super.service(servletRequest, servletResponse);
        	findBaseDispatcher().forward(req, resp);
        }
    }

    private RequestDispatcher findBaseDispatcher() {
    	RequestDispatcher dispatcher = servletConfig.getServletContext().getNamedDispatcher("default");
    	if(dispatcher == null)
    		dispatcher = servletConfig.getServletContext().getNamedDispatcher("shell");
    	if(dispatcher == null)
    		throw new RuntimeException("Cannot dispatch to base servlet - none available");

    	return dispatcher;
    }

    /**
     * Proxy a request to a different server
     *
     * @param req request
     * @param resp response
     * @param uri uri to proxy
     * @throws ServletException in case of error
     * @throws IOException in case of error
     */
    protected void proxy(HttpServletRequest req, HttpServletResponse resp, Route route)
            throws ServletException, IOException {
        String redirectURI = null;
        Matcher matcher = route.getMatchedSourceURI();
        synchronized(matcher) {
            redirectURI = matcher.replaceFirst(route.getTargetURI());
        }
    	if(log.isDebugEnabled())
        	log.debug("ProxyServlet forwarding " + req.getMethod() + " to " + req.getScheme() + "://" + route.getHost() + ":" + route.getPort() + ", uri = " + redirectURI);

        URL url = new URL(req.getScheme(), route.getHost(), route.getPort(), redirectURI);

        URLConnection connection = url.openConnection();
        connection.setAllowUserInteraction(false);

        HttpURLConnection httpConnection = null;
        if (connection instanceof HttpURLConnection) {
            httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod(req.getMethod());
            httpConnection.setInstanceFollowRedirects(false);
        }

        boolean hasContent = false;
        Enumeration<?> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = (String) headerNames.nextElement();
            if ("content-type".equals(name.toLowerCase())) {
                hasContent = true;
            }
            Enumeration<?> values = req.getHeaders(name);
            while (values.hasMoreElements()) {
                String value = (String) values.nextElement();
                if (value != null) {
                    connection.addRequestProperty(name, value);
                }
            }
        }

        connection.setDoInput(true);

        propagateCookies(req, connection);
        
        InputStream in = req.getInputStream();
        // setDoOutput to true with change the method from GET to POST so only do it if the method is POST already.
        // POST is the method that has content anyway
        if ("POST".equals(httpConnection.getRequestMethod()) && hasContent) {
            connection.setDoOutput(true);
            copy(in, connection.getOutputStream());
        }
        connection.connect();

        InputStream proxyIn = null;

        int code = 500;

        if (httpConnection != null) {
            proxyIn = httpConnection.getErrorStream();
            code = httpConnection.getResponseCode();
            resp.setStatus(code);
        }
        if (proxyIn == null) {
            try {
                proxyIn = connection.getInputStream();
            } catch (IOException e) {
                if (httpConnection != null) {
                    proxyIn = httpConnection.getErrorStream();
                }
            }
        }

        resp.setHeader("Date", null);
        resp.setHeader("Server", null);

        int h = 0;
        String headerName = connection.getHeaderFieldKey(h);
        String headerValue = connection.getHeaderField(h);
        while (headerName != null || headerValue != null) {
            if (headerName != null && headerValue != null) {
                if (!router.skipHeader(headerName)) {
                	resp.addHeader(headerName, headerValue);
                }
            }
            h++;
            headerName = connection.getHeaderFieldKey(h);
            headerValue = connection.getHeaderField(h);
        }

        if (proxyIn != null) {
            copy(proxyIn, resp.getOutputStream());
        }
        resp.flushBuffer();
    }

    private void propagateCookies(HttpServletRequest request, URLConnection connection) {
    	Cookie[] cookies = request.getCookies();
    	if(cookies == null) {
    		return;
    	}
    	StringBuilder value = new StringBuilder();
    	for(int i = 0; i < cookies.length; i++) {
    		Cookie cookie = cookies[i];
    		value.append(cookie.getName() + "=" + cookie.getValue());
    		if(i != cookies.length - 1) {
    			value.append(";");
    		}
    	}
    	connection.setRequestProperty("Cookie", value.toString());
    }
    
    /**
     * Copy all of in to out.
     *
     * @param in input stream to read from
     * @param out output stream to write to
     * @throws IOException thrown if things go wrong
     */
    private void copy(InputStream in, OutputStream out)
            throws IOException {

        byte[] buffer = new byte[2*88192];
        int len = 0;
        while (true) {
            len = in.read(buffer, 0, buffer.length);
            if (len < 0) break;
            out.write(buffer, 0, len);
        }
    }
}
