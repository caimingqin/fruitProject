package app.util;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ServletUtils {
	
	private final static Log logger = LogFactory.getLog(ServletUtils.class.getName());
	public final static String HTTP_JSON_CONTENTS = "application/json";

	public static String getValueFromCookie(HttpServletRequest req,String key) {
		
		Cookie[] cArray = req.getCookies();
		if(cArray == null
				|| cArray.length<1){
			logger.info("cookie is null");
			return null;
		}
		String uSession = null;
		for(Cookie c:cArray){
			logger.debug(c.getName());
			if(key.equalsIgnoreCase(c.getName())){
				uSession = c.getValue();
				break;
			}
		}
		return uSession;
	}


	public static void removeCookie(HttpServletRequest req,String key) {
		Cookie[] cArray = req.getCookies();
		if(cArray == null
				|| cArray.length<1){
			logger.info("cookie is null");
		}
		for(Cookie c:cArray){
			if(key.equalsIgnoreCase(c.getName())){
				logger.info("Cookie value["+c.getName()+"] is invalid and reset cookie");				
				c.setMaxAge(0);
				break;
			}
		}		
	}


	public static void addCookieValue(String sessionId, String code,HttpServletResponse res) {
		// TODO Auto-generated method stub
		Cookie c = new Cookie(sessionId,code);
		c.setMaxAge(60*60);
		c.setPath("/");
		res.addCookie(c);
	}


	public static String getValueFromHead(HttpServletRequest sr,
			String sessionId) {
		return sr.getHeader(sessionId);
	}
	
	
	public static Cookie setValue(String name,String value,int timeOut){
		Cookie c = new Cookie(name,value);
		c.setMaxAge(timeOut);
		c.setPath("/");
		return c;
		
	}
	
	public static Cookie setValue(String name,String value){
		return setValue(name,value,60*60);
	}		
}
