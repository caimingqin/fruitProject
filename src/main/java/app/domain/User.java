package app.domain;

import com.mce.domain.model.AbstractModel;

public class User extends AbstractModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String  COL="USER";
	public static final String KEY="name";
	public static final String REG_EVENT="reguser";
	public static final String UPDATE_EVENT="UpdateUser";
    private String code;
    private String name;
    private String phone;
    private String addr;
    private String password;
    private String cPassword;
    
    
	public User(String name, String phone, String password, String cPassword) {
		this(name, phone, phone, password, cPassword);
	}
	public User(String logInName, String phone, String addr,
			String password, String cPassword) {
		super();
		this.name = logInName;
		this.phone = phone;
		this.addr = addr;
		this.password = password;
		this.cPassword = cPassword;
	}
	public User() {
		super();
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
    
	public void update(String password,String phone,String addr ){
		this.password=password;
		this.phone=phone;
		this.addr=addr;
	}
}
