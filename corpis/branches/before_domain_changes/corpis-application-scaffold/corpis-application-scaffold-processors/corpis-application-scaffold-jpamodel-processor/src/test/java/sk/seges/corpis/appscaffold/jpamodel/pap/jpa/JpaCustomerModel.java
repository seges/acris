package sk.seges.corpis.appscaffold.jpamodel.pap.jpa;

import sk.seges.corpis.appscaffold.jpamodel.pap.api.CustomerModel;
import sk.seges.corpis.appscaffold.shared.annotation.domain.JpaModel;

/**
 * @author ladislav.gazo
 */
@JpaModel
public interface JpaCustomerModel extends CustomerModel<Long> {
}
