package sk.seges.corpis.shared.domain.price.api;

public interface IProductPriceCondition {

    boolean applies(PriceConditionContext context);

}