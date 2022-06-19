package com.java2blog.sqlitedatabasecrudexample;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "countryData";

    // Country table name
    private static final String TABLE_COUNTRY= "Country";

    // Country Table Columns names
    private static final String KEY_ID = "id";
    private static final String COUNTRY_NAME = "CountryName";
    private static final String POPULATION = "Population";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COUNTRY_TABLE = "CREATE TABLE " + TABLE_COUNTRY + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + COUNTRY_NAME + " TEXT,"
                + COUNTRY_NAME + " LONG" + ")";
        db.execSQL(CREATE_COUNTRY_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new country
    void addCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COUNTRY_NAME, country.getCountryName()); // Country Name
        values.put(POPULATION, country.getPopulation()); // Country Population

        // Inserting Row
        db.insert(TABLE_COUNTRY, null, values);
        db.close(); // Closing database connection
    }

    // Getting single country
    Country getCountry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNTRY, new String[] { KEY_ID,
                        COUNTRY_NAME, POPULATION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Country country = new Country(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getLong(2));
        // return country
        return country;
    }

    // Getting All Countries
    public List<Country> getAllCountries() {
        List<Country> countryList = new ArrayList<Country>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COUNTRY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(Integer.parseInt(cursor.getString(0)));
                country.setCountryName(cursor.getString(1));
                country.setPopulation(cursor.getLong(2));
                // Adding country to list
                countryList.add(country);
            } while (cursor.moveToNext());
        }

        // return country list
        return countryList;
    }

    // Updating single country
    public int updateCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COUNTRY_NAME, country.getCountryName());
        values.put(POPULATION, country.getPopulation());

        // updating row
        return db.update(TABLE_COUNTRY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(country.getId()) });
    }

    // Deleting single country
    public void deleteCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTRY, KEY_ID + " = ?",
                new String[] { String.valueOf(country.getId()) });
        db.close();
    }

    // Deleting all countries
    public void deleteAllCountries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTRY,null,null);
        db.close();
    }

    // Getting countries Count
    public int getCountriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_COUNTRY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}