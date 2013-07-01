package sk.seges.acris.security.server.core.user_management.domain.jpa;

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

import sk.seges.acris.security.user_management.server.model.base.HierarchyPermissionBase;
import sk.seges.acris.security.user_management.server.model.data.HierarchyPermissionData;

@Entity
@Table(name = "permission")
@SequenceGenerator(name = "permission_id_seq", sequenceName = "permission_id_seq", initialValue = 1)
public class JpaSecurityPermission extends HierarchyPermissionBase {

	private static final long serialVersionUID = -6620157088484049838L;

	private Integer id;

	public JpaSecurityPermission() {
	}
	
	@Id
    @GeneratedValue(generator="permission_id_seq")
    @Override
    public Integer getId() {
    	return id;
    }
    
    @Column
    @Override
    public String getDiscriminator() {
    	return super.getDiscriminator();
    }
    
    @Column(nullable=false)
    @Override
    public String getWebId() {
    	return super.getWebId();
    }
    
    @ManyToOne(targetEntity=JpaSecurityPermission.class)
    @Override
    public HierarchyPermissionData getParent() {
    	return super.getParent();
    }
    
    @OneToMany(mappedBy="parent", targetEntity=JpaSecurityPermission.class, cascade=CascadeType.ALL)
    @Override
    public List<HierarchyPermissionData> getChildPermissions() {
    	return super.getChildPermissions();
    }
    
    @Column
    @Override
    public Boolean getHasChildren() {
    	return super.getHasChildren();
    }

    @Column
    @Override
    public Integer getLevel() {
    	return super.getLevel();
    }   
}