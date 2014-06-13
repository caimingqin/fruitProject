package app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import app.domain.Order;
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
			if(dName.equalsIgnoreCase(User.REG_EVENT)||dName.equalsIgnoreCase(User.UPDATE_EVENT)){
				User u=(User) de.getTarget();
				log.info("========>>>"+u.getName());
				mongodbManager.save(User.COL, u);
			}else if(dName.equalsIgnoreCase(Order.CREATE_EVENT)||dName.equalsIgnoreCase(Order.CANCEL_EVENT)){
				mongodbManager.save(Order.COL, de.getTarget());
			}
		}
	}

}
