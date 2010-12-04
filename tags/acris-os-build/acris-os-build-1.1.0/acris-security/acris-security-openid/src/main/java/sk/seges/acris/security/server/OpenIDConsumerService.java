package sk.seges.acris.security.server;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.FetchRequest;

import sk.seges.acris.security.shared.IOpenIDConsumerService;
import sk.seges.acris.security.shared.data.OpenIDUser;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class OpenIDConsumerService extends RemoteServiceServlet implements IOpenIDConsumerService {

	private static final long serialVersionUID = 530902889319575560L;
	private Logger log = Logger.getLogger(OpenIDConsumerService.class);
	private ConsumerManager manager;

	private ConsumerManager getManager() {
		if (manager == null) {
			try {
				manager = new ConsumerManager();
			} catch (ConsumerException e) {
				log.error("Error while creating openID consumer manager", e);
			}
		}
		return manager;
	}

	@Override
	public OpenIDUser authenticate(final String userSuppliedString, final String returnToUrl) {
		try {
			// perform discovery on the user-supplied identifier
			List<?> discoveries = getManager().discover(userSuppliedString);

			// attempt to associate with the OpenID provider
			// and retrieve one service endpoint for authentication
			DiscoveryInformation discovered = getManager().associate(discoveries);

			// store the discovery information in the user's session
			HttpServletRequest threadLocalRequest = getThreadLocalRequest();
			HttpSession session = threadLocalRequest.getSession();
			session.setAttribute("openid-disc", discovered);

			// obtain a AuthRequest message to be sent to the OpenID provider
			AuthRequest authReq = getManager().authenticate(discovered, returnToUrl);

			// attribute Exchange: fetching the 'email' attribute
			FetchRequest fetch = FetchRequest.createFetchRequest();
			fetch.addAttribute("email", // attribute alias
					"http://schema.openid.net/contact/email", // type URI
					true); // required
			// attach the extension to the authentication request
			authReq.addExtension(fetch);

			// simple POJO for storing the data
			OpenIDUser user = new OpenIDUser();

			// in normal servlet development the following statement would
			// make a redirect call, but this would not work when using GWT RPC
			if (!discovered.isVersion2()) {
				user.setRedirectUrl(authReq.getDestinationUrl(true));
			} else {
				user.setParams(authReq.getParameterMap());
				user.setRedirectUrl(authReq.getDestinationUrl(true));
			}
			// fakes the redirect by sending the POJO with the required
			// parameters to make a client-side redirect
			return user;
		} catch (OpenIDException e) {
			log.error("Errow while creating openID authentication request", e);
		}

		return null;
	}

	@Override
	public String verify(final String queryString, final Map<String, String[]> parameterMap) {
		try {
			// extract the parameters from the authentication response (which
			// comes in as a HTTP request from the OpenID provider)
			ParameterList response = new ParameterList(parameterMap);

			// retrieve the previously stored discovery information
			HttpServletRequest threadLocalRequest = getThreadLocalRequest();
			HttpSession session = threadLocalRequest.getSession();
			DiscoveryInformation discovered = (DiscoveryInformation) session.getAttribute("openid-disc");

			// verify the response; ConsumerManager needs to be the same
			// (static) instance used to place the authentication request
			VerificationResult verification = manager.verify(queryString, response, discovered);

			// examine the verification result and extract the verified
			// identifier
			Identifier verified = verification.getVerifiedId();
			if (verified != null) {
				return verified.getIdentifier(); // success
			}
		} catch (Exception e) {
			log.error("Error while verifying openID authentication response", e);
		}

		return null;
	}
}
