package app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import app.domain.User;
import app.util.mongodb.MongodbManager;

import com.mce.core.inject.Bean;
import com.mce.core.notification.Notification;
import com.mce.core.notification.NotificationListener;
import com.mce.domain.event.DomainEvent;

@Bean(name="AppNotificationListener")
public class AppNotificationListener implements NotificationListener{
	
    private Log log=LogFactory.getLog(this.getClass().getName());
    
    @Autowired
    private MongodbManager mongodbManager;
	@Override
	public void handle(Notification n) {
		 String notificationName = n.getName();
		if(DomainEvent.isDomainEventNotification(notificationName)){
			DomainEvent de=(DomainEvent) n.getBody();
			String dName = de.getName();
			if(dName.equalsIgnoreCase("SaveUser")){
				User u=(User) de.getTarget();
				log.info("========>>>"+u.getCode());
				log.info("mongodbManager========>>>"+mongodbManager);
				mongodbManager.save("User", u);
				
			}
		}
	}

}
