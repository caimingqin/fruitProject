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

import com.mce.core.inject.Bean;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Bean(name = "DomainModelQuery")
public class DomainModelQueryImpl implements DomainModelQuery {

	private MongodbManager mongodbManager;
//	private static String CODE_COUNTER_COLLETION = "CodeCounter";
//	private Log logger = LogFactory.getLog(this.getClass().getName());

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
//					Integer i = Integer.parseInt(ins);
					codeMapper.put(type, new AtomicInteger(i));
				}
			}
			else{
//				DBObject dbo = getDBObject(User.COLLECTION_NAME,0);
//				DBObject dbo2 = getDBObject(Customer.COLLECTION_NAME,0);
//				DBObject dbo3 = getDBObject(SurveyRequest.COLLECTION_NAME,0);
//				DBObject dbo4 = getDBObject(DocumentCategory.NAME,0);
//				DBObject dbo5 = getDBObject(DocumentPage.NAME,0);
//				DBObject dbo6 = getDBObject(SurveyReport.COLLECTION_NAME,0);
//				DBObject db07 = getDBObject(Invoice.COLLECTION_NAME,0);
				
//				col.save(dbo);
//				col.save(dbo2);
//				col.save(dbo3);
//				col.save(dbo4);
//				col.save(dbo5);
//				col.save(db07);
//				col.save(dbo6);
				
//				codeMapper.put(User.COLLECTION_NAME, new AtomicInteger(1));
//				codeMapper.put(Customer.COLLECTION_NAME, new AtomicInteger(1));
//				codeMapper.put(SurveyRequest.COLLECTION_NAME, new AtomicInteger(1));
//				codeMapper.put(DocumentCategory.NAME, new AtomicInteger(1));
//				codeMapper.put(DocumentPage.NAME, new AtomicInteger(1));
//				codeMapper.put(SurveyReport.COLLECTION_NAME, new AtomicInteger(1));
//				codeMapper.put(Invoice.COLLECTION_NAME, new AtomicInteger(1));
//				codeMapper.put(CustomerManager.COLLECTION_NAME, new AtomicInteger(1));
				
			}
		}
		
	}

	private DBObject getDBObject(String simpleName, int i) {
		DBObject dbo = new BasicDBObject("type",simpleName);
		dbo.put("ins", i);
		dbo.put("_id", simpleName);
		return dbo;
	}

	private NumberFormat formatter = new DecimalFormat("000000");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	
	
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

//	public String nextCode() {
//		AtomicInteger counter = null;
//		DBCollection collection = mongodbManager.getDBCollection(CODE_COUNTER_COLLETION);
//		DBObject dbObject = collection.findOne();
//		if(dbObject==null){
//			counter=new AtomicInteger(0);
//			dbObject=new BasicDBObject();
//			dbObject.put("code",counter.get()+"");
//		}else{
//			counter = new AtomicInteger(Integer.parseInt((String) dbObject.get("code")));;
//		}
//		String max =""+ counter.addAndGet(1);
//		dbObject.put("code", max);
//		collection.save(dbObject);
//		return max;
//	}


}
