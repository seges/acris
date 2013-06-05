package sk.seges.acris.security.server.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.openid4java.OpenIDException;
import org.openid4java.association.AssociationSessionType;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;

import sk.seges.acris.security.server.core.session.ServerSessionProvider;
import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.dto.OpenIDUserDTO;
import sk.seges.acris.security.shared.service.IOpenIDConsumerRemoteService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;

public class IOpenIDConsumerRemoteServiceImpl extends RemoteServiceServlet implements IOpenIDConsumerRemoteService {

	private static final long serialVersionUID = 530902889319575560L;
	private Logger log = Logger.getLogger(IOpenIDConsumerRemoteServiceImpl.class);
	private ConsumerManager manager;
	private ServerSessionProvider sessionProvider;

	@Inject
	public IOpenIDConsumerRemoteServiceImpl(ConsumerManager manager, ServerSessionProvider sessionProvider) {
		this.manager = manager;
		this.manager.setAssociations(new InMemoryConsumerAssociationStore());
		this.manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
        this.manager.setMinAssocSessEnc(AssociationSessionType.DH_SHA256);
		//this.manager.getRealmVerifier().setEnforceRpId(false);
		this.sessionProvider = sessionProvider;
	}

	private ConsumerManager getManager() {
		return manager;
	}

	private HttpSession getSession() {
		return sessionProvider.getSession();
	}
	
	@Override
	public OpenIDUserDTO authenticate(String userSuppliedString, String returnToUrl, String realm, boolean appendSessionId) {
		log.error("OpenID authenticate with manager: " + manager.toString() + ", instance: " + System.identityHashCode(manager));
		try {
			// perform discovery on the user-supplied identifier
			List<?> discoveries = getManager().discover(userSuppliedString);

			// attempt to associate with the OpenID provider
			// and retrieve one service endpoint for authentication
			DiscoveryInformation discovered = getManager().associate(discoveries);

			// store the discovery information in the user's session
			HttpSession session = getSession();
			session.setAttribute("openid-disc", discovered);

			if (appendSessionId) {
				returnToUrl +=  "&" + LoginConstants.ACRIS_SESSION_ID_STRING + "=" + session.getId();
			}
			// obtain an AuthRequest message to be sent to the OpenID provider
			AuthRequest authReq = getManager().authenticate(discovered, returnToUrl, realm);

			// attribute exchange: fetching the 'email' attribute
			FetchRequest fetch = FetchRequest.createFetchRequest();
			fetch.addAttribute("email", // attribute alias
					"http://schema.openid.net/contact/email", // type URI
					true); // required
			// attach the extension to the authentication request
			authReq.addExtension(fetch);

			// use simple registration to fetch the 'email' attribute
			SRegRequest sregReq = SRegRequest.createFetchRequest();
			sregReq.addAttribute("email", true);
			authReq.addExtension(sregReq);

			// simple POJO for storing the data
			OpenIDUserDTO userDTO = new OpenIDUserDTO();
			userDTO.getParams().put(OpenIDUserDTO.SESSION_ID, session.getId());
			userDTO.getParams().put(OpenIDUserDTO.ENDPOINT_URL, authReq.getDestinationUrl(true));

			// fakes the redirect by sending the POJO with the required
			// parameters to make a client-side redirect
			return userDTO;
		} catch (OpenIDException e) {
			log.error("Error while creating openID authentication request", e);
		}

		return null;
	}

	@Override
	public OpenIDUserDTO authenticate(final String userSuppliedString, final String returnToUrl, final String realm) {
		return authenticate(userSuppliedString, returnToUrl, realm, false);
	}

	@Override
	public OpenIDUserDTO verify(final String queryString, final Map<String, String[]> parameterMap) {
		log.error("OpenID verification with manager: " + manager.toString() + ", instance: " + System.identityHashCode(manager));
		try {
			// extract the parameters from the authentication response (which
			// comes in as a HTTP request from the OpenID provider)
			ParameterList response = new ParameterList(parameterMap);

			// retrieve the previously stored discovery information
			HttpSession session = getSession();
			DiscoveryInformation discovered = (DiscoveryInformation) session.getAttribute("openid-disc");

			// verify the response; ConsumerManager needs to be the same
			// (static) instance used to place the authentication request
			VerificationResult verification = getManager().verify(queryString, response, discovered);

			// examine the verification result and extract the verified
			// identifier
			Identifier verified = verification.getVerifiedId();
			if (verified != null) {
				AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();
				OpenIDUserDTO userDTO = new OpenIDUserDTO();

				userDTO.getParams().put(OpenIDUserDTO.OPENID_IDENTIFIER, authSuccess.getIdentity());

				if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
					FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);
					userDTO.getParams().put(OpenIDUserDTO.EMAIL_FROM_FETCH,
							(String) fetchResp.getAttributeValues("email").get(0));
				}
				if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
					SRegResponse sregResp = (SRegResponse) authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);
					userDTO.getParams().put(OpenIDUserDTO.EMAIL_FROM_SREG, sregResp.getAttributeValue("email"));
				}
				return userDTO; // success
			} else {
				log.error("Unable to verify using openId, status: " + verification.getStatusMsg());
			}
		} catch (Exception e) {
			log.error("Error while verifying openID authentication response", e);
		}

		return null;
	}
}
