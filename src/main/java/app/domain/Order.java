package app.domain;

import java.util.List;

import com.mce.domain.model.AbstractModel;

public class Order extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String CANCEL_EVENT = "cancelOrder";
	public static String KEY = "code";
	public static String COL = "order";
	public static String CREATE_EVENT = "createOrder";
	private String code;
	private OrderState orderState = OrderState.NORMAL;
	private String creater;
	private String createrCode;
	private String addr;
	private String phone;
	private String recipient; // 收件人
	private String remark;
	private List<OrderItem> items;
    private String deliveryTime;
	
	public Order() {
	}

	public Order(User user,String code,String addr, String phone, String recipient, String remark,
			List<OrderItem> items,String deliveryTime) {
		super();
		this.createrCode=user.getCode();
		this.creater=user.getName();
		this.code=code;
		this.addr = addr;
		this.phone = phone;
		this.recipient = recipient;
		this.remark = remark;
		this.items = items;
		this.deliveryTime=deliveryTime;
	}

	public String getCode() {
		return code;
	}

	public String getCreater() {
		return creater;
	}

	public String getCreaterCode() {
		return createrCode;
	}

	public String getAddr() {
		return addr;
	}

	public String getPhone() {
		return phone;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getRemark() {
		return remark;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public OrderState getOrderState() {
		return orderState;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public static class OrderState {
		public static OrderState NORMAL = new OrderState("10", "订单生成");
		public static OrderState DELIVERY = new OrderState("20", "订单配送中");
		public static OrderState COMPLETE = new OrderState("30", "收货完成");
		public static OrderState CANCEL = new OrderState("40", "订单取消");
		private String code;
		private String name;

		public OrderState(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public OrderState() {
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public boolean eq(OrderState dELIVERY2) {
			if (this.code.equalsIgnoreCase(dELIVERY2.getCode())) {
				return true;
			}
			return false;
		}
	}

	public void cancel() {
		this.orderState = OrderState.CANCEL;
	}
}
