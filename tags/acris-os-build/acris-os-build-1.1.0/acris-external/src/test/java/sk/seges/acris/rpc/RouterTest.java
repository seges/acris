package sk.seges.acris.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
}
