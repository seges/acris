/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.seges.acris.rpc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
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
			} else if("".equals(value)) {
				routes.add(new Route(key, defaultHost, defaultPort, null));
			} else {
				routes.add(new Route(key, defaultHost, defaultPort, value));
			}
		}
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

	public static class ValueHolder<T> {
		private T value;

		public ValueHolder() {
		}

		public ValueHolder(T value) {
			this.value = value;
		}

		public void setValue(T object) {
			this.value = object;
		}

		public T getValue() {
			return value;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final ValueHolder<T> other = (ValueHolder<T>) obj;
			if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hash = 5;
			hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
			return hash;
		}
	}
	
	public static void main(String[] args) {
        String line = ".*/fileDownload(.*)=/acris-server/fileDownload$1&webId=E14188588C4D6ACCCC65D30BEF939DD1A9C086BC";
        System.out.println(line.substring(0, line.indexOf('=')).trim());
        System.out.println(line.substring(line.indexOf('=') + 1).trim());
    }
}
