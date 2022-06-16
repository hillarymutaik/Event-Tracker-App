package com.arsari.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Add Item Activity java code.
 * <p>
 * The AddItemActivity class include the funcionality for the add item activity
 * layout. It add funcionality to the buttons in the layout, insert a new item
 * into the database, and verify any field is not empty.
 * <p>
 * This class is called from by the ItemsListActivity.
 *
 * @author	Arturo Santiago-Rivera <i>asantiago@arsari.com</i>
 * @course	CS-360-X6386 Mobile Architect & Programming 21EW6
 * @college	Southern New Hampshire University
 */
public class AddItemActivity extends AppCompatActivity {

    String EmailHolder, DescHolder, QtyHolder, UnitHolder;
    TextView UserEmail;
    ImageButton IncreaseQty, DecreaseQty;
    EditText ItemDescValue, ItemQtyValue, ItemUnitValue;
    Button CancelButton, AddItemButton;
	Boolean EmptyHolder;
    ItemsSQLiteHandler db;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        // Initiate buttons, textViews, and editText variables
        UserEmail = findViewById(R.id.textViewLoggedUser);
        ItemDescValue = findViewById(R.id.editTextItemDescription);
        ItemUnitValue = findViewById(R.id.editTextItemUnit);
        IncreaseQty = findViewById(R.id.itemQtyIncrease);
        DecreaseQty = findViewById(R.id.itemQtyDecrease);
        ItemQtyValue = findViewById(R.id.editTextItemQuantity);
        CancelButton = findViewById(R.id.addCancelButton);
        AddItemButton = findViewById(R.id.addItemButton);
		db = new ItemsSQLiteHandler(this);

        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());

        // Receiving user email send by ItemsListActivity
        EmailHolder = intent.get().getStringExtra(ItemsListActivity.UserEmail);

        // Setting user email on textViewLoggedUser
        UserEmail.setText(getString(R.string.logged_user, EmailHolder));

        // Adding click listener to itemQtyIncrease forgotPasswordButton
        IncreaseQty.setOnClickListener(view -> {
            int input = 0, total;

            String value = ItemQtyValue.getText().toString().trim();

            if (!value.isEmpty()) {
                input = Integer.parseInt(value);
            }

            total = input + 1;
            ItemQtyValue.setText(String.valueOf(total));
        });

        // Adding click listener to itemQtyDecrease forgotPasswordButton
        DecreaseQty.setOnClickListener(view -> {
            int input, total;

            String qty = ItemQtyValue.getText().toString().trim();

            if (qty.equals("0")) {
                Toast.makeText(this, "Item Quantity is Zero", Toast.LENGTH_LONG).show();
            } else {
                input = Integer.parseInt(qty);
                total = input - 1;
                ItemQtyValue.setText(String.valueOf(total));
            }
        });

        // Adding click listener to addCancelButton
        CancelButton.setOnClickListener(view -> {
            // Going back to ItemsListActivity after cancel adding item
			Intent add = new Intent();
			setResult(0, add);
            this.finish();
        });

        // Adding click listener to addItemButton and pass data to ItemsListActivity
        AddItemButton.setOnClickListener(view -> InsertItemIntoDatabase());
    }

    // Insert item data into database and send data to ItemsListActivity
    public void InsertItemIntoDatabase() {
		String message = CheckEditTextNotEmpty();

        if (!EmptyHolder) {
       	  	String email = EmailHolder;
        	String desc = DescHolder;
        	String qty = QtyHolder;
        	String unit = UnitHolder;

        	Item item = new Item(email, desc, qty, unit);
        	db.createItem(item);

            // Display toast message after insert in table
            Toast.makeText(this,"Item Added Successfully", Toast.LENGTH_LONG).show();

            // Close AddItemActivity
			Intent add = new Intent();
			setResult(RESULT_OK, add);
            this.finish();
        } else {
            // Display toast message if item description is empty and focus the field
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    // Checking item description is not empty
    public String CheckEditTextNotEmpty() {
        // Getting value from fields and storing into string variable
		String message = "";
        DescHolder = ItemDescValue.getText().toString().trim();
        UnitHolder = ItemUnitValue.getText().toString().trim();
		QtyHolder = ItemQtyValue.getText().toString().trim();

        if (DescHolder.isEmpty()) {
			ItemDescValue.requestFocus();
            EmptyHolder = true;
            message = "Item Description is Empty";
        } else if (UnitHolder.isEmpty()){
			ItemUnitValue.requestFocus();
			EmptyHolder = true;
			message = "Item Unit is Empty";
        } else {
        	EmptyHolder = false;
		}
        return message;
    }

}
