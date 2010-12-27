/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.seges.acris.rpc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Specifies target parameters of a route.
 *
 * @author eldzi
 */
public class Route {
	private final Pattern sourceURI;
	private transient Matcher matchedSourceURI;
	private final ValueHolder<String> host;
	private final ValueHolder<Integer> port;
	private final String targetURI;

	public Route(String sourceURI, ValueHolder<String> host, ValueHolder<Integer> port, String targetURI) {
		this.sourceURI = Pattern.compile(sourceURI);
		this.host = host;
		this.port = port;
		this.targetURI = targetURI;
	}

	public Pattern getSourceURI() {
		return sourceURI;
	}

	public String getHost() {
		return host.getValue();
	}

	public Integer getPort() {
		return port.getValue();
	}

	public String getTargetURI() {
		return targetURI;
	}
	
	public Matcher getMatchedSourceURI() {
		return matchedSourceURI;
	}
	
	public void setMatchedSourceURI(Matcher matchedSourceURI) {
		this.matchedSourceURI = matchedSourceURI;
	}

	

	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sourceURI == null) ? 0 : sourceURI.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Route other = (Route) obj;
		if (sourceURI == null) {
			if (other.sourceURI != null)
				return false;
		} else if (!sourceURI.pattern().equals(other.sourceURI.pattern()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Route target [" + host + ":" + port + ", uri = " + targetURI + "]";
	}
}
