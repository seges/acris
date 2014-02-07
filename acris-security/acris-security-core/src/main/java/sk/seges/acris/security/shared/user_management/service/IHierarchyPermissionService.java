package sk.seges.acris.security.shared.user_management.service;

import java.util.List;

import sk.seges.acris.security.shared.user_management.model.dto.HierarchyPermissionDTO;
import sk.seges.sesam.shared.model.dto.PageDTO;
import sk.seges.sesam.shared.model.dto.PagedResultDTO;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServiceDefinition
public interface IHierarchyPermissionService extends RemoteService {

	List<HierarchyPermissionDTO> findSecurityPermissions(Integer parentId, String webId);

	PagedResultDTO<List<HierarchyPermissionDTO>> findAll(String webId, PageDTO page);
}