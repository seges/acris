package sk.seges.acris.mvp.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.seges.acris.mvp.server.service.core.AbstractCRUDService;
import sk.seges.acris.security.server.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.sesam.dao.ICrudDAO;

@Service
public class UserMaintenanceService extends AbstractCRUDService<UserData> implements IUserMaintenanceService {

	@Autowired
	private IGenericUserDao<UserData> userDao;
	
	@Override
	protected ICrudDAO<UserData> getDao() {
		return userDao;
	}
}