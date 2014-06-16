package app.domain;

import com.mce.domain.model.AbstractModel;

public class User extends AbstractModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SESSION_ID = "client-id";
	public static final String  COL="user";
	public static final String KEY="name";
	public static final String REG_EVENT="reguser";
	public static final String UPDATE_EVENT="UpdateUser";
    private String code;
    private String name;
    private String phone;
    private String addr;
    private String password;
    private String cPassword;
	private String sessionId;
	public User() {
		super();
	}
	public User(String userCode, String logInName, String phone2,
			String password2, String cPassword2) {
		this.code=userCode;
		this.name=logInName;
		this.phone=phone2;
		this.password=password2;
		this.cPassword=cPassword2;
	}

	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getPhone() {
		return phone;
	}
	public String getAddr() {
		return addr;
	}
	public String getPassword() {
		return password;
	}
	public String getcPassword() {
		return cPassword;
	}
    
	public String getSessionId() {
		return sessionId;
	}
	public void update(String password,String phone,String addr ){
		this.password=password;
		this.phone=phone;
		this.addr=addr;
	}
	public void login(String sessionId2) {
		this.sessionId=sessionId2;
		
	}
}
