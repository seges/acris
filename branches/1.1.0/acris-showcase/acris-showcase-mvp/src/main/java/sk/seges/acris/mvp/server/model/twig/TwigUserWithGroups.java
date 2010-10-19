package sk.seges.acris.mvp.server.model.twig;

import java.util.List;
import java.util.Set;

import sk.seges.acris.mvp.shared.model.api.GroupData;
import sk.seges.acris.mvp.shared.model.api.UserGroupData;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;


public class TwigUserWithGroups implements UserGroupData, UserData {

	@Override
	public void setId(Long t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAuthority(String authority) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getUserAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUserAuthorities(List<String> authorities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUsername(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<GroupData> getGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGroups(Set<GroupData> groups) {
		// TODO Auto-generated method stub
		
	}

}
