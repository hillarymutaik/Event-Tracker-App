package com.eventtracker.eventtrackerapp;

import androidx.appcompat.app.AlertDialog;

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
