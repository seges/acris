package sk.seges.acris.test.client.cardpay.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class CardPayPlace extends Place {

    private String token;

    public CardPayPlace(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public static class Tokenizer implements PlaceTokenizer<CardPayPlace> {
        @Override
        public String getToken(CardPayPlace place) {
            return place.getToken();
        }

        @Override
        public CardPayPlace getPlace(String token) {
            return new CardPayPlace(token);
        }
    }
}