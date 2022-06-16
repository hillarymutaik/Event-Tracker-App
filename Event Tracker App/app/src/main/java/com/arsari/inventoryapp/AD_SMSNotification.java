package com.arsari.inventoryapp;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

/**
 * SMS Notification Alert Dialog java code.
 * <p>
 * The AD_SMSNotification class include the funcionality to build the
 * alert dialog to enable or disable the SMS notifications when an item
 * quantity id zero.
 * <p>
 * This is class is called from the ItemsListActivity class.
 *
 * @author	Arturo Santiago-Rivera <i>asantiago@arsari.com</i>
 * @course	CS-360-X6386 Mobile Architect & Programming 21EW6
 * @college	Southern New Hampshire University
 */
public class AD_SMSNotification {

    public static AlertDialog doubleButton(final ItemsListActivity context){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.ad_sms_title)
                .setIcon(R.drawable.sms_notification)
                .setCancelable(false)
                .setMessage(R.string.ad_sms_msg)
                .setPositiveButton(R.string.ad_sms_enable_button, (dialog, arg1) -> {
					Toast.makeText(context, "SMS Alerts Enable", Toast.LENGTH_LONG).show();
					ItemsListActivity.AllowSendSMS();
					dialog.cancel();
				})
                .setNegativeButton(R.string.ad_sms_disable_button, (dialog, arg1) -> {
					Toast.makeText(context, "SMS Alerts Disable", Toast.LENGTH_LONG).show();
					ItemsListActivity.DenySendSMS();
					dialog.cancel();
				});

        // Create the AlertDialog object and return it
		return builder.create();
    }
}
