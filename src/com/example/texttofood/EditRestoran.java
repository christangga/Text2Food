package com.example.texttofood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditRestoran extends Activity {
	private Button add;
	private EditText namaRestoran;
	private EditText nomorKontak;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_restoran);
		
		add = (Button) findViewById(R.id.buttonAdd);
		namaRestoran = (EditText) findViewById(R.id.editTextNamaRestoran);
		nomorKontak = (EditText) findViewById(R.id.editTextNomorKontak);
		
		add.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				if (namaRestoran.getText().length() == 0) {
					Toast.makeText(getApplicationContext(), "Nama Restoran harus diisi!", Toast.LENGTH_SHORT).show();
					namaRestoran.requestFocus();
				}
				else if (nomorKontak.getText().length() == 0) {
					Toast.makeText(getApplicationContext(), "Nama Restoran harus diisi!", Toast.LENGTH_SHORT).show();
					nomorKontak.requestFocus();
				}
				else {
					Intent i = new Intent();
					/* add information about new restoran */
					i.putExtra("nama_restoran", namaRestoran.getText().toString());
					i.putExtra("nomor_kontak", nomorKontak.getText().toString());
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.add_restoran, menu);
		return true;
	}

}
