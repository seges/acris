/**
 * 
 */
package sk.seges.acris.site.shared.domain.api;

/**
 * @author eldzi
 */
public enum SubscriptionState {
	SUBSCRIPTION_VALIDATION, UNSUBSCRIPTION_VALIDATION, SUBSCRIBED, UNSUBSCRIBED, NOT_ASSOCIATED_CODE, OUTDATED, NOT_EXISTING_EMAIL, ALREADY_SUBSCRIBED;
}
