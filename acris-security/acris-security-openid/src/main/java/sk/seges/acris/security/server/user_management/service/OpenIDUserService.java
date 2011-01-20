package sk.seges.acris.security.server.user_management.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.domain.jpa.JPAOpenIDUser;
import sk.seges.acris.security.shared.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.acris.security.shared.user_management.domain.api.HasOpenIDIdentifierBeanWrapper;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;
import sk.seges.acris.security.shared.user_management.service.IOpenIDUserService;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;

public class OpenIDUserService implements IOpenIDUserService {

	private static final long serialVersionUID = 6393202762149693176L;

	private IOpenIDUserDao<? extends HasOpenIDIdentifier> openIDUserDao;

	public OpenIDUserService(IOpenIDUserDao<? extends HasOpenIDIdentifier> openIDUserDao) {
		this.openIDUserDao = openIDUserDao;
	}

	@Override
	public UserData<?> getUserByOpenIDIdentifier(String identifier) {
		Page page = new Page(0, 1);
		page.setFilterable(new SimpleExpression<Comparable<? extends Serializable>>("id", identifier, Filter.EQ));
		List<HasOpenIDIdentifier> result = openIDUserDao.findAll(page).getResult();

		if (result.size() > 0) {
			return convert(result.get(0).getUser());
		}
		return null;
	}

	/**
	 * Converts source domain object to DTO.
	 * 
	 * @param source
	 * @return
	 */
	private UserData<?> convert(UserData<?> source) {
		UserData<Long> target = new GenericUserDTO();
		target.setEnabled(source.isEnabled());
		target.setId((Long) source.getId());
		target.setPassword(source.getPassword());
		target.setUserAuthorities(source.getUserAuthorities());
		target.setUsername(source.getUsername());

		return target;
	}

	@Override
	public List<OpenIDProvider> findProvidersByUserName(String userName) {
		Page page = new Page(0, Page.ALL_RESULTS);

		page.setFilterable(new SimpleExpression<Comparable<? extends Serializable>>(
				HasOpenIDIdentifierBeanWrapper.USER.USERNAME, userName, Filter.EQ));

		List<OpenIDProvider> result = new ArrayList<OpenIDProvider>();
		List<HasOpenIDIdentifier> found = openIDUserDao.findAll(page).getResult();
		for (HasOpenIDIdentifier hasOpenIDIdentifier : found) {
			result.add(hasOpenIDIdentifier.getProvider());
		}

		return result;
	}

	@Override
	public void saveUserByIdentifiers(UserData<?> user, Map<String, Object> identifiers) {
		JPAOpenIDUser entity = new JPAOpenIDUser();
		entity.setUser(user);

		String identifier = (String) identifiers.get("identifier");
		if (identifier != null) {
			entity.setId(identifier);
		}

		String email = (String) identifiers.get("email");
		if (email != null) {
			entity.setEmail(email);
		}

		OpenIDProvider provider = (OpenIDProvider) identifiers.get("provider");
		if (provider != null) {
			entity.setProvider(provider);
		}

		openIDUserDao.persist(entity);
	}
}
