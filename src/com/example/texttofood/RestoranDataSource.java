package com.example.texttofood;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RestoranDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {"id", "nama_restoran", "nomor_kontak"};
	
	public RestoranDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	private Restoran cursorToRestoran(Cursor cursor) {
		Restoran restoran = new Restoran();
		restoran.setID(cursor.getLong(0));
		restoran.setNamaRestoran(cursor.getString(1));
		restoran.setNomorKontak(cursor.getString(2));
		return restoran;
	}
	
	public void add (Restoran restoran) {
		ContentValues values = new ContentValues();
		values.put("nama_restoran", restoran.getNamaRestoran());
		values.put("nomor_kontak", restoran.getNomorKontak());
		long id = database.insert("restoran", null, values);
		restoran.setID(id);
	}
	
	public void delete (Restoran restoran) {
		long id = restoran.getID();
		database.delete("restoran", "id = "+id, null);
	}
	
	public void delete (long id) {
		database.delete("restoran", "id = "+id, null);
	}
	
	public long getRestoranID (String nama_restoran) {
		long id = -1;
		Cursor cursor = database.query("restoran", allColumns, "nama_restoran = ?", new String[] {nama_restoran}, null, null, null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getLong(0);
		}
		cursor.close();
		
		return id;
	}
	
	public Restoran getRestoran (String nama_restoran) {
		Restoran restoran = null;
		Cursor cursor = database.query("restoran", allColumns, "nama_restoran = ?", new String[] {nama_restoran}, null, null, null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			restoran = cursorToRestoran(cursor);
		}
		cursor.close();
		
		return restoran;
	}
	
	public Restoran getRestoran (long id) {
		Restoran restoran = null;
		Cursor cursor = database.query("restoran", allColumns, "id = "+id, null, null, null, null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			restoran = cursorToRestoran(cursor);
		}
		cursor.close();
		
		return restoran;
	}
	
	public List<Restoran> getAllRestoran() {
		List<Restoran> restorans = new ArrayList<Restoran>();
		
		Cursor cursor = database.query("restoran", allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Restoran restoran = cursorToRestoran(cursor);
			restorans.add(restoran);
			cursor.moveToNext();
		}
		
		cursor.close();
		return restorans;
	}
	
	public void editRestoran (Restoran restoran) {
		ContentValues values = new ContentValues();
		values.put("nama_restoran", restoran.getNamaRestoran());
		values.put("nomor_kontak", restoran.getNomorKontak());
		
		database.update("restoran", values, "id = "+restoran.getID(), null);
	}
}
