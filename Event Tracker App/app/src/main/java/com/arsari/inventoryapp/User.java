package com.arsari.inventoryapp;

/**
 * User java code.
 * <p>
 * The User class include the funcionality to model the
 * constructor to manage the user table in the database.
 *
 * @author	Arturo Santiago-Rivera <i>asantiago@arsari.com</i>
 * @course	CS-360-X6386 Mobile Architect & Programming 21EW6
 * @college	Southern New Hampshire University
 */
public class User {

    int id;
	String user_name;
	String user_phone;
	String user_email;
	String user_pass;

    public User() {
        super();
    }

    public User(int i, String name, String phone, String email, String password) {
        super();
        this.id = i;
		this.user_name = name;
        this.user_phone = phone;
        this.user_email = email;
		this.user_pass = password;
    }

    // constructor
    public User(String name, String phone, String email, String password) {
		this.user_name = name;
		this.user_phone = phone;
		this.user_email = email;
		this.user_pass = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public String getUserName() {
        return user_name;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }

	public String getUserPhone() {
		return user_phone;
	}

	public void setUserPhone(String phone) {
		this.user_phone = phone;
	}

	public String getUserEmail() {
		return user_email;
	}

	public void setUserEmail(String email) {
		this.user_email = email;
	}

	public String getUserPass() {
        return user_pass;
    }

    public void setUserPass(String pass) {
        this.user_pass = pass;
    }
}
