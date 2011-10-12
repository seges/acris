package sk.seges.acris.scaffold.model.domain.versioning;

import sk.seges.acris.scaffold.annotation.migrate.Migrate;
import sk.seges.acris.scaffold.annotation.migrate.PreviousModel;
import sk.seges.acris.scaffold.migration.domain.versioning.CustomerModelToCustomerModelVersion1Migration;
import sk.seges.acris.scaffold.model.domain.CustomerModel;

/**
 * @see CustomerModelToCustomerModelVersion1Migration
 * 
 * @author ladislav.gazo
 *
 */
@PreviousModel(version = "1", currentModel = CustomerModel.class)
public interface CustomerModelVersion1 {
	@Migrate(to = "surname") // TODO: think about a way how to reference through constant not a string
	String secondName();
}
