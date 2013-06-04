package sk.seges.acris.security.rpc.user_management.service;

import java.util.List;

import sk.seges.acris.security.rpc.user_management.domain.SecurityPermission;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ISecurityPermissionService extends RemoteService {
    public List<SecurityPermission> findSecurityPermissions(Integer parentId, String webId);
    public PagedResult<List<SecurityPermission>> findAll(String webId, Page page);
}
