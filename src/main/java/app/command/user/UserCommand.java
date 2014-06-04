package app.command.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import app.domain.User;

import com.mce.command.AbstractEventCommand;
import com.mce.command.AutoCommand;
import com.mce.command.DomainEventGather;
import com.mce.domain.event.DomainEvent;

@AutoCommand(name="UserCommand")
public class UserCommand extends AbstractEventCommand{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4769153637879988899L;
	private Log log = LogFactory.getLog(this.getClass());
	private String code;
	private String name;
	private String addr;
	
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getAddr() {
		return addr;
	}

	@Override
	public Object execute(DomainEventGather deg) {
		log.info(code+","+name+","+addr);
		User user = new User(code, name, "email", "phone", addr);
		deg.setDomainEvent(new DomainEvent("saveUser", user));
		return user;
	}

}
