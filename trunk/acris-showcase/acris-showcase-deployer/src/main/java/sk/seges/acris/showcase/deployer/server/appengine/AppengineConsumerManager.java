package sk.seges.acris.showcase.deployer.server.appengine;

import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.Discovery;
import org.openid4java.server.RealmVerifierFactory;
import org.openid4java.util.HttpFetcherFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AppengineConsumerManager extends ConsumerManager {

	@Inject
	public AppengineConsumerManager(RealmVerifierFactory realmFactory, Discovery discovery,
			HttpFetcherFactory httpFetcherFactory) {
		super(realmFactory, discovery, httpFetcherFactory);
	}
}