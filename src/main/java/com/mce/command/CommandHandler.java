package com.mce.command;

import com.mce.util.LoggerUtils;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class CommandHandler {
	private List<CommandInterceptor> cInterceptors = null;
	private Log logger = LogFactory.getLog(getClass().getName());

	public CommandHandler(List<CommandInterceptor> cInterceptors) {
		this.cInterceptors = cInterceptors;
	}

	private void handleAfterCommandInterceptor(CommandContext cc, Command command) {
		try {
			if (AbstractCommand.class.isAssignableFrom(command.getClass()))
				((AbstractCommand) command).clear();
		} finally {
			if ((this.cInterceptors != null) && (this.cInterceptors.size() > 0))
				for (CommandInterceptor ci : this.cInterceptors)
					try {
						ci.after(cc, command);//执行完后，清空当前线程的用户，RedisUserCommandInterceptor
					} catch (RuntimeException re) {
						LoggerUtils.showLog(re, this.logger);
					} catch (Exception ee) {
						LoggerUtils.showLog(ee, this.logger);
					} catch (Throwable e) {
						LoggerUtils.showLog(e, this.logger);
					}
		}
	}

	private void handleBeforeCommandInterceptor(Command eCommand, CommandContext cc)
			throws Exception {
		if ((this.cInterceptors != null) && (this.cInterceptors.size() > 0))
			for (CommandInterceptor ci : this.cInterceptors)
				ci.before(cc, eCommand);// 先拦截并取出用户，放到ThreadLocal中,给执行command的时候调用，如果用户未登录给抛异常
	}

	protected abstract Object handleCommand(Command paramCommand);

	public final Object handle(CommandContext cc, Command cmd) throws CommandHandleException {
		Object returnObs = null;
		try {
			this.logger.debug("Handle commands now");
			handleBeforeCommandInterceptor(cmd, cc);
			returnObs = handleCommand(cmd);
		} catch (CommandHandleException che) {
			this.logger.error(che);
			throw new CommandHandleException(500, che.getMessage());
		} catch (RuntimeException e) {
			this.logger.trace(e);
			throw new CommandHandleException(500, e);
		} catch (Exception e) {
			this.logger.error(e);
			throw new CommandHandleException(500, e);
		} finally {
			handleAfterCommandInterceptor(cc, cmd);
		}
		return returnObs;
	}
}
