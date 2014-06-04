package app.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.domain.User;

@Controller("test")
@RequestMapping("test")
public class TestController {
	private Log log=LogFactory.getLog(this.getClass().getName());
	@ResponseBody
	@RequestMapping("/")
  public User get(){
		log.info("get now===========>>>");
	  return new User("111", "蔡名琴", "email", "phone", "addr");
  }
}
