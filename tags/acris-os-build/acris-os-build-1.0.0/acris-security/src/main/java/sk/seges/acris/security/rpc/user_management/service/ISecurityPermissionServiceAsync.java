package sk.seges.acris.security.rpc.user_management.service;

import java.util.List;

import sk.seges.acris.security.rpc.user_management.domain.SecurityPermission;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISecurityPermissionServiceAsync {
    public void findAll(String webId, Page page, AsyncCallback<PagedResult<List<SecurityPermission>>> callback);    
    public void findSecurityPermissions(Integer parentId, String webId, AsyncCallback<List<SecurityPermission>> callback);
}
