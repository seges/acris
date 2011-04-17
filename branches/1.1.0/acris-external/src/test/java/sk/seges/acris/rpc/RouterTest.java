package sk.seges.acris.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.regex.Matcher;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author eldzi
 */
public class RouterTest {
	private Router router;

	@Before
	public void setUp() throws Exception {
		router = new Router("target/test-classes/routes-test.properties");
	}

	@Test
	public void testCorrectRulesLoaded() {

		assertEquals("localhost", router.getDefaultHost());
		assertEquals(Integer.valueOf(8888), router.getDefaultPort());
		Route route = router.getRoute("/sk.seges.test.Me/image.png");
		assertNull(route.getTargetURI());

		String requestURI = "/sk.seges.test.Me/service/cms";
		route = new Route(".*sk\\.seges\\.test\\.Me(/service.*)",
				new ValueHolder<String>("localhost"),
				new ValueHolder<Integer>(8888), "$1");
//		route.setRequestURI(requestURI);

		assertEquals(route, router.getRoute(requestURI));
	}

	@Test
	public void testCorrectSourceToTargetURIReplace() {
		String uri = "/client/sk.seges.test.Me/service/cms";
		Route route = router.getRoute(uri);

//		 String redirectURI = route.getRe().subst(route.getRequestURI(), route.getTargetURI(), RE.REPLACE_BACKREFERENCES);
//		assertEquals("/service/cms", redirectURI);

		uri = "/client/skAsegesBtestCMe/service/cms";
		route = router.getRoute(uri);
		assertEquals(Router.NO_ROUTE, route);
	}

	@Test
	public void testAbsoluteTargetURL() {
		
		//Testing if default port is 8888 but redirected port should be differetn
		assertEquals("localhost", router.getDefaultHost());
		assertEquals(Integer.valueOf(8888), router.getDefaultPort());

		String requestURI = "/sk.seges.test.Absolute/service/test";
		assertEquals("localhost:8881/service/test", routeToUrl(router.getRoute(requestURI)));

		requestURI = "/sk.seges.web.template.Site.JUnit/acris-server/acris-service/gs";
		assertEquals("localhost:5881/synapso-acris-server/acris-service/gs", routeToUrl(router.getRoute(requestURI)));
	}

	public String routeToUrl(Route route) {
        Matcher matcher = route.getMatchedSourceURI();
        String redirectURI = matcher.replaceFirst(route.getTargetURI());
		return route.getHost() + ":" + route.getPort() + (redirectURI == null ? "" : redirectURI);
	}

}
