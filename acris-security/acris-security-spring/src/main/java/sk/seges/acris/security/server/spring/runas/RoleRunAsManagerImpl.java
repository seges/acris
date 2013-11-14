package sk.seges.acris.security.server.spring.runas;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;


public class RoleRunAsManagerImpl implements RunAsManager, InitializingBean, IMuttableRunAsManager {

    public static final String ROLE_PREFIX = "ROLE_";

    private String key;
    private String rolePrefix = ROLE_PREFIX;
    private UserDetails runAsUser;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(key, "A Key is required and should match that configured for the RunAsImplAuthenticationProvider");
    }

    public void setRunAsUser(UserDetails runAsUser) {
        this.runAsUser = runAsUser;
    }

    @SuppressWarnings("unchecked")
    public Authentication buildRunAs(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        RunAsUserToken result = null;
        if (runAsUser == null) {
            List<GrantedAuthority> newAuthorities = new Vector<GrantedAuthority>();
            for (ConfigAttribute attribute : attributes) {
                if (this.supports(attribute)) {
                    String role = attribute.getAttribute().substring(attribute.getAttribute().lastIndexOf("RUN_AS_") + "RUN_AS_".length());
                    GrantedAuthorityImpl extraAuthority = new GrantedAuthorityImpl(getRolePrefix() + role);
                    newAuthorities.add(extraAuthority);
                }
            }

            if (newAuthorities.size() == 0) {
                return null;
            }

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                newAuthorities.add(authority);
            }

            result = new RunAsUserToken(this.key, authentication.getPrincipal(), authentication.getCredentials(), newAuthorities, authentication
                    .getClass());
        } else {
            result = new RunAsUserToken(key, runAsUser.getUsername(), runAsUser.getPassword(), runAsUser.getAuthorities(), authentication.getClass());
            runAsUser = null;
        }
        return result;
    }

    public String getKey() {
        return key;
    }

    public String getRolePrefix() {
        return rolePrefix;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Allows the default role prefix of <code>ROLE_</code> to be overridden.
     * May be set to an empty value, although this is usually not desirable.
     * 
     * @param rolePrefix
     *            the new prefix
     */
    public void setRolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
    }

    public boolean supports(ConfigAttribute attribute) {
        if ((attribute.getAttribute() != null) && attribute.getAttribute().startsWith("RUN_AS_")) {
            return true;
        }
        return false;
    }

    /**
     * This implementation supports any type of class, because it does not query
     * the presented secure object.
     * 
     * @param clazz
     *            the secure object
     * 
     * @return always <code>true</code>
     */
    @SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
        return true;
    }

}