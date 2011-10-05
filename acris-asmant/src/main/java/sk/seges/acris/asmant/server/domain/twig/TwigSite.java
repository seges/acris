package sk.seges.acris.asmant.server.domain.twig;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author ladislav.gazo
 */
public class TwigSite {
	@Key
	private String id;
	private String url;
	private String name;
	private String method;
	private String headers;
	private String payload;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "TwigSite [id=" + id + ", url=" + url + ", name=" + name
				+ ", method=" + method + ", headers=" + headers + ", payload="
				+ payload + "]";
	}
}
