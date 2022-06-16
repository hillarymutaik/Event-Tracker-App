package com.arsari.inventoryapp;

import androidx.appcompat.app.AlertDialog;

/**
 * Delete Items Alert Dialog java code.
 * <p>
 * The AD_DeleteItems class include the funcionality to build the
 * alert dialog to delete all the items records in the database.
 * <p>
 * This is class is called from the ItemsListActivity class.
 *
 * @author	Arturo Santiago-Rivera <i>asantiago@arsari.com</i>
 * @course	CS-360-X6386 Mobile Architect & Programming 21EW6
 * @college	Southern New Hampshire University
 */
public class AD_DeleteItems {

    public static AlertDialog doubleButton(final ItemsListActivity context){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.ad_delete_title)
                .setIcon(R.drawable.delete_all_items)
                .setCancelable(false)
                .setMessage(R.string.ad_delete_msg)
                .setPositiveButton(R.string.ad_delete_dialog_yes_button, (dialog, arg1) -> {
					ItemsListActivity.YesDeleteItems();
					dialog.cancel();
				})
                .setNegativeButton(R.string.ad_delete_dialog_no_button, (dialog, arg1) -> {
					ItemsListActivity.NoDeleteItems();
					dialog.cancel();
				});

        // Create the AlertDialog object and return it
		return builder.create();
    }
}
