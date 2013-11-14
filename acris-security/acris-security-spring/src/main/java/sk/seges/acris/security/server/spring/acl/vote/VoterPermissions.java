package sk.seges.acris.security.server.spring.acl.vote;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Component;

@Component
public class VoterPermissions {
	public List<Permission> READ;
	public List<Permission> WRITE;
	public List<Permission> CREATE;
	public List<Permission> DELETE;

    @Autowired
    private DefaultPermissionFactory permissionFactory;
    
	public void setPermissionFactory(DefaultPermissionFactory permissionFactory) {
		this.permissionFactory = permissionFactory;
	}
	
    @PostConstruct
    public void init() {
        READ = buildPermission(new int[] { 0x1, 0x3, 0x5, 0x7, 0x9, 0xb, 0xd, 0xf });
        WRITE = buildPermission(new int[] { 0x2, 0x3, 0x6, 0x7, 0xa, 0xb, 0xe, 0xf });
        CREATE = buildPermission(new int[] { 0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf });
        DELETE = buildPermission(new int[] { 0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf });
    }
    
    private List<Permission> buildPermission(int[] masks) {
    	List<Permission> permissions = new ArrayList<Permission>();
    	for (int mask : masks) {
    		permissions.add(permissionFactory.buildFromMask(mask));
    	}
    	return permissions;
    }
}