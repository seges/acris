package sk.seges.acris.mvp.server.initialization;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.mvp.server.service.IUserMaintenanceService;
import sk.seges.acris.mvp.shared.security.Grants;
import sk.seges.acris.security.server.core.annotation.RunAs;
import sk.seges.acris.security.shared.user_management.domain.SecurityConstants;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;
import sk.seges.acris.security.shared.user_management.domain.dto.UserPreferencesDTO;
import sk.seges.sesam.dao.Page;

@Component
public class UserInitializer implements IUserInitializer {

	@Autowired
	private IUserMaintenanceService userMaintenanceService;

	@RunAs(Grants.USER_MAINTENANCE)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void initialize() {
		if (userMaintenanceService.findAll(Page.ALL_RESULTS_PAGE).getTotalResultCount() == 0) {
			GenericUserDTO user = new GenericUserDTO();
			user.setId(1L);
			user.setUsername("admin");
			user.setPassword("admin");
			user.setEnabled(true);
			user.setDescription("administrator");

			List<String> authorities = new ArrayList<String>();
			authorities.add(SecurityConstants.AUTH_PREFIX + Grants.USER_MAINTENANCE);
			user.setUserAuthorities(authorities);
			
			UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
			userPreferencesDTO.setId(1L);
			user.setUserPreferences(userPreferencesDTO);

			userMaintenanceService.persist(user);

			user = new GenericUserDTO();
			user.setId(2L);
			user.setUsername("test");
			user.setPassword("Test");
			user.setEnabled(true);
			user.setDescription("test");

			authorities = new ArrayList<String>();
			authorities.add("test");
			user.setUserAuthorities(authorities);

			userPreferencesDTO = new UserPreferencesDTO();
			userPreferencesDTO.setId(2L);
			user.setUserPreferences(userPreferencesDTO);

			userMaintenanceService.persist(user);
		}
	}
}