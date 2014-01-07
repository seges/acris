package sk.seges.acris.security.server.user_management.service;

import sk.seges.acris.security.server.spring.user_management.dao.user.api.IGenericUserDao;
import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.domain.jpa.JPAOpenIDUser;
import sk.seges.acris.security.server.user_management.server.model.data.OpenIDUserData;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;
import sk.seges.acris.security.shared.user_management.service.IOpenIDUserRemoteService;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;
import sk.seges.sesam.pap.service.annotation.LocalService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenIDUserService implements IOpenIDUserRemoteServiceLocal {

	private static final long serialVersionUID = 6393202762149693176L;

	private IGenericUserDao<UserData> genericUserDao;

	private IOpenIDUserDao<OpenIDUserData> openIDUserDao;

	public OpenIDUserService(IGenericUserDao<UserData> genericUserDao,
			IOpenIDUserDao<OpenIDUserData> openIDUserDao) {
		this.genericUserDao = genericUserDao;
		this.openIDUserDao = openIDUserDao;
	}

	@Override
	public UserData getUserByOpenIDIdentifier(String identifier) {
		Page page = new Page(0, Page.ALL_RESULTS);
		page.setFilterable(new SimpleExpression<Comparable<? extends Serializable>>("id", identifier, Filter.EQ));
		List<OpenIDUserData> result = openIDUserDao.findAll(page).getResult();

		if (result.size() > 0) {
			return result.get(0).getUser();
		}

		return null;
	}

	@Override
	public List<OpenIDProvider> findProvidersByUserName(String userName) {
		Page page = new Page(0, Page.ALL_RESULTS);

		page.setFilterable(new SimpleExpression<Comparable<? extends Serializable>>(
				OpenIDUserData.USER + "." + UserData.USERNAME, userName, Filter.EQ));

		List<OpenIDProvider> result = new ArrayList<OpenIDProvider>();
		List<OpenIDUserData> found = openIDUserDao.findAll(page).getResult();
		for (OpenIDUserData hasOpenIDIdentifier : found) {
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