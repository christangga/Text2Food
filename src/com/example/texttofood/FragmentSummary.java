package com.example.texttofood;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class FragmentSummary extends Fragment {
	private Activity parent;
	private EditText editTextOrder;
	private EditText editTextPrice;
	private EditText editTextWhere;
	private CheckBox checkBoxNotes;
	private EditText editTextNotes;
	private EditText editTextMessage;
	private Button buttonOrder;
	private Button buttonRefresh;
	
	private Long id_restoran;
	private MenuDataSource menudatasource;
	private RestoranDataSource restorandatasource;
	
	private List<Menu> allmenus, menus;
	private Long harga;
	
	public FragmentSummary() {
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = getActivity();
		
		menudatasource = new MenuDataSource(parent.getApplicationContext());
		menudatasource.open();
		restorandatasource = new RestoranDataSource(parent.getApplicationContext());
		restorandatasource.open();
		
		id_restoran = restorandatasource.getRestoranID(parent.getTitle().toString());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.main_menu_summary, container, false);
		
		editTextOrder = (EditText) v.findViewById(R.id.editTextOrder);
		editTextPrice = (EditText) v.findViewById(R.id.editTextPrice);
		editTextWhere = (EditText) v.findViewById(R.id.editTextWhere);
		checkBoxNotes = (CheckBox) v.findViewById(R.id.checkBoxNotes);
		editTextNotes = (EditText) v.findViewById(R.id.editTextNotes);
		editTextMessage = (EditText) v.findViewById(R.id.editTextMessage);
		buttonOrder = (Button) v.findViewById(R.id.buttonOrder);
		buttonRefresh = (Button) v.findViewById(R.id.buttonRefresh);
		
		checkBoxNotes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
				editTextNotes.setEnabled(checked);
			}
		});
		
		buttonRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				allmenus = menudatasource.getAllMenu(id_restoran);
				menus = new ArrayList<Menu>();
				for (int i = 0; i < allmenus.size(); ++i){
					if (allmenus.get(i).getQuantity() != 0){
						menus.add(allmenus.get(i));
					}
				}
				
				//Toast.makeText(parent.getApplicationContext(), "total menu = "+menus.size()+" dari "+allmenus.size(), Toast.LENGTH_SHORT).show();
				
				/* set Order */
				if (menus.size() == 0){
					editTextOrder.setText("Anda belum memesan!");
					editTextMessage.setText("");
				}
				else {
					editTextOrder.setText("");
					editTextOrder.append("Makanan:");
					for (int i = 0; i < menus.size(); ++i){
						Menu menu = menus.get(i);
						if (menu.getJenis().contentEquals("makanan")){
							editTextOrder.append("\n");
							editTextOrder.append(Long.toString(menu.getQuantity()) + " buah " + menu.getNamaMakanan());
						}
					}
					editTextOrder.append("\n\nMinuman:");
					for (int i = 0; i < menus.size(); ++i){
						Menu menu = menus.get(i);
						if (menu.getJenis().contentEquals("minuman")){
							editTextOrder.append("\n");
							editTextOrder.append(Long.toString(menu.getQuantity()) + " buah " + menu.getNamaMakanan());
						}
					}
					
					//menghitung harga
					harga = (long) 0;
					for (int i = 0; i < menus.size(); ++i){
						Menu menu = menus.get(i);
						harga += menu.getHarga() * menu.getQuantity();
					}
					editTextPrice.setText(harga.toString());
					
					//compose message
					editTextMessage.setText("");
					//salam pembuka
					editTextMessage.append("Mas, mau pesan makanan:\n");
					//list makanan
					for (int i = 0; i < menus.size(); ++i){
						Menu menu = menus.get(i);
						if (menu.getJenis().contentEquals("makanan")){
							editTextMessage.append(Long.toString(menu.getQuantity()) + " buah " + menu.getNamaMakanan() + ",\n");
						}
					}
					for (int i = 0; i < menus.size(); ++i){
						Menu menu = menus.get(i);
						if (menu.getJenis().contentEquals("minuman")){
							editTextMessage.append(Long.toString(menu.getQuantity()) + " buah " + menu.getNamaMakanan() + ",\n");
						}
					}
					//lokasi antar
					editTextMessage.append("tolong diantar ke:\n");
					editTextMessage.append(editTextWhere.getText());
					editTextMessage.append("\n");
					//catatan tambahan
					if (checkBoxNotes.isChecked()){
						editTextMessage.append("N.B.:\n");
						editTextMessage.append(editTextNotes.getText());
						editTextMessage.append("\n");
					}
					//salam penutup
					editTextMessage.append("Terima kasih");
				}		
			}
		});
		
		buttonOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (editTextMessage.getText().length() == 0){
					Toast.makeText(parent.getApplicationContext(), "Anda belum memesan!", Toast.LENGTH_SHORT).show();
				}
				if (editTextWhere.getText().length() == 0){
					Toast.makeText(parent.getApplicationContext(), "Anda belum mengisi lokasi antar!", Toast.LENGTH_SHORT).show();
				}
				else {
					String number, message;
					
					//cari nomornya dulu
					Restoran restoran = restorandatasource.getRestoran(parent.getTitle().toString());
					number = restoran.getNomorKontak();
					
					//susun messagenya
					message = editTextMessage.getText().toString();

					//kirim!
				    android.telephony.SmsManager sms = android.telephony.SmsManager.getDefault();
				    sms.sendTextMessage(number, null, message, null, null);
				    
				    //tampilin pesan lah biar enak
				    Toast.makeText(parent.getApplicationContext(), "pesanan Anda akan segera diantar! Terima Kasih", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return v;
	}

}