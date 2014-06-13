package app.web.writer.command;

import org.springframework.beans.factory.annotation.Autowired;

import app.domain.Order;
import app.domain.Order.OrderState;
import app.util.mongodb.MongodbManager;

import com.mce.command.AbstractEventCommand;
import com.mce.command.AutoCommand;
import com.mce.command.Command;
import com.mce.command.CommandHandleException;
import com.mce.command.DomainEventGather;
import com.mce.domain.event.DomainEvent;
import com.mce.util.StringUtils;

@AutoCommand(name = "CancelOrderCommand")
public class CancelOrderCommand extends AbstractEventCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orderCode;

	@Autowired
	private MongodbManager manager;

	@Override
	public Object execute(DomainEventGather deg) {
		if (StringUtils.isNull(orderCode)) {
			throw new CommandHandleException("订单编号不能为空！");
		}
		Order order = manager.get(Order.class, Order.COL, Order.KEY, this.orderCode);
		if(order.getOrderState().eq(OrderState.DELIVERY)){
			throw new CommandHandleException("订单正在配送中，无法取消！");
		}
		order.cancel();
		deg.setDomainEvent(new DomainEvent(Order.CANCEL_EVENT,order));
		return Command.SUCCESS;
	}

	public String getOrderCode() {
		return orderCode;
	}

}
