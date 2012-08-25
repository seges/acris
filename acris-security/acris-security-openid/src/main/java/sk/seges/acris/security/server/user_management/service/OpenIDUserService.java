package sk.seges.acris.security.server.user_management.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sk.seges.acris.security.server.core.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.acris.security.server.user_management.domain.api.HasOpenIDIdentifierMetaModel;
import sk.seges.acris.security.server.user_management.domain.jpa.JPAOpenIDUser;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;
import sk.seges.acris.security.shared.user_management.service.IOpenIDUserService;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;

public class OpenIDUserService implements IOpenIDUserService {

	private static final long serialVersionUID = 6393202762149693176L;

	private IGenericUserDao<UserData> genericUserDao;

	private IOpenIDUserDao<? extends HasOpenIDIdentifier> openIDUserDao;

	public OpenIDUserService(IGenericUserDao<UserData> genericUserDao,
			IOpenIDUserDao<? extends HasOpenIDIdentifier> openIDUserDao) {
		this.genericUserDao = genericUserDao;
		this.openIDUserDao = openIDUserDao;
	}

	@Override
	public UserData getUserByOpenIDIdentifier(String identifier) {
		Page page = new Page(0, Page.ALL_RESULTS);
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
	private UserData convert(UserData source) {
		UserData target = new GenericUserDTO();
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
				HasOpenIDIdentifierMetaModel.USER.USERNAME, userName, Filter.EQ));

		List<OpenIDProvider> result = new ArrayList<OpenIDProvider>();
		List<HasOpenIDIdentifier> found = openIDUserDao.findAll(page).getResult();
		for (HasOpenIDIdentifier hasOpenIDIdentifier : found) {
			result.add(hasOpenIDIdentifier.getProvider());
		}

		return result;
	}

	@Override
	public void saveUserByIdentifiers(String userName, Map<String, Object> identifiers) {
		JPAOpenIDUser entity = new JPAOpenIDUser();
		entity.setUser(genericUserDao.findByUsername(userName));

		String identifier = (String) identifiers.get("identifier");
		if (identifier != null) {
			entity.setId(identifier);
		}

		String email = (String) identifiers.get("email");
		if (email != null) {
			entity.setEmail(email);
		}

		String providerName = (String) identifiers.get("provider");
		if (providerName != null) {
			if (providerName.equals(OpenIDProvider.GOOGLE.name())) {
				entity.setProvider(OpenIDProvider.GOOGLE);
			} else if (providerName.equals(OpenIDProvider.YAHOO.name())) {
				entity.setProvider(OpenIDProvider.YAHOO);
			} else if (providerName.equals(OpenIDProvider.AOL.name())) {
				entity.setProvider(OpenIDProvider.AOL);
			} else if (providerName.equals(OpenIDProvider.SEZNAM.name())) {
				entity.setProvider(OpenIDProvider.SEZNAM);
			} else if (providerName.equals(OpenIDProvider.MYOPENID.name())) {
				entity.setProvider(OpenIDProvider.MYOPENID);
			}
		}

		openIDUserDao.persist(entity);
	}
}
