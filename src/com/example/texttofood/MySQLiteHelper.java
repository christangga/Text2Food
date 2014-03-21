package com.example.texttofood;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "apaya";
	private static final String RESTORAN = "restoran";
	private static final String MENU = "menu";
	
	public MySQLiteHelper(Context context){
		super(context,DATABASE_NAME,null,1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		/* Buat tabel restoran */
		db.execSQL("CREATE TABLE " + RESTORAN + "" +
				"(id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nama_restoran TEXT," +
				" nomor_kontak TEXT);");
		/* Buat tabel menu */
		db.execSQL("CREATE TABLE " + MENU +
				" (id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" id_restoran INTEGER," +
				" nama_makanan TEXT," +
				" jenis TEXT," +
				" harga INTEGER," +
				" quantity INTEGER);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + RESTORAN);
		db.execSQL("DROP TABLE IF EXISTS " + MENU);
		onCreate(db);
	}
}
