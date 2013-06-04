package sk.seges.acris.security.server.acl;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.RunAsManager;
import org.springframework.security.runas.RunAsUserToken;
import org.springframework.security.userdetails.UserDetails;
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
    public Authentication buildRunAs(Authentication authentication, Object object, ConfigAttributeDefinition config) {
        RunAsUserToken result = null;
        if (runAsUser == null) {
            List<GrantedAuthority> newAuthorities = new Vector<GrantedAuthority>();
            Iterator<ConfigAttribute> iter = config.getConfigAttributes().iterator();

            while (iter.hasNext()) {
                ConfigAttribute attribute = (ConfigAttribute) iter.next();

                if (this.supports(attribute)) {
                    String role = attribute.getAttribute().substring(attribute.getAttribute().lastIndexOf("RUN_AS_") + "RUN_AS_".length());
                    GrantedAuthorityImpl extraAuthority = new GrantedAuthorityImpl(getRolePrefix() + role);
                    newAuthorities.add(extraAuthority);
                }
            }

            if (newAuthorities.size() == 0) {
                return null;
            }

            for (int i = 0; i < authentication.getAuthorities().length; i++) {
                newAuthorities.add(authentication.getAuthorities()[i]);
            }

            GrantedAuthority[] resultType = { new GrantedAuthorityImpl("holder") };
            GrantedAuthority[] newAuthoritiesAsArray = (GrantedAuthority[]) newAuthorities.toArray(resultType);

            result = new RunAsUserToken(this.key, authentication.getPrincipal(), authentication.getCredentials(), newAuthoritiesAsArray, authentication
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
    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return true;
    }

}