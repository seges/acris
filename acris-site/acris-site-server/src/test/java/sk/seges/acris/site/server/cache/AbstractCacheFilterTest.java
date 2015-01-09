package sk.seges.acris.site.server.cache;

import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.GenericResponseWrapper;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.core.server.utils.io.StringFile;
import sk.seges.acris.site.server.cache.configuration.spring.CacheFilterTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.core.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by PeterSimun on 23.11.2014.
 *
 * Abstract test case for testing the cache filter
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AbstractCacheFilterTest.CacheFilterTestConfigurationLoader.class)
public abstract class AbstractCacheFilterTest {

    protected final String EXTENSION = ".txt";
    protected final String URI_SUFFIX = "_request_uri";
    protected final String UNPROCESSED_RESPONSE_SUFFIX = "_response_unprocessed";
    protected final String PROCESSED_RESPONSE_SUFFIX = "_response_processed";

    static class CacheFilterTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

        public CacheFilterTestConfigurationLoader() {
            super(CacheFilterTestConfiguration.class);
        }
    }

    protected void executeTest(final String fileNamePrefix, MockServlet servlet) throws IOException, ServletException {
        executeTest(fileNamePrefix, getRequest(), servlet);
    }

    protected void executeTest(final String fileNamePrefix, MockHttpServletRequest request, MockServlet servlet) throws IOException, ServletException {

        byte[] response = executeFilterChain(
                setRequestURI(request, getTestResource(fileNamePrefix, URI_SUFFIX)),
                servlet
        );

        String expectedOutput = getTestResource(fileNamePrefix, PROCESSED_RESPONSE_SUFFIX);

        boolean result = compareBytes(response,
                gzip(expectedOutput.getBytes()));

        if (!result) {
            System.out.println("Expected output: " + expectedOutput);
            System.out.println("Actual output: " + new String(ungzip(response)));
        }

        Assert.assertTrue("Filter chain response does not produce expected output", result);

    }

    private static final int GZIP_MAGIC_NUMBER_BYTE_1 = 31;
    private static final int GZIP_MAGIC_NUMBER_BYTE_2 = -117;
    private static final int FOUR_KB = 4196;

    public static boolean isGzipped(byte[] candidate) {
        if (candidate == null || candidate.length < 2) {
            return false;
        } else {
            return (candidate[0] == GZIP_MAGIC_NUMBER_BYTE_1 && candidate[1] == GZIP_MAGIC_NUMBER_BYTE_2);
        }
    }

    private byte[] ungzip(final byte[] gzipped) throws IOException {
        final GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(gzipped));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(gzipped.length);
        final byte[] buffer = new byte[FOUR_KB];
        int bytesRead = 0;
        while (bytesRead != -1) {
            bytesRead = inputStream.read(buffer, 0, FOUR_KB);
            if (bytesRead != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        }
        byte[] ungzipped = byteArrayOutputStream.toByteArray();
        inputStream.close();
        byteArrayOutputStream.close();
        return ungzipped;
    }

    private byte[] gzip(byte[] ungzipped) throws IOException, AlreadyGzippedException {
        if (isGzipped(ungzipped)) {
            throw new AlreadyGzippedException("The byte[] is already gzipped. It should not be gzipped again.");
        }
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(bytes);
        gzipOutputStream.write(ungzipped);
        gzipOutputStream.close();
        return bytes.toByteArray();
    }


    protected boolean compareBytes(byte[] actual, byte[] expected) {
        if (expected == null) {
            if (actual != null) {
                System.out.println("Expected output is null, actual is not!");
                return false;
            } else {
                return true;
            }
        } else {
            if (actual == null) {
                System.out.println("Expected output is not null, actual is not!");
                return false;
            }
        }

        if (actual.length != expected.length) {
            System.out.println("Expected result and actual result does not have the same length!");
            System.out.print("Expected result: ");
            printBytes(expected);
            System.out.print("Actual result: ");
            printBytes(actual);
            return false;
        }

        for (int i = 0; i < actual.length; i++) {
            if (actual[i] != expected[i]) {
                System.out.println("Expected result and actual result differs on the position " + i + " on expected character '" + expected[i] + "' and actual character '" + actual[i] + "'." );
                return false;
            }
        }

        return true;
    }

    protected void printBytes(byte[] bytes) {
        int i = 0;
        for (byte b: bytes) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(b);
            i++;
        }
        System.out.println();
    }

    protected String getTestResource(String fileNamePrefix, String type) {
        final String packageName = getClass().getPackage().getName().replace('.',File.separatorChar);
        return getFileContent(packageName + File.separator + fileNamePrefix + File.separator + fileNamePrefix + type + EXTENSION);
    }

    protected String getFileContent(String fileName) {

        final StringFile file = StringFile.getFileDescriptor(fileName);

        try {
            return file.readTextFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    protected FilterChainProxy springSecurityFilterChain;

    @Autowired
    protected CacheFilter cacheFilter;

    @Autowired
    protected MutableCacheManager cacheManager;

    protected byte[] executeFilterChain(ServletRequest request, HttpServlet servlet) throws IOException, ServletException {

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final GenericResponseWrapper wrapper = new GenericResponseWrapper(getResponse(), outputStream);

        MockFilterChain chain = new MockFilterChain(servlet, cacheFilter);
        chain.doFilter(request, wrapper);

        wrapper.flush();
        return outputStream.toByteArray();
    }

    protected MockHttpServletRequest setRequestURI(MockHttpServletRequest request, String uri) {
        request.setRequestURI(uri);
        return request;
    }

    protected MockHttpServletResponse getResponse() {
        return new MockHttpServletResponse();
    }

    protected MockHttpServletRequest getRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("get");
        request.addHeader(HttpHeaders.ACCEPT, "*/*");
        request.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate,sdch");
        request.addHeader(HttpHeaders.ACCEPT_LANGUAGE, "sk,cs;q=0.8,en-US;q=0.6,en;q=0.4");
        request.addHeader(HttpHeaders.CONTENT_TYPE, "text/x-gwt-rpc; charset=utf-8");
        request.addHeader(HttpHeaders.COOKIE, "__cfduid=d0c536d7127f83ddad899b4d35d142c2b1416736887; _ga=GA1.2.1835855588.1416736887; _gat=1; acris-token=");
        request.addHeader(HttpHeaders.HOST, "localhost");
        request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");

        return request;
    }
}