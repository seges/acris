package sk.seges.acris.site.server.cache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.core.HttpHeaders;

/**
 * Created by PeterSimun on 22.11.2014.
 */
public class CompressFreeHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public static final String CONTENT_ENCODING_GZIP = "gzip";

    public CompressFreeHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    private HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) super.getRequest();
    }

    @Override
    public String getHeader(String name) {

        if (!name.toLowerCase().equals(HttpHeaders.ACCEPT_ENCODING.toLowerCase())) {
            return super.getHeader(name);
        }

        String acceptEncoding = super.getHeader(HttpHeaders.ACCEPT_ENCODING);
        if (null == acceptEncoding) {
            return null;
        }

        acceptEncoding = acceptEncoding.replaceAll(CONTENT_ENCODING_GZIP, "");
        acceptEncoding.replaceAll(",,", ",");
        if (acceptEncoding.startsWith(",")) {
            acceptEncoding = acceptEncoding.substring(1);
        } else if (acceptEncoding.endsWith(",")) {
            acceptEncoding = acceptEncoding.substring(0, acceptEncoding.length() - 1);
        }

        return acceptEncoding;
    }
}
