package sk.seges.acris.site.server.cache;

import net.sf.ehcache.constructs.web.filter.Filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPOutputStream;

/**
 * Created by PeterSimun on 23.11.2014.
 *
 * Servlet used for cache testing purposes. It returns predefined response but counts how many times is servlet
 * executed.
 *
 */
public abstract class MockServlet extends HttpServlet {

    public static final String CHARSET_UTF8_NAME = "UTF-8";
    public static final Charset CHARSET_UTF8 = Charset.forName(CHARSET_UTF8_NAME);
    private static final String CONTENT_ENCODING_GZIP = "gzip";

    private int count = 0;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        count++;

        Object attribute = req.getAttribute(CacheFilter.EHCACHE_ENABLED);

        if (attribute != null && attribute.equals(false) && acceptsGzipEncoding(req)) {
            gzip(resp.getOutputStream(), getResponseText());
        } else {
            resp.getWriter().write(getResponseText());
        }
    }

    public static boolean acceptsGzipEncoding(HttpServletRequest request) {
        assert (request != null);

        String acceptEncoding = request.getHeader(HttpHeaders.ACCEPT_ENCODING);
        if (null == acceptEncoding) {
            return false;
        }

        return (acceptEncoding.indexOf(CONTENT_ENCODING_GZIP) != -1);
    }

    protected void gzip(OutputStream outputStream, String responseContent) throws IOException {

        byte[] responseBytes = responseContent.getBytes(CHARSET_UTF8);

        try {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
            gzipOutputStream.write(responseBytes);
            gzipOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getExecutionCount() {
        return count;
    }

    protected abstract String getResponseText();
}
