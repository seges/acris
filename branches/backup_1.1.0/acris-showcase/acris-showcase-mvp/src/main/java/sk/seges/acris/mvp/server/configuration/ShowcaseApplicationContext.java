package sk.seges.acris.mvp.server.configuration;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.userdetails.UserDetailsService;

import sk.seges.acris.mvp.server.dispatch.GileadHttpSessionSecurityCookieFilter;
import sk.seges.acris.mvp.server.dispatch.GileadRequestProvider;
import sk.seges.acris.mvp.server.provider.TwigUserDao;
import sk.seges.acris.mvp.server.service.IUserMaintenanceService;
import sk.seges.acris.mvp.server.service.UserMaintenanceService;
import sk.seges.acris.mvp.server.service.dozer.DozerSupport;
import sk.seges.acris.mvp.server.service.dozer.DozerUserMaintenanceService;
import sk.seges.acris.security.server.core.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.server.spring.user_management.service.SpringUserService;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

import com.gwtplatform.dispatch.server.AbstractHttpSessionSecurityCookieFilter;
import com.gwtplatform.dispatch.server.RequestProvider;
import com.gwtplatform.dispatch.server.spring.DispatchModule;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

@Import({ DispatchModule.class })
public class ShowcaseApplicationContext {

	private/* @Value("cookie") */String securityCookieName;

	@Bean
	public String getSecurityCookieName() {
		return securityCookieName;
	}

	@Bean
	public AbstractHttpSessionSecurityCookieFilter getCookieFilter() {
		return new GileadHttpSessionSecurityCookieFilter(getSecurityCookieName());
	}

	@Bean
	public RequestProvider getRequestProvider() {
		return new GileadRequestProvider();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new SpringUserService(genericUserDao());
	}

	@Bean
	public ObjectDatastore objectDatastore() {
		return new AnnotationObjectDatastore();
	}

	@Bean
	@SuppressWarnings("unchecked")
	public IGenericUserDao<UserData<?>> genericUserDao() {
		return (IGenericUserDao<UserData<?>>) ((IGenericUserDao<?>) new TwigUserDao(objectDatastore()));
	}

	@Bean
	public IUserMaintenanceService userMaintenanceService() {
		UserMaintenanceService userMaintenanceService = new UserMaintenanceService(objectDatastore(), genericUserDao());
		return new DozerUserMaintenanceService(dozerSupport(), userMaintenanceService);
	}

	@Bean
	public Mapper getMapper() {
		List<String> sources = new ArrayList<String>();
		sources.add("sk/seges/acris/mvp/server/dozer-mapping.xml");
		DozerBeanMapper mapper = new DozerBeanMapper(sources);
		return mapper;
	}

	@Bean
	public DozerSupport dozerSupport() {
		return new DozerSupport(getMapper());
	}
}