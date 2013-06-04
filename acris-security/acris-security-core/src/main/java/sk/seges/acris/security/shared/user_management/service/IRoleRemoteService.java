package sk.seges.acris.security.shared.user_management.service;

import java.util.List;

import sk.seges.acris.security.shared.user_management.model.dto.SecurityRoleDTO;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServiceDefinition
public interface IRoleRemoteService extends RemoteService {

	SecurityRoleDTO findRole(String roleName);

	List<String> findSelectedPermissions(Integer roleId);

	PagedResult<List<SecurityRoleDTO>> findAll(Page page);

	SecurityRoleDTO persist(SecurityRoleDTO role);

	SecurityRoleDTO merge(SecurityRoleDTO role);

	void remove(SecurityRoleDTO role);
}