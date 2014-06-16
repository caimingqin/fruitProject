package app.domain;

import java.math.BigDecimal;

import com.mce.domain.model.AbstractModel;

public class Product extends AbstractModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3217829387496649011L;
	private String code;
	private String title;
	private String description;
	private BigDecimal price;
	private BigDecimal weight;//净含量 规格
	private String imageUrl;
	public String getCode() {
		return code;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	
	
}
