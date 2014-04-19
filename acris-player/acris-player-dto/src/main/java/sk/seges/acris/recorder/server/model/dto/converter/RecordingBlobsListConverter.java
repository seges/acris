package sk.seges.acris.recorder.server.model.dto.converter;

import sk.seges.acris.recorder.server.model.data.RecordingBlobData;
import sk.seges.sesam.server.domain.converter.PersistentCollectionConverter;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

/**
 * Created by PeterSimun on 14.4.2014.
 */
public class RecordingBlobsListConverter extends PersistentCollectionConverter<String, RecordingBlobData> {

    public RecordingBlobsListConverter(ConverterProviderContext converterProviderContext) {
        super(converterProviderContext);
    }

    @Override
    protected DtoConverter<String, RecordingBlobData> getConverterForDomain(RecordingBlobData recordingBlobData) {
        return converterProviderContext.getConverterForDomain(RecordingBlobData.class);
    }

    @Override
    protected DtoConverter<String, RecordingBlobData> getConverterForDto(String s) {
        return converterProviderContext.getConverterForDomain(RecordingBlobData.class);
    }
}