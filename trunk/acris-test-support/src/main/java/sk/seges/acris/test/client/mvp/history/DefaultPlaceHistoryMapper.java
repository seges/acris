package sk.seges.acris.test.client.mvp.history;

import sk.seges.acris.test.client.cardpay.place.CardPayPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({CardPayPlace.Tokenizer.class})
public interface DefaultPlaceHistoryMapper extends PlaceHistoryMapper {

}
