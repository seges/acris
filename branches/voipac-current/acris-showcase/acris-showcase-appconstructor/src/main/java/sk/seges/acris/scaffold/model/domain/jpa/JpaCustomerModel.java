package sk.seges.acris.scaffold.model.domain.jpa;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

import sk.seges.acris.scaffold.model.domain.CustomerModel;
import sk.seges.acris.scaffold.model.domain.RoleModel;
import sk.seges.corpis.appscaffold.shared.annotation.domain.JpaModel;

/**
 * @author ladislav.gazo
 */
@JpaModel
public interface JpaCustomerModel extends CustomerModel<Long> {
	@Column(nullable = false)
	String firstName();
	
	@ManyToOne
	RoleModel role();
}
