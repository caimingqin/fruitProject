package app.domain;

import com.mce.domain.model.AbstractModel;

public class User extends AbstractModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String addr;
    
	public User() {
		super();
	}
	public User(String code, String name, String email, String phone,
			String addr) {
		this.code = code;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.addr = addr;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public String getAddr() {
		return addr;
	}
    
}
