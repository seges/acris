package sk.seges.acris.security.server.spring.acl.sid;

import org.springframework.security.acls.sid.GrantedAuthoritySid;
import org.springframework.security.acls.sid.PrincipalSid;
import org.springframework.security.acls.sid.Sid;

public class SidNameResolver {

	public static String getSidName(Sid sid) {
		if (sid instanceof PrincipalSid) {
			return ((PrincipalSid) sid).getPrincipal();
		} else if (sid instanceof GrantedAuthoritySid) {
			return ((GrantedAuthoritySid) sid).getGrantedAuthority();
		} else {
			throw new IllegalArgumentException("Unsupported implementation of Sid");
		}
	}

}
