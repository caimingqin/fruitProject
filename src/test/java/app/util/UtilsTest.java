package app.util;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import app.domain.Order;
import app.domain.User;
import app.util.mongodb.MongodbManagerImpl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class UtilsTest {

	@Test
	public void test() {
		MongodbManagerImpl mongodbManagerImpl = new MongodbManagerImpl();
		mongodbManagerImpl.save("User", new User("123", "蔡明其",
				"737895494@qq.com", "12343", "dizhi"));
	}

	@Test
	public void testDrop() {
		MongodbManagerImpl mongodbManagerImpl = new MongodbManagerImpl();
		mongodbManagerImpl.drop("User");
	}
	@Test
	public void testLike() {
		MongodbManagerImpl mongodbManagerImpl = new MongodbManagerImpl();
		mongodbManagerImpl.drop("User");
		BasicDBObject queryDBObject = new BasicDBObject();
		Pattern pattern = Pattern.compile( "^.*"+"20140614"+".*$", Pattern.CASE_INSENSITIVE);
		BasicDBObject reg = new BasicDBObject();
		reg.put("$regex", pattern);
		queryDBObject.put("code", reg);
		List<DBObject> query = mongodbManagerImpl.query(Order.COL, queryDBObject);
		System.out.println(query);
	}

//	@Test
//	public void baiduMongoDB() throws UnknownHostException {
//
//		String databaseName = "JtWjQDnnBWdTFATFndoo";
//		String host = "mongo.duapp.com";
//		String port = "8908";
//		String username = "zhd2xIKGILmGMZGGi0V2SQ3k";// 用户名(api key);
//		String password = "GeIh3lizU1z6YvwG1GC1ShIuDxkYaKi7";// 密码(secret key)
//		String serverName = host + ":" + port;
//		MongoClient mongoClient = new MongoClient(
//				new ServerAddress(serverName), Arrays.asList(MongoCredential
//						.createMongoCRCredential(username, databaseName,
//								password.toCharArray())),
//				new MongoClientOptions.Builder().cursorFinalizerEnabled(false)
//						.build());
//		DB mongoDB = mongoClient.getDB(databaseName);
//		mongoDB.authenticate(username, password.toCharArray());
//		DBCollection mongoCollection = mongoDB.getCollection("test_mongo");
//
//		DBObject data1 = new BasicDBObject();
//		data1.put("no", 2007);
//		data1.put("name", "this is a test message");
//		mongoCollection.insert(data1);
//		mongoClient.close();
//
//	}

	@Test
	public void baiduMysql() throws UnknownHostException {

		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			/***** 1. 填写数据库相关信息(请查找数据库详情页) *****/
			String databaseName = " pzJfgNXovjYJzdRhCsHv ";
			String host = "sqld.duapp.com";
			String port = "4050";
			String username = "zhd2xIKGILmGMZGGi0V2SQ3k";// 用户名(api key);zhd2xIKGILmGMZGGi0V2SQ3k
			String password = "GeIh3lizU1z6YvwG1GC1ShIuDxkYaKi7";// 密码(secret
																	// key)
			String driverName = "com.mysql.jdbc.Driver";
			String dbUrl = "jdbc:mysql://";
			String serverName = host + ":" + port + "/";
			String connName = dbUrl + serverName + databaseName;

			/****** 2. 接着连接并选择数据库名为databaseName的服务器 ******/
			Class.forName(driverName);
			connection = DriverManager.getConnection(connName, username,
					password);
			stmt = connection.createStatement();
			/* 至此连接已完全建立，就可对当前数据库进行相应的操作了 */
			/* 3. 接下来就可以使用其它标准mysql函数操作进行数据库操作 */
			// 创建一个数据库表
			sql = "create table if not exists test_mysql("
					+ "id int primary key auto_increment," + "no int, "
					+ "name varchar(1024)," + "key idx_no(no))";
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
