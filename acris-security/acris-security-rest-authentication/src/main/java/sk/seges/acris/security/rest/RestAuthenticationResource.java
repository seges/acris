package sk.seges.acris.security.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.seges.acris.crypto.util.Hasher;
import sk.seges.acris.security.server.core.user_management.context.APIKeyConstants;

@Path("/auth")
public class RestAuthenticationResource {
	
	private static final Logger log = LoggerFactory.getLogger(RestAuthenticationResource.class);

	/**
	 * Return JSON for authentication
	 * {allowed: “true”/”false”}
	 */
	@GET
	@Path("/user")
	@Produces({ MediaType.APPLICATION_JSON })
	public JSONObject authenticate(@QueryParam("webId") String webId, @QueryParam("apiKey") String apiKey) {
		JSONObject result = null;
		try {
			if (webId != null && (Hasher.getSHAHexDigest(webId).equals(apiKey))) {
				result = new JSONObject().put(APIKeyConstants.RESULT_PARAMETER, "TRUE");
			} else {
				result = new JSONObject().put(APIKeyConstants.RESULT_PARAMETER, "FALSE");
			}
		} catch (JSONException e) {
			log.error("Could not parse json, cause:" , e);
		}
		return result;
	}
	
	/**
	 * Return JSON with user and password for 
	 * FTP authentication, where to publish ofline version of the web
	 * {allowed: “true”/”false”, user: “test_user”,password: “test_pass”}
	 */
	@GET
	@Path("/userftp")
	@Produces({ MediaType.APPLICATION_JSON })
	public JSONObject auth(@QueryParam("webId") String webId, @QueryParam("apiKey") String apiKey) {
		JSONObject result = null;
		try {
			if (webId != null && (Hasher.getSHAHexDigest(webId).equals(apiKey))) {
				result = new JSONObject();
				result.put(APIKeyConstants.RESULT_PARAMETER, "TRUE");
				result.put(APIKeyConstants.RESULT_PARAMETER_FTP_USER, "test_user");
				result.put(APIKeyConstants.RESULT_PARAMETER_FTP_PASS, "test_pass");
			} else {
				result = new JSONObject().put(APIKeyConstants.RESULT_PARAMETER, "FALSE");
			}
		} catch (JSONException e) {
			log.error("Could not parse json, cause:" , e);
		}
		return result;
	}

}
