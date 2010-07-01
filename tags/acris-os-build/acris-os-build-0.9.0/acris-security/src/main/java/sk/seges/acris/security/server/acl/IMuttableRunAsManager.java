package sk.seges.acris.security.server.acl;

import org.springframework.security.userdetails.UserDetails;

public interface IMuttableRunAsManager {
    public void setRunAsUser(UserDetails user);
}
