package sk.seges.acris.mvp.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.seges.acris.mvp.server.dao.IUserDao;
import sk.seges.acris.mvp.server.service.core.AbstractCRUDService;
import sk.seges.acris.mvp.server.service.core.ICRUDService;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.sesam.dao.ICrudDAO;

@Service
public class UserMaintenanceService extends AbstractCRUDService<GenericUser> implements IUserMaintenanceService {

	@Autowired
	private IUserDao userDao;
	
	@Override
	protected ICrudDAO<GenericUser> getDao() {
		return userDao;
	}
}