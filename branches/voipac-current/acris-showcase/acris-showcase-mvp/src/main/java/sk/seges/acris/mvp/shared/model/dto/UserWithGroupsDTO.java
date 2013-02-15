package sk.seges.acris.mvp.shared.model.dto;

import java.util.Set;

import sk.seges.acris.mvp.shared.model.api.GroupData;
import sk.seges.acris.mvp.shared.model.api.UserGroupData;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;


public class UserWithGroupsDTO extends GenericUserDTO implements UserGroupData {

	private static final long serialVersionUID = 1343440414788366417L;

	private Set<GroupData> groups;
	
	@Override
	public Set<GroupData> getGroups() {
		return groups;
	}

	@Override
	public void setGroups(Set<GroupData> groups) {
		this.groups = groups;
	}
}