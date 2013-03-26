/**
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package sk.seges.acris.openid.server.service;

import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;

import sk.seges.acris.openid.server.session.servlet.GuiceServerSessionProvider;
import sk.seges.acris.security.server.core.session.ServerSessionProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

public class ShowcaseGuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ServerSessionProvider.class).to(GuiceServerSessionProvider.class).in(Scopes.SINGLETON);
	}

	@Provides
	@Singleton
	public ConsumerManager provideConsumerManager() {
		ConsumerManager manager = new ConsumerManager();
		manager.setAssociations(new InMemoryConsumerAssociationStore());
		manager.setNonceVerifier(new InMemoryNonceVerifier(5000));

		return manager;
	}
}
