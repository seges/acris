package sk.seges.acris.generator.client.json.params;

import sk.seges.acris.domain.params.ContentParameters;

public interface OfflineClientWebParams extends ContentParameters {

    public enum OfflineMode {
        OFFLINE, COMBINED, BOTH {
            @Override
            public boolean contains(OfflineMode mode) {
                return true;
            }
        };

        public boolean contains(OfflineMode mode) {
            return this.equals(mode);
        }
    }

	Boolean isPublishOnSaveEnabled();

	void setPublishOnSaveEnabled(boolean publishOnSaveEnabled);

	Boolean supportsAutodetectMode();

    OfflineMode getOfflineMode();

    void setOfflineMode(OfflineMode offlineMode);
}