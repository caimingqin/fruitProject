package app.domain;

import java.math.BigDecimal;
import java.util.List;

import com.mce.domain.model.AbstractModel;

public class Product extends AbstractModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3217829387496649011L;
	private String code;
	private String title;
	private String description;
	private List<Price> prices;
	private BigDecimal weight;//净含量 规格
//	private String htmlContent;
	
	
}
