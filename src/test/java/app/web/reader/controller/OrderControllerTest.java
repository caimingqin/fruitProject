package app.web.reader.controller;

import org.junit.Test;

import app.util.mongodb.MongodbManagerImpl;

public class OrderControllerTest {

	private OrderController oc=new OrderController();
	@Test
	public void test(){
	  MongodbManagerImpl mongodbManagerImpl = new MongodbManagerImpl();
	  oc.setMongodbManager(mongodbManagerImpl);
	  System.out.println(oc.findByCurrnetDay());
	}
}
