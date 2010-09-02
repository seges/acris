package sk.seges.acris.security.server.spring.acl.vote;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.stereotype.Component;

@Component
public class VoterPermissions {
    @Autowired
    private DefaultPermissionFactory permissionFactory;
    
    @PostConstruct
    public void init() {
        permissionFactory.registerPermission(BasePermission.READ, BasePermission.READ.getPattern());
        permissionFactory.registerPermission(BasePermission.WRITE, BasePermission.WRITE.getPattern());
        permissionFactory.registerPermission(BasePermission.CREATE, BasePermission.CREATE.getPattern());
        permissionFactory.registerPermission(BasePermission.DELETE, BasePermission.DELETE.getPattern());
        
        READ = permissionFactory.buildFromMask(new int[] {
                0x1, 0x3, 0x5, 0x7, 0x9, 0xb, 0xd, 0xf
        });
        WRITE = permissionFactory.buildFromMask(new int[] {
                0x2, 0x3, 0x6, 0x7, 0xa, 0xb, 0xe, 0xf
        });
        CREATE = permissionFactory.buildFromMask(new int[] {
                0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf
        });
        DELETE = permissionFactory.buildFromMask(new int[] {
                0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf
        });
    }
    
    public Permission[] READ;
    public Permission[] WRITE;
    public Permission[] CREATE;
    public Permission[] DELETE;
}
