package sk.seges.acris.security.server.user_management.dao;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public interface IGrantsDao {

	List<String> findGrantsForToken(LoginToken token);
}
