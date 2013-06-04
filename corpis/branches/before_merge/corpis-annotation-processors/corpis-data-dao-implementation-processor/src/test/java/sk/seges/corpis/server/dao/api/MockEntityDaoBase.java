package sk.seges.corpis.server.dao.api;

import javax.annotation.Generated;

import sk.seges.corpis.shared.model.MockEntityData;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.IEntityInstancer;

@Generated(value = "sk.seges.corpis.core.pap.dao.DataDaoApiProcessor")
public interface MockEntityDaoBase<T extends MockEntityData> extends ICrudDAO<T>, IEntityInstancer<T> {

}
