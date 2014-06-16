package app.web;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import app.domain.User;

import com.mce.util.StringUtils;

public class UserMap {

	//重复的数据在
	private final Map<String, User> userSessionMap = new ConcurrentHashMap<String, User>();

	private static final UserMap instance = new UserMap();

	public static UserMap get() {
		return instance;
	}

	public User getUser(String sessionId) {
		if (StringUtils.isNull(sessionId)) {
			return null;
		}
		return userSessionMap.get(sessionId);
	}

	public User getUserByids(String ids) {
		Collection<User> users = this.userSessionMap.values();
		for (User user : users) {
			if (user.getCode().equalsIgnoreCase(ids)) {
				return user;
			}
		}
		return null;
	}

	public User getUserByCode(String loginName) {
		Collection<User> users = this.userSessionMap.values();
		for (User user : users) {
			if (user.getName().equalsIgnoreCase(loginName)) {
				return user;
			}
		}
		return null;
	}

	public void put(String sessionId, User user) {
		userSessionMap.put(sessionId, user);
	}


	public void remove(String sessionId) {
		userSessionMap.remove(sessionId);
	}

	public Map<String, User> getUserSessionMap() {
		return userSessionMap;
	}
	
	
}
