package sk.seges.acris.security.server.spring.runas;

import org.springframework.security.core.userdetails.UserDetails;

public interface IMuttableRunAsManager {
    public void setRunAsUser(UserDetails user);
}
