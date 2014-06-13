package app.util.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import app.util.JsonParser;

import com.mce.core.inject.Bean;
import com.mce.domain.model.AbstractModel;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.util.JSON;

@Bean(name = "mongodbManager")
public class MongodbManagerImpl implements MongodbManager {

	private static final Log logger = LogFactory
			.getLog(MongodbManagerImpl.class.getName());
	private static Mongo mongo = null;
	private static DB MONGO_DB = null;
	private static final String MONGODB_NAME = "webdb";

	private static final String HOST = "127.0.0.1";
	private static final BasicDBObject FILTER_ID = new BasicDBObject("_id", 0);
	static {
		try {
			MongoOptions mongoOptions = new MongoOptions();
			mongoOptions.setThreadsAllowedToBlockForConnectionMultiplier(40);
			mongo = new Mongo(HOST, mongoOptions);
			MONGO_DB = mongo.getDB(MONGODB_NAME);
		} catch (UnknownHostException e) {
			logger.info("create  Mongo Exception " + e.getMessage());
		}
	}



	@Override
	public DB getDB() {
		return MONGO_DB;
	}

	@Override
	public DBCollection getDBCollection(String name) {
		return MONGO_DB.getCollection(name);
	}

	@Override
	public List<DBObject> query(String colName, DBObject queryDBObject) {
		return query(colName, queryDBObject, new BasicDBObject());
	}

	@Override
	public List<DBObject> query(String colName, DBObject queryDBObject,
			DBObject filterQueryDBObject) {
		DBCollection dbCollection = this.getDBCollection(colName);
		if (filterQueryDBObject == null) {
			filterQueryDBObject = FILTER_ID;
		} else {
			filterQueryDBObject.put("_id", 0);
		}
		DBCursor dbCursor = dbCollection.find(queryDBObject,
				filterQueryDBObject).limit(1000);
		List<DBObject> array = dbCursor.toArray();
		dbCursor.close();
		return array;
	}

	@Override
	public List<DBObject> query(String colName, DBObject queryDBObject,
			DBObject filterQueryDBObject, DBObject orderBy, int limit) {
		DBCollection dbCollection = this.getDBCollection(colName);
		if (queryDBObject == null) {
			queryDBObject = new BasicDBObject();
		}
		if (filterQueryDBObject == null) {
			filterQueryDBObject = FILTER_ID;
		} else {
			filterQueryDBObject.put("_id", 0);
		}
		DBCursor dbCursor = dbCollection.find(queryDBObject,
				filterQueryDBObject);
		if (orderBy != null) {
			dbCursor.sort(orderBy);
		}
		dbCursor.limit(limit);
		List<DBObject> array = dbCursor.toArray();
		dbCursor.close();
		return array;
	}

	@Override
	public DBObject findDBObject(String colName, String key, String value) {
		if (key == null || value == null) {
			return null;
		}
		DBCollection collection = this.getDBCollection(colName);
		BasicDBObject basicDBObject = new BasicDBObject(key, value);
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 0);
		return collection.findOne(basicDBObject, fields);
	}

	@Override
	public <T> T get(Class<T> clazz, String colName, String key, String value) {
		DBObject dbObject = findDBObject(colName, key, value);
		return toClass(dbObject, clazz);
	}

	@Override
	public <T> T toClass(DBObject dbObject, Class<T> clazz) {
		if (dbObject == null) {
			return null;
		}
		return JsonParser.decode(dbObject.toString(), clazz);
	}

	@Override
	public <T> List<T> toList(List<DBObject> listDbObjects, Class<T> clazz) {
		List<T> arr = new ArrayList<T>();
		if (listDbObjects.size() == 0) {
			return arr;
		}
		for (DBObject dbObject : listDbObjects) {
			arr.add(JsonParser.decode(dbObject.toString(), clazz));
		}
		return arr;
	}

	@Override
	public DBObject findDBObject(String colName, DBObject query, DBObject filter) {
		if (filter != null) {
			return this.getDBCollection(colName).findOne(query, filter);
		}
		return this.getDBCollection(colName).findOne(query,
				MongodbManagerImpl.FILTER_ID);
	}

	@Override
	public void save(String colName, Object obj) {

		DBCollection dbCollection = MONGO_DB.getCollection(colName);
		DBObject dbObject = toDBObject(obj);
		if (obj instanceof AbstractModel) {
			AbstractModel abstractModel = (AbstractModel) obj;
			dbObject.put("_id", abstractModel.getObsId());
		}
		dbCollection.save(dbObject);

	}

	@Override
	public void remove(String colName, String key, String value) {
		DBCollection dbCollection = getDBCollection(colName);
		DBObject dbObject = new BasicDBObject(key, value);
		dbCollection.remove(dbObject);
	}

	private DBObject toDBObject(Object obj) {
		String json = JsonParser.encode(obj);
		return (DBObject) JSON.parse(json);
	}

	@Override
	public void drop(String collectionName) {
		MONGO_DB.getCollection(collectionName).drop();

	}

	@Override
	public List<DBObject> queryAll(String colName) {
		return query(colName, new BasicDBObject());
	}

	@Override
	public DBObject findDBObject(String collectionName,
			BasicDBObject queryObject) {
		return findDBObject(collectionName, queryObject, null);
	}

}
