package sk.seges.acris.binding.client.init;

/**
 * Generic registrator for all available adapters. Concreate implementation should register all adapter providers in
 * order to avoid manual registration
 * 
 * @author psimun
 */
public interface AdaptersRegistration {

	void registerAllAdapters();
}
