package sk.seges.acris.security.server.spring.acl.sid;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;

import sk.seges.acris.security.user_management.server.model.data.RoleData;
import sk.seges.acris.security.user_management.server.model.data.UserData;


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
	public List<Sid> getSids(Authentication authentication) {
		List<Sid> sids = super.getSids(authentication);
		List<Sid> result = new ArrayList<Sid>();;
		if (authentication.getPrincipal() instanceof UserData && ((UserData)authentication.getPrincipal()).getRoles() != null) {
			List<RoleData> roles = ((UserData)authentication.getPrincipal()).getRoles();
			int i = 0;
			while (i < roles.size()) {
				result.add(new PrincipalSid(roles.get(i).getName()));
				i++;
			}
			copySids(sids, result, i);
			result.add(new PrincipalSid(RoleData.ALL_USERS)); 
		} else {
			copySids(sids, result, 0);
			result.add(new PrincipalSid(RoleData.ALL_USERS));
		}
		
		return result;
	}
	
	private void copySids(List<Sid> from, List<Sid> to, int startIndex) {
		for (Sid sid : from) {
			to.add(sid);
		}
	}
}