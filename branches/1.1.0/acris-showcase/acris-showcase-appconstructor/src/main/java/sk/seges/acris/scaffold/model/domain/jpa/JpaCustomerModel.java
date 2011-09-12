package sk.seges.acris.scaffold.model.domain.jpa;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

import sk.seges.acris.scaffold.annotation.domain.JpaModel;
import sk.seges.acris.scaffold.model.domain.CustomerModel;
import sk.seges.acris.scaffold.model.domain.RoleModel;

/**
 * @author ladislav.gazo
 */
@JpaModel
public interface JpaCustomerModel extends CustomerModel {
	@Column(nullable = false)
	String firstName();
	
	@ManyToOne
	RoleModel role();
}
