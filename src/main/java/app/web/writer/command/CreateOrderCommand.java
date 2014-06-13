package app.web.writer.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import app.domain.Order;
import app.domain.OrderItem;
import app.util.mongodb.DomainModelQuery;

import com.mce.command.AbstractEventCommand;
import com.mce.command.AutoCommand;
import com.mce.command.Command;
import com.mce.command.CommandHandleException;
import com.mce.command.DomainEventGather;
import com.mce.domain.event.DomainEvent;
import com.mce.util.StringUtils;

@AutoCommand(name ="CreateOrderCommand")
public class CreateOrderCommand extends AbstractEventCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<OrderItem> items;
	private String addr;
	private String phone;
	private String recipient; // 收件人
	private String remark;
	@Autowired
    private DomainModelQuery domainModelQuery;
	@Override
	public Object execute(DomainEventGather deg) {
		if (StringUtils.isNull(addr)) {
			throw new CommandHandleException("收货地址不能为空！");
		}
		if (StringUtils.isNull(phone)) {
			throw new CommandHandleException("联系方式不能为空！");
		}
		if (StringUtils.isNull(recipient)) {
			throw new CommandHandleException("收货人不能为空");
		}
		if (items.isEmpty()) {
			throw new CommandHandleException("订单列表不能为空！");
		}
		String orderCode = domainModelQuery.nextCode(Order.COL);
		Order order = new Order(orderCode,addr, phone, recipient, remark, items);
		deg.setDomainEvent(new DomainEvent(Order.CREATE_EVENT, order));
		return Command.SUCCESS;
	}

	public List<OrderItem> getItems() {
		return items;
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

}
