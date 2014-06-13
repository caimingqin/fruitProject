package app.util.mongodb;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import app.domain.Order;
import app.domain.User;

import com.mce.core.inject.Bean;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Bean(name = "DomainModelQuery")
public class DomainModelQueryImpl implements DomainModelQuery {

	private MongodbManager mongodbManager;
	private Map<String,AtomicInteger> codeMapper = new ConcurrentHashMap<String,AtomicInteger>();
	private String codeCollName = "codes";
	private Log logger=LogFactory.getLog(this.getClass().getName());

	public DomainModelQueryImpl() {
	}
	
	public DomainModelQueryImpl(MongodbManager mq) {
		this.mongodbManager = mq;
	}

	@Autowired
	public void setDB(MongodbManager m) {
		this.mongodbManager = m;
		initCodes();
	}

	private void initCodes() {
		synchronized(this){
			DBCollection col = this.mongodbManager.getDBCollection(this.codeCollName);
			DBCursor dc = col.find();
			if(dc.hasNext()){
				while(dc.hasNext()){
					DBObject dbo = dc.next();
					String type = (String) dbo.get("type");
					Integer i = (Integer)dbo.get("ins");
					codeMapper.put(type, new AtomicInteger(i));
				}
			}
			else{
				DBObject dbo = getDBObject(User.COL,0);
				DBObject dbo2 = getDBObject(Order.COL,0);
				col.save(dbo);
				col.save(dbo2);
				
				codeMapper.put(User.COL, new AtomicInteger(0));
				codeMapper.put(Order.COL, new AtomicInteger(0));
				
			}
		}
		
	}

	private DBObject getDBObject(String typeName, int i) {
		DBObject dbo = new BasicDBObject("type",typeName);
		dbo.put("ins", i);
		dbo.put("_id", typeName);
		return dbo;
	}

	private NumberFormat formatter = new DecimalFormat("0000");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	@Override
	public String nextCode(String type){
		AtomicInteger nextInteger = codeMapper.get(type);
		if(nextInteger == null){
			throw new IllegalArgumentException("not found code type["+type+"]");
		}
		int nextCount = nextInteger.addAndGet(1);
		String str = this.formatter.format(nextCount);
		String ss = sdf.format(new Date());
		this.logger.info("===========================>>>"+ss);
		//update type and next count
		DBCollection collection = mongodbManager.getDBCollection(codeCollName);
		DBObject dbObject = getDBObject(type, nextCount);
		collection.save(dbObject);
		return ss+str;
	}


}
