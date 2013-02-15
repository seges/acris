package sk.seges.acris.generator.server.manager.api;

import java.util.Set;

public interface OfflineWebSettings {

	Set<String> getInactiveIndexProcessors();

	Set<String> getInactiveProcessors();

	boolean supportsAutodetectMode();

	boolean publishOnSaveEnabled();
}
