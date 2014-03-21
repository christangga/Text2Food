package com.example.texttofood;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuArrayAdapter extends ArrayAdapter<Menu> {
	private final Context context;
	private final List<Menu> values;
	
	public MenuArrayAdapter(Context context, List<Menu> values) {
		super(context, R.layout.menu_list_item, values);
		this.context = context;
		this.values = values;
	}
	@Override
	public int getCount(){
		if (values == null){
			return 0;
		}
		else {
			return values.size();
		}
	}
/*
	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		return position % 2;
	}
*/
	public static class ViewHolder {
		public TextView textViewNamaMakanan;
		public TextView textViewHarga;
		public EditText editTextQuantity;
		public ImageView i;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = null;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.menu_list_item, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.textViewNamaMakanan = (TextView) rowView.findViewById(R.id.textViewNamaMakanan);
			holder.textViewHarga = (TextView) rowView.findViewById(R.id.textViewHarga);
			holder.editTextQuantity = (EditText) rowView.findViewById(R.id.editTextQuantity);
			holder.i = (ImageView) rowView.findViewById(R.id.icon);
			rowView.setTag(holder);
		}

		ViewHolder tag = (ViewHolder) rowView.getTag();

		final TextView textViewNamaMakanan = tag.textViewNamaMakanan;
		final TextView textViewHarga = tag.textViewHarga;
		final TextView editTextQuantity = tag.editTextQuantity;
		final Menu menu = values.get(position);
		
		textViewNamaMakanan.setText(menu.getNamaMakanan());
		textViewHarga.setText("Rp "+Long.toString(menu.getHarga()));
		editTextQuantity.setText(Long.toString(menu.getQuantity()));
		
		final Long posisi = (long) position;
		
    	Log.d("MenuArrayAdapter.getView","GUA ADA DI POSISI "+Long.toString((long) posisi)+" dari "+Long.toString((long) this.getCount()));
		for (int i = 0; i < values.size(); ++i){
			Log.d("MenuArrayAdapter.getView","quantity ke-"+Long.toString(i)+" = "+Long.toString(values.get(i).getQuantity()));
		}
		
		editTextQuantity.addTextChangedListener(new TextWatcher()
        {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            public void afterTextChanged(Editable editable)
            {
            	if (editable.toString().isEmpty()){
            		menu.setQuantity((long) 0);
            	}
            	else {
                	menu.setQuantity(Long.valueOf(editable.toString()));
            	}
            	
            }
        });

		return rowView;
	}
	
}
