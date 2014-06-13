package app.util.mongodb;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public interface MongodbManager {

	DBCollection getDBCollection(String name);

	DBObject findDBObject(String colName, String key, String value);

	DBObject findDBObject(String colName, DBObject query, DBObject filter);

	List<DBObject> queryAll(String colName);
	List<DBObject> query(String colName, DBObject queryDBObject);

	List<DBObject> query(String colName, DBObject queryDBObject, DBObject filterQueryDBObject);
	List<DBObject> query(String colName, DBObject queryDBObject, DBObject filterQueryDBObject,DBObject orderBy,int limit);

	<T> T toClass(DBObject dbObject, Class<T> clazz);

	<T> List<T> toList(List<DBObject> dbObject, Class<T> clazz);

	void save(String colName, Object obj);

	void drop(String collectionName);

	DB getDB();

	void remove(String colName, String key, String value);

	DBObject findDBObject(String collectionName, BasicDBObject queryObject);

	<T> T get(Class<T> clazz, String colName, String key, String value);
    
}
