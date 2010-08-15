package sk.seges.acris.mvp.client.model;

import java.util.List;

import sk.seges.acris.security.rpc.user_management.domain.GenericUser;


public interface ModelAdapter {

	<T> T[] convertUsersForGrid(List<GenericUser> users);

}