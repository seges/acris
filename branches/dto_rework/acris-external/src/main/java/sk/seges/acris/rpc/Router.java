/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.seges.acris.rpc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 *
 * @author eldzi
 */
public class Router {
	public static final Route NO_ROUTE = new Route("noRoute", new ValueHolder<String>(), new ValueHolder<Integer>(), "noRouteTarget");
	private final List<Route> routes;
	private final ValueHolder<String> defaultHost;
	private final ValueHolder<Integer> defaultPort;
	private String[] skipHeaders;

	private static final String HEADER_DELIMITER = ";";
	
	public Router(String fileName) throws FileNotFoundException {
		this(new FileInputStream(new File(fileName)));
	}
	
	public Router(InputStream inputStream) {
		routes = new LinkedList<Route>();
		defaultHost = new ValueHolder<String>();
		defaultPort = new ValueHolder<Integer>();

		Scanner scanner = new Scanner(inputStream);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			if("".equals(line) || line.startsWith("#")) {
				continue;
			}

//			StringTokenizer st = new StringTokenizer(line, "=");
//
//			String key = st.nextToken().trim();
//			String value = st.hasMoreTokens() ? st.nextToken().trim() : "";
			String key = line.substring(0, line.indexOf('=')).trim();
			String value = line.substring(line.indexOf('=') + 1).trim();
			if(key.equals("*default-host")) {
				defaultHost.setValue(value);
			} else if(key.equals("*default-port")) {
				defaultPort.setValue(Integer.parseInt(value));
			} else if (key.equals("*skip-headers")) {
				skipHeaders = value.split(HEADER_DELIMITER);
			} else {
				Route route = createRoute(key, defaultHost, defaultPort, "".equals(value) ? null : value);
				if (route != null) {
					routes.add(route);
				}
			}
		}
	}

	private Route createRoute(String sourceURI, ValueHolder<String> host, ValueHolder<Integer> port, String targetURI) {
		if (sourceURI == null) {
			return null;
		}
		//We will support only this option. Yes, there plenty of other possibilities, but we want to have proxyServlet fast
		if (targetURI != null && targetURI.startsWith("http://")) {
			try {
				return new Route(sourceURI, targetURI);
			} catch (MalformedURLException e) {
				return null;
			}
		}
		
		return new Route(sourceURI, defaultHost, defaultPort, targetURI);
	}
	
	public boolean skipHeader(String headerName) {
		if (skipHeaders == null) {
			return false;
		}
		
		for (String skipHeader : skipHeaders) {
			if (skipHeader != null && skipHeader.equals(headerName)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected String getDefaultHost() {
		return defaultHost.getValue();
	}

	protected Integer getDefaultPort() {
		return defaultPort.getValue();
	}

	public Route getRoute(String requestURI) {
		for(Route route : routes) {
			Matcher m = route.getSourceURI().matcher(requestURI);
			if(m.find(0)) {
				route.setMatchedSourceURI(m);
				return route;
			}
		}
		return NO_ROUTE;
	}

	public static void main(String[] args) {
        String line = ".*/fileDownload(.*)=/acris-server/fileDownload$1&webId=E14188588C4D6ACCCC65D30BEF939DD1A9C086BC";
        System.out.println(line.substring(0, line.indexOf('=')).trim());
        System.out.println(line.substring(line.indexOf('=') + 1).trim());
    }
}
