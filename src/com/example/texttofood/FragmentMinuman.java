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

public class FragmentMinuman extends Fragment {
	private Activity parent;
	private ListView listViewMinuman;
	private Button buttonAddMinuman;
	private Button buttonDone;
	
	private Long id_restoran;
	private MenuDataSource menudatasource;
	private RestoranDataSource restorandatasource;
	private List<Menu> minumans;
	private MenuArrayAdapter adapter;
	
	public FragmentMinuman() {
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
		
		minumans = menudatasource.getAllMinuman(id_restoran);
		//set quantity to zero
		for (int i = 0; i < minumans.size(); ++i){
			minumans.get(i).setQuantity((long) 0);
			menudatasource.update(minumans.get(i));
		}
		
		//Toast.makeText(parent.getApplicationContext(), "Restoran "+parent.getTitle()+" dengan ID = "+id_restoran, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.main_menu_minuman, container, false);
		
		listViewMinuman = (ListView) v.findViewById(R.id.listViewMinuman);
		buttonAddMinuman = (Button) v.findViewById(R.id.buttonAddMinuman);
		buttonDone = (Button) v.findViewById(R.id.buttonDoneMinuman);
		
		//Toast.makeText(parent.getApplicationContext(), "VIEW Restoran "+parent.getTitle()+" dengan ID = "+id_restoran, Toast.LENGTH_SHORT).show();
		
		adapter = new MenuArrayAdapter(parent.getApplicationContext(),minumans);
		listViewMinuman.setAdapter(adapter);
		
		Log.d("FragmentMinuman.onCreateView","n minumans = "+Long.toString(minumans.size()));
		for (int i = 0; i < minumans.size(); ++i){
			Log.d("FragmentMinuman.onCreateView","quantity ke-"+Long.toString(i)+" = "+Long.toString(minumans.get(i).getQuantity()));
		}
		
		buttonAddMinuman.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				Menu menu = new Menu();
				menu.setIDRestoran(id_restoran);
				menu.setNamaMakanan("huba");
				menu.setJenis("minuman");
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
