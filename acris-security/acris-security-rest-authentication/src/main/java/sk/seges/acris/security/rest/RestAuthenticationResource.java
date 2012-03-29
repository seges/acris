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

@Path("/auth")
public class RestAuthenticationResource {
	
	private static final Logger log = LoggerFactory.getLogger(RestAuthenticationResource.class);

	@GET
	@Path("/user")
	@Produces({ MediaType.APPLICATION_JSON })
	public JSONObject authenticate(@QueryParam("webId") String webId, @QueryParam("apiKey") String apiKey) {
		JSONObject result = null;
		try {
			if (webId != null && (Hasher.getSHAHexDigest(webId).equals(apiKey))) {
				result = new JSONObject().put("allowed", true);
			} else {
				result = new JSONObject().put("allowed", false);
			}
		} catch (JSONException e) {
			log.error("Could not parse json, cause:" , e);
		}
		return result;
	}
}
