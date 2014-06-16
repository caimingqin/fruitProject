package app.web.writer.command.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import app.domain.User;
import app.util.ServletUtils;
import app.web.UserMap;

import com.mce.command.AutoCommand;
import com.mce.command.Command;
import com.mce.command.CommandContext;
import com.mce.command.CommandHandleException;
import com.mce.command.CommandInterceptor;
import com.mce.core.UserSession;
import com.mce.util.StringUtils;

public class UserInterceptor implements CommandInterceptor {

	private Log logger = LogFactory.getLog(this.getClass().getName());

	@Override
	public void before(CommandContext arg0, Command cmd) throws Exception {
		if (needIntercept(cmd)) {
			logger.info("Strart UserCommandInterceptor now");
			HttpServletRequest sr = (HttpServletRequest) arg0
					.getParameter(HttpServletRequest.class.getName());
			String sessionId = ServletUtils.getValueFromCookie(sr,
					User.SESSION_ID);
			if (sessionId == null) {
				logger.info("not found session from cookie");
				sessionId = ServletUtils.getValueFromHead(sr, User.SESSION_ID);
				if (StringUtils.isNull(sessionId)) {
					throw new CommandHandleException(
							CommandHandleException.ERROR,"not found session key");
				}
			}
			logger.info("Current user sessionId[" + sessionId + "]");
			User user = UserMap.get().getUser(sessionId);
			logger.info("Current user[" + user.getName() + "]");
			UserSession.instance().put(User.class.getName(), user);
		}
	}

	@Override
	public void after(CommandContext arg0, Command cmd) throws Exception {
		if (needIntercept(cmd)) {
			UserSession.instance().remove(User.class.getName());
		}
	}

	private boolean needIntercept(Command cmd) {
		AutoCommand ac = cmd.getClass().getAnnotation(AutoCommand.class);
		if (ac.name().equalsIgnoreCase("RegUserCommand")) {
			return false;
		}
		return true;
	}
}
