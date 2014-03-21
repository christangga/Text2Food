package com.example.texttofood;

import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends Activity {
	private RestoranDataSource datasource;
	private MenuDataSource menudatasource;
	private ArrayAdapter<Restoran> adapter;

	private ListView listViewRestoran;
	private PopupMenu popupMenuRestoran;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		datasource = new RestoranDataSource(this);
		datasource.open();
		menudatasource = new MenuDataSource(this);
		menudatasource.open();
		
		//Add Kabita
		addKabita();
		
		listViewRestoran = (ListView) findViewById(R.id.listViewRestoran);
		
		List<Restoran> restorans = datasource.getAllRestoran();
		
		adapter = new ArrayAdapter<Restoran>(this,R.layout.restoran_list_view_item,restorans);
		listViewRestoran.setAdapter(adapter);
		
		listViewRestoran.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				Intent i = new Intent(getApplicationContext(), MainMenu.class);
				i.putExtra("nama_restoran", parent.getItemAtPosition(position).toString());
				startActivity(i);
			}
		});
		
		popupMenuRestoran = new PopupMenu(this, findViewById(R.id.listViewRestoran));
        popupMenuRestoran.getMenu().add(Menu.NONE, 1, Menu.NONE, "Edit");
        popupMenuRestoran.getMenu().add(Menu.NONE, 2, Menu.NONE, "Delete");
        popupMenuRestoran.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				switch (arg0.getItemId()) {
				case 1:
					Toast.makeText(getApplicationContext(), "yang diklik edit!", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "yang diklik delete!", Toast.LENGTH_SHORT).show();
					break;
				}
				return false;
			}
		});
		
        listViewRestoran.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        	@Override
        	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        		long id_restoran = adapter.getItem(position).getID();
        		showOptionsDialog(id_restoran);
        		//popupMenuRestoran.show();
        		return false;
        	}
		});
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		// Collect data from the intent and use it
		if (requestCode == 1){
			if (data != null){
				Bundle extras = data.getExtras();
				String nama_restoran = extras.getString("nama_restoran");
				String nomor_kontak = extras.getString("nomor_kontak");
				if (datasource.getRestoranID(nama_restoran) != -1){
					Toast.makeText(getApplicationContext(), "restoran "+nama_restoran+" sudah ada!", Toast.LENGTH_SHORT).show();
				}
				else {
					Restoran newRestoran = new Restoran();
					newRestoran.setNamaRestoran(nama_restoran);
					newRestoran.setNomorKontak(nomor_kontak);
					datasource.add(newRestoran);
					adapter.add(newRestoran);
					adapter.notifyDataSetChanged();
					Toast.makeText(getApplicationContext(), "restoran "+nama_restoran+" telah ditambahkan!", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.add_restoran:
			//startActivityForResult(new Intent(getApplicationContext(), AddRestoran.class), 1);
			showAddDialog();
			break;
		case R.id.action_settings:
			break;
		case R.id.action_sort_alpha:
			adapter.sort(new Comparator<Restoran>() {
				@Override
				public int compare (Restoran a, Restoran b) {
					return a.getNamaRestoran().compareToIgnoreCase(b.getNamaRestoran());
				}
			});
			adapter.notifyDataSetChanged();
			break;
		case R.id.action_sort_time:
			adapter.sort(new Comparator<Restoran>() {
				@Override
				public int compare (Restoran a, Restoran b) {
					return a.getID() < b.getID() ? -1 : a.getID() == b.getID() ? 0 : 1;
				}
			});
			adapter.notifyDataSetChanged();
			break;
		}

		return true;
	}

	private void addKabita() {
		Long _id = datasource.getRestoranID("Pondok Kabita");
		if (_id == -1){
			Restoran kabita = new Restoran();
			kabita.setNamaRestoran("Pondok Kabita");
			kabita.setNomorKontak("08978670424"); // ini nomor riady
			datasource.add(kabita);
			_id = kabita.getID();
			
			Toast.makeText(getApplicationContext(), "ID = "+Long.toString(_id), Toast.LENGTH_SHORT).show();
			//add menu!
			com.example.texttofood.Menu menu = new com.example.texttofood.Menu();
			
			menu.setIDRestoran(_id);
			
			menu.setNamaMakanan("Ayam Crispy Manis Pedas");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Crispy Manis Pedas");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Crispy Asam Manis Pedas");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Crispy Manis");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Crispy Saos Tiram");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Crispy Teriyaki");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Crispy Saos Mayones");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Crispy Kinca");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Bakar Pedas Manis");
			menu.setJenis("makanan");
			menu.setHarga(13500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Bakar Pedas");
			menu.setJenis("makanan");
			menu.setHarga(13500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Goreng Kabita");
			menu.setJenis("makanan");
			menu.setHarga(13500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Black Sweet Kabita");
			menu.setJenis("makanan");
			menu.setHarga(13500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Ayam Bakar Penyet");
			menu.setJenis("makanan");
			menu.setHarga(13500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Nasi Goreng Mentega Keju");
			menu.setJenis("makanan");
			menu.setHarga(13500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Nasi Goreng Barbeque");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Nasi Goreng Katsu");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Nasi Goreng Hainan");
			menu.setJenis("makanan");
			menu.setHarga(13500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Nasi Goreng Telor Sosis");
			menu.setJenis("makanan");
			menu.setHarga(11500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Nasi Goreng Telor Kornet");
			menu.setJenis("makanan");
			menu.setHarga(11500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Nasi Goreng Telor Sosis Ayam");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Mie Tek-Tek");
			menu.setJenis("makanan");
			menu.setHarga(9500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Mie Goreng Telor Sosis Ayam");
			menu.setJenis("makanan");
			menu.setHarga(11500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Mie Goreng Telor Sosis");
			menu.setJenis("makanan");
			menu.setHarga(10500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Kwetiau Goreng Telor Sosis Ayam");
			menu.setJenis("makanan");
			menu.setHarga(11500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Kwetiau Goreng Telor Sosis");
			menu.setJenis("makanan");
			menu.setHarga(10500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Nasi Dadar Kornet(Darko)");
			menu.setJenis("makanan");
			menu.setHarga(9500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Nasi Orak-Arik Telor Kornet");
			menu.setJenis("makanan");
			menu.setHarga(9500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Nasi Ikan Bakar Pedas Manis");
			menu.setJenis("makanan");
			menu.setHarga(12500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Lotek Lontong");
			menu.setJenis("makanan");
			menu.setHarga(8500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Lotek Nasi");
			menu.setJenis("makanan");
			menu.setHarga(9500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Kupat Tahu Petis");
			menu.setJenis("makanan");
			menu.setHarga(8500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Pempek");
			menu.setJenis("makanan");
			menu.setHarga(10500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Juice Melon");
			menu.setJenis("minuman");
			menu.setHarga(5500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Juice Jambu");
			menu.setJenis("minuman");
			menu.setHarga(5500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Juice Alpukat");
			menu.setJenis("minuman");
			menu.setHarga(5500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Juice Strawberry");
			menu.setJenis("minuman");
			menu.setHarga(5500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Juice Mangga");
			menu.setJenis("minuman");
			menu.setHarga(5500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Juice Sirsak");
			menu.setJenis("minuman");
			menu.setHarga(5500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Es Mocca Black");
			menu.setJenis("minuman");
			menu.setHarga(4500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Es Coklat Biskuit");
			menu.setJenis("minuman");
			menu.setHarga(4500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Sop Buah Segar");
			menu.setJenis("minuman");
			menu.setHarga(6500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Es Markisa");
			menu.setJenis("minuman");
			menu.setHarga(4500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Es Teh Manis");
			menu.setJenis("minuman");
			menu.setHarga(3500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Es Jeruk");
			menu.setJenis("minuman");
			menu.setHarga(4500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Es Teh Susu");
			menu.setJenis("minuman");
			menu.setHarga(5500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Es Lemon");
			menu.setJenis("minuman");
			menu.setHarga(5500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Es Lemon Tea");
			menu.setJenis("minuman");
			menu.setHarga(4500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Es Cingcau Selasih");
			menu.setJenis("minuman");
			menu.setHarga(4500);
			menudatasource.add(menu);

			menu.setNamaMakanan("Es Carabian Milk White");
			menu.setJenis("minuman");
			menu.setHarga(5500);
			menudatasource.add(menu);
		}
	}
	
	//dialog part
	//OptionsDialog
    void showOptionsDialog(long id_restoran) {
        DialogFragment newFragment = OptionsDialogFragment.newInstance(id_restoran);
        newFragment.show(getFragmentManager(), "optionsdialog");
    }

    public void ubah(long id_restoran) {
    	showEditDialog(id_restoran);
    }

    public void hapus(long id_restoran) {
    	//confirmation dialog should be added here
    	Restoran deleted = null;
    	for (int i = 0; i < adapter.getCount(); ++i) {
    		if (adapter.getItem(i).getID() == id_restoran){
    			deleted = adapter.getItem(i);
    			break;
    		}
    	}
    	datasource.delete(deleted);
    	adapter.remove(deleted);
    	adapter.notifyDataSetChanged();
    }

    public static class OptionsDialogFragment extends DialogFragment {
    	private static long id_restoran;

        public static OptionsDialogFragment newInstance(long _id_restoran) {
    		id_restoran = _id_restoran;
            OptionsDialogFragment frag = new OptionsDialogFragment();
            //Bundle args = new Bundle();
            //args.putInt("title", title);
            //frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //int title = getArguments().getInt("title");
        	CharSequence[] items = {"Ubah", "Hapus"};

            return new AlertDialog.Builder(getActivity())
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Pilihan")
                    //.setMessage("Halo hai hai")
                    .setItems(items, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								((MainActivity)getActivity()).ubah(id_restoran);
								break;
							case 1:
								((MainActivity)getActivity()).hapus(id_restoran);
								break;
							}
						}
					})
					.create();
        }
    }

    //EditDialog
    void showEditDialog(long id_restoran) {
        DialogFragment newFragment = EditDialogFragment.newInstance(id_restoran);
        newFragment.show(getFragmentManager(), "editdialog");
    }
    
    public void editRestoran(Restoran restoran) {
    	//id_restoran should be the same
    	for (int i = 0; i < adapter.getCount(); ++i){
    		if (adapter.getItem(i).getID() == restoran.getID()){
    			adapter.getItem(i).setNamaRestoran(restoran.getNamaRestoran());
    			adapter.getItem(i).setNomorKontak(restoran.getNomorKontak());
    			break;
    		}
    	}
    	adapter.notifyDataSetChanged();
    	datasource.editRestoran(restoran);
    }

    public static class EditDialogFragment extends DialogFragment {
    	private static long id_restoran;
    	private static Restoran restoran;
    	private EditText nomorKontak, namaRestoran;
    	private ImageButton addFromContact;
    	
        public static EditDialogFragment newInstance(long _id_restoran) {
        	id_restoran = _id_restoran;
            EditDialogFragment frag = new EditDialogFragment();
            //Bundle args = new Bundle();
            //args.putInt("title", title);
            //frag.setArguments(args);
            return frag;
        }
        
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
        	restoran = ((MainActivity)getActivity()).datasource.getRestoran(id_restoran);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_restoran_dialog, null);
            namaRestoran = (EditText) view.findViewById(R.id.editTextNamaRestoran);
            nomorKontak = (EditText) view.findViewById(R.id.editTextNomorKontak);
            addFromContact = (ImageButton) view.findViewById(R.id.imageButtonAddFromContact);
            namaRestoran.setText(restoran.getNamaRestoran());
            nomorKontak.setText(restoran.getNomorKontak());
            addFromContact.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
					i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
					startActivityForResult(i, 1);
				}
			});
            
            return new AlertDialog.Builder(getActivity())
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Ubah Restoran")
                    .setView(view)
                    .setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
                    	
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Restoran restoran = new Restoran();
							restoran.setID(id_restoran);
							restoran.setNamaRestoran(namaRestoran.getText().toString());
							restoran.setNomorKontak(nomorKontak.getText().toString());
							((MainActivity)getActivity()).editRestoran(restoran);
							//Toast.makeText(getActivity(), "nama = "+namaRestoran.getText().toString()+", nomor = "+nomorKontak.getText().toString(), Toast.LENGTH_SHORT).show();
						}
					})
					.setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							/* do nothing */
						}
					})
					.create();
        }
        
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        	if (data != null) {
        		switch (requestCode) {
        		case (1) :
        			if (resultCode == Activity.RESULT_OK) {
        				Uri uri = data.getData();
        				if (uri != null) {
        	                Cursor c = null;
        	                try {
        	                    c = getActivity().getContentResolver().query(uri, null,
        	                            null, null, null);
        	                    if (c != null && c.moveToFirst()) {
        	                    	String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        	                    	nomorKontak.setText(number);
        	                    }
        	                } finally {
        	                    if (c != null) {
        	                        c.close();
        	                    }
        	                }
        				}
        			}
        			break;
        		}
        	}
        }
    }
    
    //AddDialog
    void showAddDialog() {
        DialogFragment newFragment = AddDialogFragment.newInstance();
        newFragment.show(getFragmentManager(), "adddialog");
    }
    
    public void addRestoran(Restoran restoran) {
    	datasource.add(restoran);
    	adapter.add(restoran);
    	adapter.notifyDataSetChanged();
    }

    public static class AddDialogFragment extends DialogFragment {
    	private EditText nomorKontak, namaRestoran;
    	private ImageButton addFromContact;

    	public static AddDialogFragment newInstance() {
            AddDialogFragment frag = new AddDialogFragment();
            //Bundle args = new Bundle();
            //args.putInt("title", title);
            //frag.setArguments(args);
            return frag;
        }
        
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_restoran_dialog, null);
            namaRestoran = (EditText) view.findViewById(R.id.editTextNamaRestoran);
            nomorKontak = (EditText) view.findViewById(R.id.editTextNomorKontak);
            addFromContact = (ImageButton) view.findViewById(R.id.imageButtonAddFromContact);
            addFromContact.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
					i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
					startActivityForResult(i, 1);
				}
			});

            return new AlertDialog.Builder(getActivity())
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Tambah Restoran")
                    .setView(view)
                    .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                    	
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Restoran restoran = new Restoran();
							restoran.setNamaRestoran(namaRestoran.getText().toString());
							restoran.setNomorKontak(nomorKontak.getText().toString());
							((MainActivity)getActivity()).addRestoran(restoran);
							//Toast.makeText(getActivity(), "nama = "+namaRestoran.getText().toString()+", nomor = "+nomorKontak.getText().toString(), Toast.LENGTH_SHORT).show();
						}
					})
					.setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							/* do nothing */
						}
					})
					.create();
        }
        
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        	if (data != null) {
        		switch (requestCode) {
        		case (1) :
        			if (resultCode == Activity.RESULT_OK) {
        				Uri uri = data.getData();
        				if (uri != null) {
        	                Cursor c = null;
        	                try {
        	                    c = getActivity().getContentResolver().query(uri, null,
        	                            null, null, null);
        	                    if (c != null && c.moveToFirst()) {
        	                    	String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        	                    	nomorKontak.setText(number);
        	                    }
        	                } finally {
        	                    if (c != null) {
        	                        c.close();
        	                    }
        	                }
        				}
        			}
        			break;
        		}
        	}
        }

    }
}
