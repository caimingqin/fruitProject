package app.util;

import org.junit.Test;

import app.domain.User;
import app.util.mongodb.MongodbManagerImpl;

public class UtilsTest {

	@Test
	public void test(){
		MongodbManagerImpl mongodbManagerImpl = new MongodbManagerImpl();
		mongodbManagerImpl.save("User", new User("123", "蔡明其", "737895494@qq.com", "12343", "dizhi"));
	}
	
	@Test
	public void testDrop(){
		MongodbManagerImpl mongodbManagerImpl = new MongodbManagerImpl();
		mongodbManagerImpl.drop("User");
	}
}
