package sk.seges.acris.security.server.spring.acl.sid;

import java.util.List;

import org.springframework.security.Authentication;
import org.springframework.security.acls.sid.PrincipalSid;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.acls.sid.SidRetrievalStrategyImpl;

import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;


/**
 * Roles Sid Retrieval Strategy
 * Sid retrieval strategy which also add roles
 * from the user to the sids 
 * 
 * @author psloboda
 *
 */
public class RolesPublicSidRetrievalStrategy extends SidRetrievalStrategyImpl {

	@Override
	public Sid[] getSids(Authentication authentication) {
		Sid[] sids = super.getSids(authentication);
		Sid[] result;
		if (authentication.getPrincipal() instanceof UserData && ((UserData)authentication.getPrincipal()).getRoles() != null) {
			List<RoleData> roles = ((UserData)authentication.getPrincipal()).getRoles();
			result = new Sid[(sids.length + roles.size() + 1)];
			int i = 0;
			while (i < roles.size()) {
				result[i] = new PrincipalSid(roles.get(i).getName());
				i++;
			}
			i = copySids(sids, result, i);
			result[i] = new PrincipalSid(RoleData.ALL_USERS); 
		} else {
			result = new Sid[sids.length + 1];
			copySids(sids, result, 0);
			result[sids.length] = new PrincipalSid(RoleData.ALL_USERS);
		}
		
		return result;
	}
	
	private int copySids(Sid[] from, Sid[] to, int startIndex) {
		int i = startIndex;
		for (int j = 0; j < from.length; j++) {
			to[i] = from[j];
			i++;
		}
		return i;
	}
}