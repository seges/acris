package sk.seges.acris.security.rpc.user_management.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.sf.gilead.pojo.java5.LightEntity;
import sk.seges.sesam.domain.IDomainObject;

@Entity
@Table(name = "permission")
@SequenceGenerator(name = "permission_id_seq", sequenceName = "permission_id_seq", initialValue = 1)
public class SecurityPermission extends LightEntity implements IDomainObject<Integer> {
    private static final long serialVersionUID = -8053014688760726531L;
    
    public static final String A_PARENT = "parent";
    public static final String A_ID = "id";
    public static final String A_WEBID = "webId";
    public static final String A_PERMISSION = "permission";
    
    @Id
    @GeneratedValue(generator="permission_id_seq")
    private Integer id;
    private String permission;
    @Column
    private String discriminator;
    @Column(nullable=false)
    private String webId;
    @ManyToOne
    private SecurityPermission parent;
    @OneToMany(mappedBy="parent", cascade=CascadeType.ALL)
    private List<SecurityPermission> childPermissions;
    @Column
    private Boolean hasChildren = false;
    @Column
    private Integer level = 1;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public SecurityPermission getParent() {
        return parent;
    }

    public void setParent(SecurityPermission parent) {
        this.parent = parent;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getWebId() {
        return webId;
    }

    public void setWebId(String webId) {
        this.webId = webId;
    }

    public List<SecurityPermission> getChildPermissions() {
        return childPermissions;
    }

    public void setChildPermissions(List<SecurityPermission> childPermissions) {
        if(null != childPermissions && childPermissions.size() > 0) {
            hasChildren = true;
            for(SecurityPermission childPermission : childPermissions) {
                childPermission.setLevel(getLevel() + 1);
                childPermission.setParent(this);
            }
        }
        this.childPermissions = childPermissions;
    }
    
    public void addChildPermissions(SecurityPermission ... permissions) {
        if(null == permissions || permissions.length == 0) {
            throw new IllegalArgumentException();
        }
        if(null == childPermissions) {
            childPermissions = new ArrayList<SecurityPermission>();
            hasChildren = true;
        }
        for(SecurityPermission permission : permissions) {
            permission.setLevel(getLevel() + 1);
            permission.setParent(this);
            childPermissions.add(permission);
        }
    }
    
    public void addChildPermissions(SecurityPermission[] ... permissionArrays) {
        if(null == permissionArrays || permissionArrays.length == 0) {
            throw new IllegalArgumentException();
        }
        if(null == childPermissions) {
            childPermissions = new ArrayList<SecurityPermission>();
            hasChildren = true;
        }
        for(SecurityPermission[] permissionArray : permissionArrays) {
            for(SecurityPermission permission : permissionArray) {
                permission.setLevel(getLevel() + 1);
                permission.setParent(this);
                childPermissions.add(permission);
            }
        }
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SecurityPermission other = (SecurityPermission) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
