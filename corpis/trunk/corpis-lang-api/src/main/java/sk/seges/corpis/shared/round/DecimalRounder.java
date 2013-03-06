/**
 * 
 */
package sk.seges.corpis.shared.round;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Decimal rounder which purpose is to round to required number of fraction
 * digits. There are several prepared rounders commonly used in i.e. financial
 * world.
 * 
 * @author eldzi
 */
public class DecimalRounder implements Rounder {
	private static final int DEFAULT_NUMBER_OF_FRACTION_DIGITS = 2;
	private BigDecimal fractionDigitsMultiplier;
	private final MathContext mc;

	private DecimalRounder(int numberOfFractionDigits, MathContext mc) {
		if (mc == null) {
			this.mc = new MathContext(0, RoundingMode.HALF_EVEN);
		} else {
			this.mc = mc;
		}
		fractionDigitsMultiplier = new BigDecimal(10).pow(numberOfFractionDigits, this.mc);
	}

	/**
	 * @param numberOfFractionDigits
	 * @return Rounder instance returning rounded number to defined fraction
	 *         digits.
	 */
	public static DecimalRounder getRounderInstance(int numberOfFractionDigits) {
		return new DecimalRounder(numberOfFractionDigits, null);
	}

	/**
	 * @param numberOfFractionDigits
	 * @return Rounder instance returning rounded number to defined fraction
	 *         digits.
	 */
	public static DecimalRounder getRounderInstance(int numberOfFractionDigits, MathContext mc) {
		return new DecimalRounder(numberOfFractionDigits, mc);
	}

	/**
	 * @return Rounder instance returning rounded number to default number
	 *         (DEFAULT_NUMBER_OF_FRACTION_DIGITS) of fraction digits.
	 */
	public static DecimalRounder getDefaultInstance() {
		return getRounderInstance(DEFAULT_NUMBER_OF_FRACTION_DIGITS);
	}

	/**
	 * @return Rounder instance returning rounded number to default number
	 *         (DEFAULT_NUMBER_OF_FRACTION_DIGITS) of fraction digits.
	 */
	public static DecimalRounder getDefaultInstance(MathContext mc) {
		return getRounderInstance(DEFAULT_NUMBER_OF_FRACTION_DIGITS, mc);
	}

	/**
	 * @return Rounder instance returning rounded number to 1 fraction digit.
	 */
	public static DecimalRounder getTenthRounderInstance() {
		return getRounderInstance(1);
	}

	/**
	 * @return Rounder instance returning rounded number to 1 fraction digit.
	 */
	public static DecimalRounder getTenthRounderInstance(MathContext mc) {
		return getRounderInstance(1, mc);
	}

	/**
	 * @return Rounder instance returning rounded number to 2 fraction digits.
	 */
	public static DecimalRounder getFinancialRounderInstance() {
		return getRounderInstance(2);
	}

	/**
	 * @return Rounder instance returning rounded number to 2 fraction digits.
	 */
	public static DecimalRounder getFinancialRounderInstance(MathContext mc) {
		return getRounderInstance(2, mc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sk.seges.facilitis.client2.component.round.Rounder#round(java.lang.Float)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Number> T round(T value) {
		if (!(value instanceof BigDecimal)) {
			return (T) Double.valueOf(Math
					.round(value.doubleValue() * fractionDigitsMultiplier.doubleValue())
					/ fractionDigitsMultiplier.doubleValue());
		} else {
			BigDecimal bd = (BigDecimal) value;
			BigDecimal multiply = bd.multiply(fractionDigitsMultiplier, mc);
			BigDecimal left = new BigDecimal(multiply.add(new BigDecimal("0.5"),
					new MathContext(0, RoundingMode.FLOOR)).longValue(), mc);
			return (T) left.divide(fractionDigitsMultiplier, mc);
		}
	}
}
