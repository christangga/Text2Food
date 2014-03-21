package com.example.texttofood;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class FragmentMakanan extends Fragment {
	private Activity parent;
	private ListView listViewMakanan;
	private Button buttonAddMakanan;
	private Button buttonDone;
	
	private Long id_restoran;
	private MenuDataSource menudatasource;
	private RestoranDataSource restorandatasource;
	private List<Menu> makanans;
	private MenuArrayAdapter adapter;
	
	public FragmentMakanan() {
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
		
		makanans = menudatasource.getAllMakanan(id_restoran);
		//set quantity to zero
		for (int i = 0; i < makanans.size(); ++i){
			makanans.get(i).setQuantity((long) 0);
			menudatasource.update(makanans.get(i));
		}
		
		//Toast.makeText(parent.getApplicationContext(), "Restoran "+parent.getTitle()+" dengan ID = "+id_restoran, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.main_menu_makanan, container, false);
		
		listViewMakanan = (ListView) v.findViewById(R.id.listViewMakanan);
		buttonAddMakanan = (Button) v.findViewById(R.id.buttonAddMakanan);
		buttonDone = (Button) v.findViewById(R.id.buttonDoneMakanan);
		
		//Toast.makeText(parent.getApplicationContext(), "VIEW Restoran "+parent.getTitle()+" dengan ID = "+id_restoran, Toast.LENGTH_SHORT).show();
		
		adapter = new MenuArrayAdapter(parent.getApplicationContext(),makanans);
		listViewMakanan.setAdapter(adapter);
		
		Log.d("FragmentMakanan.onCreateView","n makanans = "+Long.toString(makanans.size()));
		for (int i = 0; i < makanans.size(); ++i){
			Log.d("FragmentMakanan.onCreateView","quantity ke-"+Long.toString(i)+" = "+Long.toString(makanans.get(i).getQuantity()));
		}
		
		buttonAddMakanan.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				Menu menu = new Menu();
				menu.setIDRestoran(id_restoran);
				menu.setNamaMakanan("huba");
				menu.setJenis("makanan");
				menu.setHarga(17000);
				menudatasource.add(menu);
				adapter.add(menu);
				adapter.notifyDataSetChanged();
			}
		});
		
		buttonDone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				for (int i = 0; i < adapter.getCount(); ++i){
					menudatasource.update(adapter.getItem(i));
					//Menu haha = menudatasource.getMenu(adapter.getItem(i).getID());
					//Toast.makeText(parent.getApplicationContext(), "udah keupdate coy. id = " + haha.getID() + ", quantity = " + haha.getQuantity(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return v;
	}
	/*
	@Override
	public void onDestroy() {
		menudatasource.close();
		restorandatasource.close();
	}
	*/

}
