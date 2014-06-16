package app.web.reader.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.domain.Order;
import app.util.mongodb.MongodbManager;

import com.mce.core.inject.BeanInjector;
import com.mce.test.OnlyForTest;
import com.mce.util.DateUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
@RequestMapping("order")
public class OrderController {

	private MongodbManager manager;

	@Autowired
	public void setBeanProvider(BeanInjector bp) {
		this.manager = bp.getBean(MongodbManager.class);
	}
	
	@OnlyForTest
	public void setMongodbManager(MongodbManager manager) {
		this.manager = manager;
	}


	@ResponseBody
	@RequestMapping("/")
	public List<DBObject> findAll() {
		return manager.queryAll(Order.COL);
	}

	@ResponseBody
	@RequestMapping("/day")
	public List<DBObject> findByCurrnetDay() {
		
		String currentDay = DateUtils.yyyymmdd.format(new Date());
		
		return manager.likeQuery(Order.COL, Order.KEY, currentDay);
	}
	
	
	@ResponseBody
	@RequestMapping("/user/{userCode}")
	public List<DBObject> find(String userCode) {
		
		String currentDay = DateUtils.yyyymmdd.format(new Date());
		DBObject likeQueryDBObject = manager.getLikeQueryDBObject(currentDay);
		DBObject qo=new BasicDBObject("creater.code", userCode);
		qo.put(Order.KEY, likeQueryDBObject);
		
		return manager.query(Order.COL, qo);
	}
}
