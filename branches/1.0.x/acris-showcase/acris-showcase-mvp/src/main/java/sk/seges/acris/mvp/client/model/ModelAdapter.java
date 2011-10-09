package sk.seges.acris.mvp.client.model;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.UserData;


public interface ModelAdapter {

	<T> T[] convertUsersForGrid(List<UserData> users);

}