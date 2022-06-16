package com.arsari.inventoryapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Item List Activity java code.
 * <p>
 * The ItemsListActivity class include the funcionality for the items activity
 * layout. It add funcionality to the buttons in the layout, call the SMS
 * notification AD, call the add item activity, call the delete items AD, look
 * for the total of items in the database, and allow the user logout
 * <p>
 * This is the second class executed after user is authenticated.
 *
 * @author	Arturo Santiago-Rivera <i>asantiago@arsari.com</i>
 * @course	CS-360-X6386 Mobile Architect & Programming 21EW6
 * @college	Southern New Hampshire University
 */
public class ItemsListActivity extends AppCompatActivity {

    TextView UserName, TotalItems;
    ImageButton AddItemButton, SmsButton, DelAllItemsButton;
	ListView ItemsListView;
	ItemsSQLiteHandler db;
    static String NameHolder, EmailHolder, PhoneNumHolder;
    AlertDialog AlertDialog = null;
	ArrayList<Item> items;
	CustomItemsList customItemsList;
	int itemsCount;

    public static final String UserEmail = "";
    private static final int USER_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static boolean smsAuthorized = false;
	private static boolean deleteItems = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        // Initiate buttons, textViews, and editText variables
        UserName = findViewById(R.id.textViewUserNameLabel);
        TotalItems = findViewById(R.id.textViewTotalItemsCount);
        AddItemButton = findViewById(R.id.addItemButton);
        SmsButton = findViewById(R.id.smsNotification);
        DelAllItemsButton = findViewById(R.id.deleteAllItemsButton);
		ItemsListView = findViewById(R.id.bodyListView);
        db = new ItemsSQLiteHandler(this);

        // Receiving user name and user email send by LoginActivity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            NameHolder = bundle.getString("user_name");
            EmailHolder = bundle.getString("user_email");
            PhoneNumHolder = bundle.getString("user_phone");
            // Setting user name in textViewUserNameLabel
            UserName.setText(getString(R.string.salutation, NameHolder.toUpperCase()));
        }

		items = (ArrayList<Item>) db.getAllItems();

		itemsCount = db.getItemsCount();

		if (itemsCount > 0) {
			customItemsList = new CustomItemsList(this, items, db);
			ItemsListView.setAdapter(customItemsList);
		} else {
			Toast.makeText(this, "Database is Empty", Toast.LENGTH_LONG).show();
		}

		TotalItems.setText(String.valueOf(itemsCount));

        // Adding click listener to addItemButton
        AddItemButton.setOnClickListener(view -> {
            // Opening new AddItemActivity using intent on forgotPasswordButton click.
            Intent add = new Intent(this, AddItemActivity.class);
            add.putExtra(UserEmail, EmailHolder);
            startActivityForResult(add, 1);
        });

        // Adding click listener to smsNotification
        SmsButton.setOnClickListener(view -> {
            // Request sms permission for the device
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SEND_SMS)) {
                    Toast.makeText(this,"Device SMS Permission is Needed", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[] {Manifest.permission.SEND_SMS},
                            USER_PERMISSIONS_REQUEST_SEND_SMS);
                }
            } else {
                Toast.makeText(this,"Device SMS Permission is Allowed", Toast.LENGTH_LONG).show();
            }
            // Open SMS Alert Dialog
            AlertDialog = AD_SMSNotification.doubleButton(this);
            AlertDialog.show();
        });

        DelAllItemsButton.setOnClickListener(view -> {
			itemsCount = db.getItemsCount();

			if (itemsCount > 0) {
				// Open Delete Alert Dialog
				AlertDialog = AD_DeleteItems.doubleButton(this);
				AlertDialog.show();

				AlertDialog.setCancelable(true);
					AlertDialog.setOnCancelListener(dialog -> DeleteAllItems());
			} else {
				Toast.makeText(this, "Database is Empty", Toast.LENGTH_LONG).show();
			}
		});
    }

    // Setting sing out menu item in AppBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_items_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signoutMenuItem) {
            // End ItemsListActivity on menu item click.
            db.close();
            super.finish();
            Toast.makeText(this,"Log Out Successful", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				itemsCount = db.getItemsCount();
				TotalItems.setText(String.valueOf(itemsCount));

				if(customItemsList == null)	{
					customItemsList = new CustomItemsList(this, items, db);
					ItemsListView.setAdapter(customItemsList);
				}

				customItemsList.items = (ArrayList<Item>) db.getAllItems();
				((BaseAdapter)ItemsListView.getAdapter()).notifyDataSetChanged();
			} else {
				Toast.makeText(this, "Action Canceled", Toast.LENGTH_SHORT).show();
			}
		}
	}

	// Receive and evaluate user response from AlertDialog to delete items
	public static void YesDeleteItems() {
    	deleteItems = true;
	}

	public static void NoDeleteItems() {
		deleteItems = false;
	}

	public void DeleteAllItems() {
		if (deleteItems) {
			db.deleteAllItems();
			Toast.makeText(this, "All Items were Deleted", Toast.LENGTH_SHORT).show();
			TotalItems.setText("0");

			if (customItemsList == null) {
				customItemsList = new CustomItemsList(this, items, db);
				ItemsListView.setAdapter(customItemsList);
			}

			customItemsList.items = (ArrayList<Item>) db.getAllItems();
			((BaseAdapter) ItemsListView.getAdapter()).notifyDataSetChanged();
		}
	}

    // Receive and evaluate user response from AlertDialog to send SMS
    public static void AllowSendSMS() {
        smsAuthorized = true;
    }

    public static void DenySendSMS() {
        smsAuthorized = false;
    }

    public static void SendSMSMessage(Context context) {
        String phoneNo = PhoneNumHolder;
        String smsMsg = "Please, you have items with zero value in your Inventory App.";

        // Evaluate AlertDialog permission to send SMS and ItemQtyValue value
        if (smsAuthorized) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, smsMsg, null, null);
                Toast.makeText(context, "SMS Sent", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(context, "Device Permission Denied", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(context, "App SMS Alert Disable", Toast.LENGTH_LONG).show();
        }
	}
}
