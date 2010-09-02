package sk.seges.acris.mvp.server.initialization;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer {

	@Autowired
	private IUserInitializer userInitializer;
	
	@PostConstruct
	public void initialize() {
		userInitializer.initialize();
	}
}