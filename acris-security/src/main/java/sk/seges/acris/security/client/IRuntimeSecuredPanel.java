package sk.seges.acris.security.client;

public interface IRuntimeSecuredPanel {
    void setPermission(String role);
    void setPermissions(String[] roles);
    String[] getRoles();
}