package sk.seges.acris.security.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/auth")
public class RestAuthenticationResource {
	
	@GET
	@Path("/user&{webId}&{apiKey}")
	@Produces({ MediaType.APPLICATION_JSON })
	public JSONObject authenticateUser(@PathParam("webId") String webId, @PathParam("apiKey") String apiKey) throws JSONException {
		JSONObject result;
		if (webId != null && webId.matches("demo*.\\.synapso\\.sk")) {
			result = new JSONObject().put("allowed", true);
		} else {
			result = new JSONObject().put("allowed", false);
		}
		return result;
	}
}
