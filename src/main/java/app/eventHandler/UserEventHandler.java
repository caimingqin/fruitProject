package app.eventHandler;

import app.domain.User;

import com.mce.domain.event.DomainEvent;
import com.mce.domain.event.handler.AutoEventHandler;

@AutoEventHandler(name="UserEventHandler")
public class UserEventHandler {

	@AutoEventHandler(name="saveUser")
	public void saveUser(DomainEvent de){
		User u=(User) de.getTarget();
		System.out.println("user:"+u);
	}
}
