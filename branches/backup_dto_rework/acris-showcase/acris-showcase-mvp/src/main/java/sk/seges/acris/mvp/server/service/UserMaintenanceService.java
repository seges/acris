package sk.seges.acris.mvp.server.service;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import sk.seges.acris.mvp.server.service.core.AbstractCRUDService;
import sk.seges.acris.mvp.shared.security.Grants;
import sk.seges.acris.security.server.core.annotation.PostExecutionAclEvaluation;
import sk.seges.acris.security.server.core.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.vercer.engine.persist.ObjectDatastore;

public class UserMaintenanceService extends AbstractCRUDService<UserData<?>> implements IUserMaintenanceService {

	private IGenericUserDao<UserData<?>> userDao;
	
	@Autowired
	public UserMaintenanceService(ObjectDatastore datastore, IGenericUserDao<UserData<?>> userDao) {
		super();
		this.userDao = userDao;
	}
	
	@Override
	protected ICrudDAO<UserData<?>> getDao() {
		return userDao;
	}
	
	@Override
	@RolesAllowed(Grants.USER_MAINTENANCE)
	@PostExecutionAclEvaluation
	public UserData<?> persist(UserData<?> entity) {
		return super.persist(entity);
	}

	@Override
	@RolesAllowed(Grants.USER_MAINTENANCE)
	public UserData<?> merge(UserData<?> entity) {
		return super.merge(entity);
	}

	@Override
	@RolesAllowed(Grants.USER_MAINTENANCE)
	public void remove(UserData<?> entity) {
		super.remove(entity);
	}

	@Override
	@RolesAllowed(Grants.USER_MAINTENANCE)
	public PagedResult<List<UserData<?>>> findAll(Page requestedPage) {
		return super.findAll(requestedPage);
	}

	@Override
	@RolesAllowed(Grants.USER_MAINTENANCE)
	public UserData<?> findEntity(UserData<?> entity) {
		return super.findEntity(entity);
	}
}