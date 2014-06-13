package app.web.writer.command;

import org.springframework.beans.factory.annotation.Autowired;

import app.domain.User;
import app.util.mongodb.MongodbManager;

import com.mce.command.AbstractEventCommand;
import com.mce.command.AutoCommand;
import com.mce.command.Command;
import com.mce.command.CommandHandleException;
import com.mce.command.DomainEventGather;
import com.mce.domain.event.DomainEvent;
import com.mce.util.StringUtils;

@AutoCommand(name = "UpdateUserCommand")
public class UpdateUserCommand extends AbstractEventCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String phone;
	private String addr;
	private String password;
	
	@Autowired
	private MongodbManager manager;

	@Override
	public Object execute(DomainEventGather deg) {
		
		if(StringUtils.isNull(name)){
			throw new CommandHandleException("用户名不能为空！");
		}
		if(StringUtils.isNull(password)){
			throw new CommandHandleException("密码不能为空！");
		}
		
		if(StringUtils.isNull(phone)){
			throw new CommandHandleException("手机号不能为空！");
		}
		
		if(StringUtils.isNull(addr)){
			throw new CommandHandleException("收货地址不能为空！");
		}
		
		User user = manager.get(User.class, User.COL, User.KEY, this.name);
		if(user==null){
			throw new CommandHandleException("用户不存在！");
		}
		user.update(password,phone,addr);
		deg.setDomainEvent(new DomainEvent(User.UPDATE_EVENT,user));
		return Command.SUCCESS;
	}





	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddr() {
		return addr;
	}

	public String getPassword() {
		return password;
	}
	
	

}
