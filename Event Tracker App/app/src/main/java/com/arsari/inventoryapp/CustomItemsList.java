package com.arsari.inventoryapp;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Custom Items List Activity java code.
 * <p>
 * The CustomItemsList class include the funcionality to populate
 * the items from the database into the ItemListActivity. Add the
 * row funcionality to edit and delete an item, and to change item
 * quantity. It also call and build the edit item alert dialog.
 * <p>
 * This class generate the row in the ItemsListActivity.
 *
 * @author	Arturo Santiago-Rivera <i>asantiago@arsari.com</i>
 * @course	CS-360-X6386 Mobile Architect & Programming 21EW6
 * @college	Southern New Hampshire University
 */
public class CustomItemsList extends BaseAdapter {

    private final Activity context;
    private PopupWindow popwindow;
    ArrayList<Item> items;
    ItemsSQLiteHandler db;

	public CustomItemsList(Activity context, ArrayList<Item> items, ItemsSQLiteHandler db) {
        this.context = context;
        this.items = items;
        this.db = db;
    }

    public static class ViewHolder {
        TextView textViewItemId;
		TextView textViewUserEmail;
        TextView textViewItemDesc;
        TextView textViewItemQty;
		TextView textViewItemUnit;
        ImageButton editBtn;
        ImageButton deleteBtn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;

		if (convertView == null) {
			vh = new ViewHolder();
			row = inflater.inflate(R.layout.item_row_template, null, true);

			vh.editBtn = row.findViewById(R.id.editButton);
			vh.textViewItemId = row.findViewById(R.id.textViewItemId);
			vh.textViewUserEmail = row.findViewById(R.id.textViewUserEmail);
			vh.textViewItemDesc = row.findViewById(R.id.textViewItemDesc);
			vh.textViewItemQty = row.findViewById(R.id.textViewItemQty);
			vh.textViewItemUnit = row.findViewById(R.id.textViewItemUnit);
			vh.deleteBtn = row.findViewById(R.id.deleteButton);

			row.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.textViewItemId.setText("" + items.get(position).getId());
		vh.textViewUserEmail.setText(items.get(position).getUserEmail());
		vh.textViewItemDesc.setText(items.get(position).getDesc());
		vh.textViewItemQty.setText(items.get(position).getQty());
		vh.textViewItemUnit.setText(items.get(position).getUnit());

		// Check is the cell value is zero to change color and send SMS
		String value = vh.textViewItemQty.getText().toString().trim();
		if (value.equals("0")) {
			// Change background color and text color of item qty cell if value is zero
			vh.textViewItemQty.setBackgroundColor(Color.RED);
			vh.textViewItemQty.setTextColor(Color.WHITE);
			ItemsListActivity.SendSMSMessage(context.getApplicationContext());
		} else {
			// Change background color and text color of item qty cell to default
			vh.textViewItemQty.setBackgroundColor(Color.parseColor("#E6E6E6"));
			vh.textViewItemQty.setTextColor(Color.BLACK);
		}

		final int positionPopup = position;

		vh.editBtn.setOnClickListener(view -> editPopup(positionPopup));

		vh.deleteBtn.setOnClickListener(view -> {
			//Integer index = (Integer) view.getTag();
			db.deleteItem(items.get(positionPopup));

			//items.remove(index.intValue());
			items = (ArrayList<Item>) db.getAllItems();
			notifyDataSetChanged();

			Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();

			int itemsCount = db.getItemsCount();
			TextView TotalItems = context.findViewById(R.id.textViewTotalItemsCount);
			TotalItems.setText(String.valueOf(itemsCount));
		});

        return  row;
    }

    public Object getItem(int position) {
        return position;
    }

	public long getItemId(int position) {
		return position;
	}

	public int getCount() {
        return items.size();
    }

	public void editPopup(final int positionPopup) {
		LayoutInflater inflater = context.getLayoutInflater();
		View layout = inflater.inflate(R.layout.edit_item_popup, context.findViewById(R.id.popup_element));

		popwindow = new PopupWindow(layout, 800, 1000, true);
		popwindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

		final EditText editItemDesc = layout.findViewById(R.id.editTextItemDescriptionPopup);
		final EditText editItemQty = layout.findViewById(R.id.editTextItemQtyPopup);
		final EditText editItemUnit = layout.findViewById(R.id.editTextItemUnitPopup);

		editItemDesc.setText(items.get(positionPopup).getDesc());
		editItemQty.setText(items.get(positionPopup).getQty());
		editItemUnit.setText(items.get(positionPopup).getUnit());

		Button save = layout.findViewById(R.id.editSaveButton);
		Button cancel = layout.findViewById(R.id.editCancelButton);

		save.setOnClickListener(view -> {
			String itemDesc = editItemDesc.getText().toString();
			String itemQty = editItemQty.getText().toString();
			String itemUnit = editItemUnit.getText().toString();

			Item item = items.get(positionPopup);
			item.setDesc(itemDesc);
			item.setQty(itemQty);
			item.setUnit(itemUnit);

			db.updateItem(item);
			items = (ArrayList<Item>) db.getAllItems();
			notifyDataSetChanged();

			Toast.makeText(context, "Item Updated", Toast.LENGTH_SHORT).show();

			popwindow.dismiss();
		});

		cancel.setOnClickListener(view -> {
			Toast.makeText(context, "Action Canceled", Toast.LENGTH_SHORT).show();
			popwindow.dismiss();
		});
	}

}
