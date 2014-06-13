package app.domain;

import java.math.BigDecimal;

public class Price {

	private PriceType type;
	private BigDecimal value;

	public enum PriceType {
		Normal, Discount
	}
}
