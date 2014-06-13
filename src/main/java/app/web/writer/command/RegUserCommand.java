package app.web.writer.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import app.domain.User;
import app.util.mongodb.DomainModelQuery;
import app.util.mongodb.MongodbManager;

import com.mce.command.AbstractEventCommand;
import com.mce.command.AutoCommand;
import com.mce.command.Command;
import com.mce.command.CommandHandleException;
import com.mce.command.DomainEventGather;
import com.mce.domain.event.DomainEvent;
import com.mce.util.StringUtils;

@AutoCommand(name ="RegUserCommand" )
public class RegUserCommand extends AbstractEventCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4769153637879988899L;
	private Log log = LogFactory.getLog(this.getClass());
	private String logInName;
	private String password;
	private String cPassword;
	private String phone;
	
	@Autowired
	private MongodbManager manager;
	@Autowired
    private DomainModelQuery domainModelQuery;

	@Override
	public Object execute(DomainEventGather deg) {
		log.info( "," + logInName + "," );
		if(StringUtils.isNull(logInName)){
			throw new CommandHandleException("用户名不能为空！");
		}
		if(StringUtils.isNull(password)||StringUtils.isNull(cPassword)){
			throw new CommandHandleException("密码不能为空！");
		}
		if(!password.equalsIgnoreCase(cPassword)){
			throw new CommandHandleException("密码不一致！");
		}
		String userCode = domainModelQuery.nextCode(User.COL);
		User user = new User(userCode,logInName, phone, password, cPassword);
		deg.setDomainEvent(new DomainEvent(User.REG_EVENT,user));
		return Command.SUCCESS;
	}
	
	public String getLogInName() {
		return logInName;
	}

	public String getPassword() {
		return password;
	}

	public String getcPassword() {
		return cPassword;
	}

	public String getPhone() {
		return phone;
	}
	

}
