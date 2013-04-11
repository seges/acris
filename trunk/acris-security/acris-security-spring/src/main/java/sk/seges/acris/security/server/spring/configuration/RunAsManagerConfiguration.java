package sk.seges.acris.security.server.spring.configuration;

import org.springframework.context.annotation.Bean;

import sk.seges.acris.security.server.spring.runas.RoleRunAsManagerImpl;


public class RunAsManagerConfiguration {

	@Bean
	public RoleRunAsManagerImpl runAsManager() {
		RoleRunAsManagerImpl runAsManager = new RoleRunAsManagerImpl();
		runAsManager.setKey("secretRunAsKey");
		return runAsManager;
	}
}
