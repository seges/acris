package sk.seges.acris.security.rpc.user_management.service;

import java.util.List;

import sk.seges.acris.security.rpc.user_management.domain.SecurityRole;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ISecurityRoleService extends RemoteService {
    public SecurityRole findRole(String roleName);
    public List<String> findSelectedPermissions(Integer roleId);
    public PagedResult<List<SecurityRole>> findAll(Page page);
    public SecurityRole persist(SecurityRole role);
    public SecurityRole merge(SecurityRole role);
    public void remove(SecurityRole role);
}
