package app.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import app.domain.User;
import app.util.ServletUtils;
import app.util.mongodb.MongodbManager;

import com.mce.core.ResponsibleMessage;
import com.mce.core.inject.BeanInjector;
import com.mce.util.IdUtils;
import com.mce.util.jackson.JacksonUtils;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MongodbManager manager;
	private Log logger = LogFactory.getLog(this.getClass().getName());

	@Override
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		BeanInjector bi = (BeanInjector) sc.getServletContext().getAttribute(
				BeanInjector.class.getName());
		this.manager = bi.getBean(MongodbManager.class);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		doPost(req, res);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {

		String uname = req.getParameter("uname");
		String pass = req.getParameter("passwd");

		logger.info("User name[" + uname + "] upass[" + pass + "]");

		User user = UserMap.get().getUserByCode(uname);
		if (user == null) {
			user = manager.get(User.class, User.COL, User.KEY, uname);
		}
		if (user == null) {
			logger.info("not found user ");
			writeErrorMessage(HttpServletResponse.SC_BAD_REQUEST, "用户不存在", res);
			return;
		}
		if(!user.getPassword().equalsIgnoreCase(pass)){
			logger.info(" found user but password is wrong ");
			writeErrorMessage(HttpServletResponse.SC_BAD_REQUEST, "密码错误", res);
			return;
		}
		String sessionId = IdUtils.getUUID();
		user.login(sessionId);
		manager.save(User.COL,user);//同步最新数据
		UserMap um = UserMap.get();
		
		um.put(sessionId, user);
		logger.info("userSessionMap size:"+um.getUserSessionMap().size());
		res.setContentType("application/json");

		Cookie cos = ServletUtils.setValue(User.SESSION_ID, user.getSessionId());
		res.addCookie(cos);

		ResponsibleMessage rm = new ResponsibleMessage();
		rm.setStatusCode(HttpServletResponse.SC_OK);
		rm.setContents(user);
		String resMessage = JacksonUtils.writeValueString(rm);
		res.getWriter().write(resMessage);
	}

	private void writeErrorMessage(int scBadRequest, String msg,
			HttpServletResponse res) throws IOException {
		res.setStatus(scBadRequest);
		// res.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=UTF-8");
		// res.getOutputStream().write(string.getBytes());
		res.getWriter().write(msg);
	}
}
