package sk.seges.acris.mvp.server.service.dozer;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import sk.seges.acris.mvp.server.service.IUserMaintenanceService;
import sk.seges.acris.mvp.shared.security.Grants;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

public class DozerUserMaintenanceService implements IUserMaintenanceService {

	private DozerSupport dozer;
	private IUserMaintenanceService service;
	
	public DozerUserMaintenanceService(DozerSupport dozer, IUserMaintenanceService service) {
		this.dozer = dozer;
		this.service = service;
	}

	@Override
	public UserData<?> persist(UserData<?> entity) {
		entity = dozer.convert(entity);
		return dozer.convert(service.persist(entity));
	}

	@Override
	public UserData<?> merge(UserData<?> entity) {
		entity = dozer.convert(entity);
		return dozer.convert(service.merge(entity));
	}

	@Override
	public void remove(UserData<?> entity) {
		entity = dozer.convert(entity);
		service.remove(entity);
	}

	@Override
	@RolesAllowed(Grants.USER_MAINTENANCE)
	public PagedResult<List<UserData<?>>> findAll(Page requestedPage) {
		requestedPage = dozer.convert(requestedPage);
		return dozer.convert(service.findAll(requestedPage));
	}

	@Override
	public UserData<?> findEntity(UserData<?> entity) {
		entity = dozer.convert(entity);
		return dozer.convert(service.findEntity(entity));
	}
	
	
}