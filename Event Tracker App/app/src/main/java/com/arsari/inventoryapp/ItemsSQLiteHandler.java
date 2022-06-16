package com.arsari.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Items SQLite Handler java code.
 * <p>
 * The ItemsSQLiteHandler class include the funcionality to manage
 * all the CRUD operations the user can perform in the app.
 *
 * @author	Arturo Santiago-Rivera <i>asantiago@arsari.com</i>
 * @course	CS-360-X6386 Mobile Architect & Programming 21EW6
 * @college	Southern New Hampshire University
 */
public class ItemsSQLiteHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "ItemsData.DB";
    private static final String TABLE_NAME = "ItemsTable";

    private static final String COLUMN_0_ID = "id";
    private static final String COLUMN_1_USER_EMAIL = "email";
    private static final String COLUMN_2_DESCRIPTION = "description";
    private static final String COLUMN_3_QUANTITY = "quantity";
	private static final String COLUMN_4_UNIT = "unit";

	private static final String CREATE_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " +
		TABLE_NAME + " (" +
		COLUMN_0_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
		COLUMN_1_USER_EMAIL + " VARCHAR, " +
		COLUMN_2_DESCRIPTION + " VARCHAR, " +
		COLUMN_3_QUANTITY + " VARCHAR, " +
		COLUMN_4_UNIT + " VARCHAR" + ");";

	public ItemsSQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEMS_TABLE);
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
	public void createItem(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_1_USER_EMAIL, item.getUserEmail());
		values.put(COLUMN_2_DESCRIPTION, item.getDesc());
        values.put(COLUMN_3_QUANTITY, item.getQty());
		values.put(COLUMN_4_UNIT, item.getUnit());

        db.insert(TABLE_NAME, null, values);
        db.close();
	}

	// Read item from Database
	public Item readItem(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
			new String[] { COLUMN_0_ID, COLUMN_1_USER_EMAIL, COLUMN_2_DESCRIPTION, COLUMN_3_QUANTITY, COLUMN_4_UNIT }, COLUMN_0_ID + " = ?",
            new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
			cursor.moveToFirst();

		Item item = new Item(Integer.parseInt(Objects.requireNonNull(cursor).getString(0)),
			cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

        cursor.close();

        return item;
	}

	// Update item in database
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
		values.put(COLUMN_1_USER_EMAIL, item.getUserEmail());
        values.put(COLUMN_2_DESCRIPTION, item.getDesc());
        values.put(COLUMN_3_QUANTITY, item.getQty());
		values.put(COLUMN_4_UNIT, item.getUnit());

        return db.update(TABLE_NAME, values, COLUMN_0_ID + " = ?", new String[] { String.valueOf(item.getId()) });
    }

	// Delete item from database
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_0_ID + " = ?", new String[] { String.valueOf(item.getId()) });
        db.close();
    }

	/**
	 * Global Database Operations
	 */

	// Getting All Items
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
				item.setUserEmail(cursor.getString(1));
                item.setDesc(cursor.getString(2));
                item.setQty(cursor.getString(3));
				item.setUnit(cursor.getString(4));

                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return itemList;
    }

	// Deleting all items
    public void deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }

    // Getting Items Count
    public int getItemsCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int itemsTotal = cursor.getCount();
        cursor.close();

        return itemsTotal;
    }
}
