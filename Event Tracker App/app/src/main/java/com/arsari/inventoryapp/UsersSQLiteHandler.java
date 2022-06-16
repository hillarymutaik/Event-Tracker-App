package com.arsari.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User SQLite Handler java code.
 * <p>
 * The UsersSQLiteHandler class include the funcionality to manage
 * all the CRUD operations to of the user database.
 *
 * @author	Arturo Santiago-Rivera <i>asantiago@arsari.com</i>
 * @course	CS-360-X6386 Mobile Architect & Programming 21EW6
 * @college	Southern New Hampshire University
 */
public class UsersSQLiteHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "UsersData.DB";
    public static final String TABLE_NAME = "UsersTable";

    public static final String COLUMN_0_ID = "id";
    public static final String COLUMN_1_NAME = "name";
    public static final String COLUMN_2_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_3_EMAIL = "email";
    public static final String COLUMN_4_PASSWORD = "password";

	private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " +
		TABLE_NAME + " (" +
		COLUMN_0_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
		COLUMN_1_NAME + " VARCHAR, " +
		COLUMN_2_PHONE_NUMBER + " VARCHAR, " +
		COLUMN_3_EMAIL + " VARCHAR, " +
		COLUMN_4_PASSWORD + " VARCHAR" + ");";

	public UsersSQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

	/**
	 * Database CRUD (Create, Read, Update, Delete) Operations
	 */

	// Add item to database
	public void createUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_1_NAME, user.getUserName());
		values.put(COLUMN_2_PHONE_NUMBER, user.getUserPhone());
		values.put(COLUMN_3_EMAIL, user.getUserEmail());
		values.put(COLUMN_4_PASSWORD, user.getUserPass());

		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	// Read item from Database
	public User readUser(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME,
			new String[] { COLUMN_0_ID, COLUMN_1_NAME, COLUMN_2_PHONE_NUMBER, COLUMN_3_EMAIL, COLUMN_4_PASSWORD }, COLUMN_0_ID + " = ?",
			new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		User user = new User(Integer.parseInt(Objects.requireNonNull(cursor).getString(0)),
			cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

		cursor.close();

		return user;
	}

	// Update user in database
	public int updateUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_1_NAME, user.getUserName());
		values.put(COLUMN_2_PHONE_NUMBER, user.getUserPhone());
		values.put(COLUMN_3_EMAIL, user.getUserEmail());
		values.put(COLUMN_4_PASSWORD, user.getUserPass());

		return db.update(TABLE_NAME, values, COLUMN_0_ID + " = ?", new String[] { String.valueOf(user.getId()) });
	}

	// Delete user from database
	public void deleteItem(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_NAME, COLUMN_0_ID + " = ?", new String[] { String.valueOf(user.getId()) });
		db.close();
	}

	/**
	 * Global Database Operations
	 */

	// Getting All Users
	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<>();

		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				User user = new User();
				user.setId(Integer.parseInt(cursor.getString(0)));
				user.setUserName(cursor.getString(1));
				user.setUserPhone(cursor.getString(2));
				user.setUserEmail(cursor.getString(3));
				user.setUserPass(cursor.getString(4));

				userList.add(user);
			} while (cursor.moveToNext());
		}

		cursor.close();

		return userList;
	}

	// Deleting All Users
	public void deleteAllUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME,null,null);
		db.close();
	}

	// Getting Users Count
	public int getUsersCount() {
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int usersTotal = cursor.getCount();
		cursor.close();

		return usersTotal;
	}
}
