package sk.seges.corpis.shared.domain.invoice;

public interface IProductPriceCondition {
	public boolean applies(PriceConditionContext context);
}
