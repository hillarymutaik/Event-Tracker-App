package com.arsari.inventoryapp;

/*
 * Arturo Santiago-Rivera (asantiago@arsari.com)
 * Programmer | Web Analytics Specialist
 * Refactor of SNHU Final Project
 * CS-360-X6386 Mobile Architect & Programming 21EW6
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {

    Button RegisterButton, CancelButton;
    EditText NameHolder, PhoneNumberHolder, EmailHolder, PasswordHolder;
    Boolean EmptyHolder;
    SQLiteDatabase db;
    UsersSQLiteHandler handler;
    String F_Result = "Not_Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        NameHolder = findViewById(R.id.editTextPersonName);
        PhoneNumberHolder = findViewById(R.id.editTextPhoneNumber);
        EmailHolder = findViewById(R.id.editTextEmailAddress);
        PasswordHolder = findViewById(R.id.editTextPassword);
        RegisterButton = findViewById(R.id.regSignupButton);
        CancelButton = findViewById(R.id.regCancelButton);
        handler = new UsersSQLiteHandler(this);

        // Adding click listener to register forgotPasswordButton
        RegisterButton.setOnClickListener(view -> {
			String message = CheckEditTextNotEmpty();

			if (!EmptyHolder) {
				// Check if email already exists in database
				CheckEmailAlreadyExists();
				// Empty editText fields after done inserting in database
				EmptyEditTextAfterDataInsert();
			} else {
				// Display toast message if any field is empty and focus the field
				Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			}
		});

		// Adding click listener to addCancelButton
		CancelButton.setOnClickListener(view -> {
			// Going back to LoginActivity after cancel Register
			startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
			this.finish();
		});

	}

    // Register new user into database
    public void InsertUserIntoDatabase(){
        	String name = NameHolder.getText().toString().trim();
        	String phone = PhoneNumberHolder.getText().toString().trim();
			String email = EmailHolder.getText().toString().trim();
			String pass = PasswordHolder.getText().toString().trim();

			User user = new User(name, phone, email, pass);
			handler.createUser(user);

            // Printing toast message after done inserting.
            Toast.makeText(RegisterActivity.this,"User Registered Successfully", Toast.LENGTH_LONG).show();

            // Going back to LoginActivity after register success message
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            this.finish();
    }

	// Checking item description is not empty
	public String CheckEditTextNotEmpty() {
		// Getting value from fields and storing into string variable
		String message = "";
		String name = NameHolder.getText().toString().trim();
		String phone = PhoneNumberHolder.getText().toString().trim();
		String email = EmailHolder.getText().toString().trim();
		String pass = PasswordHolder.getText().toString().trim();

		if (name.isEmpty()) {
			NameHolder.requestFocus();
			EmptyHolder = true;
			message = "User Name is Empty";
		} else if (phone.isEmpty()){
			PhoneNumberHolder.requestFocus();
			EmptyHolder = true;
			message = "User Phone is Empty";
		} else if (email.isEmpty()){
			EmailHolder.requestFocus();
			EmptyHolder = true;
			message = "User Email is Empty";
		} else if (pass.isEmpty()){
			PasswordHolder.requestFocus();
			EmptyHolder = true;
			message = "User Password is Empty";
		} else {
			EmptyHolder = false;
		}
		return message;
	}

    // Check if user email already exists in database
    public void CheckEmailAlreadyExists(){
		String email = EmailHolder.getText().toString().trim();
		db = handler.getWritableDatabase();

        // Adding search email query to cursor
        Cursor cursor = db.query(UsersSQLiteHandler.TABLE_NAME, null, " " + UsersSQLiteHandler.COLUMN_3_EMAIL + "=?", new String[]{email}, null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                // If email exists then set result variable value as Email Found
                F_Result = "Email Found";
                // Closing cursor.
                cursor.close();
            }
        }
        handler.close();

        // Calling method to check final result and insert data into SQLite database
        CheckFinalCredentials();
    }

    // Check login credentials are correct
    public void CheckFinalCredentials(){
        // Checking whether email is already in database
        if(F_Result.equalsIgnoreCase("Email Found"))
        {
            // If email is exists then toast msg will display
            Toast.makeText(RegisterActivity.this,"Email Already Exists",Toast.LENGTH_LONG).show();
        }
        else {
            // If email already doesn't exists then user registration details will entered to SQLite database
            InsertUserIntoDatabase();
        }
        F_Result = "Not_Found" ;
    }

    // Empty edittext after done inserting in database
    public void EmptyEditTextAfterDataInsert(){
        NameHolder.getText().clear();
        PhoneNumberHolder.getText().clear();
        EmailHolder.getText().clear();
        PasswordHolder.getText().clear();
    }

}
