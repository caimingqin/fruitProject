package app.web.reader.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.domain.User;
import app.util.mongodb.MongodbManager;

import com.mce.core.inject.BeanInjector;
import com.mongodb.DBObject;

@Controller
@RequestMapping("user")
public class UserController {
	
	private MongodbManager manager;
	@Autowired
	public void setBeanProvider(BeanInjector bp) {
		this.manager = bp.getBean(MongodbManager.class);
	}
	
	@ResponseBody
	@RequestMapping("/")
	public List<DBObject> findAll()  {
		return manager.queryAll(User.COL);
	}

}
