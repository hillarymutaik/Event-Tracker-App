package com.arsari.inventoryapp;

/**
 * Items java code.
 * <p>
 * The Item class include the funcionality to model the
 * constructor to manage the item table in the database.
 *
 * @author	Arturo Santiago-Rivera <i>asantiago@arsari.com</i>
 * @course	CS-360-X6386 Mobile Architect & Programming 21EW6
 * @college	Southern New Hampshire University
 */
public class Item {

    int id;
	String user_email;
    String desc;
    String qty;
	String unit;

    public Item() {
        super();
    }

    public Item(int i, String email, String description, String quantity, String unit) {
        super();
        this.id = i;
		this.user_email = email;
        this.desc = description;
        this.qty = quantity;
		this.unit = unit;
    }

    // constructor
    public Item(String email, String description, String quantity, String unit) {
		this.user_email = email;
        this.desc = description;
        this.qty = quantity;
		this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public String getUserEmail() {
        return user_email;
    }

    public void setUserEmail(String user_email) {
        this.user_email = user_email;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

	public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
