package com.example.texttofood;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MenuDataSource {
	//Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {"id", "id_restoran", "nama_makanan", "jenis", "harga", "quantity"};
	
	public MenuDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	private Menu cursorToMenu(Cursor cursor) {
		Menu menu = new Menu();
		menu.setID(cursor.getLong(0));
		menu.setIDRestoran(cursor.getLong(1));
		menu.setNamaMakanan(cursor.getString(2));
		menu.setJenis(cursor.getString(3));
		menu.setHarga(cursor.getLong(4));
		menu.setQuantity(cursor.getLong(5));
		return menu;
	}
	
	public void add(Menu menu) {
		ContentValues values = new ContentValues();
		values.put("id_restoran", menu.getIDRestoran());
		values.put("nama_makanan", menu.getNamaMakanan());
		values.put("jenis", menu.getJenis());
		values.put("harga", menu.getHarga());
		values.put("quantity", menu.getQuantity());
		long id = database.insert("menu", null, values);
		menu.setID(id);
	}
	
	public void delete(Menu menu) {
		long id = menu.getID();
		database.delete("menu", "ID = "+id, null);
	}
	
	public void update(Menu menu) {
		long id = menu.getID();
		ContentValues values = new ContentValues();
		values.put("id_restoran", menu.getIDRestoran());
		values.put("nama_makanan", menu.getNamaMakanan());
		values.put("jenis", menu.getJenis());
		values.put("harga", menu.getHarga());
		values.put("quantity", menu.getQuantity());
		database.update("menu", values, "id = ?", new String[]{Long.toString(id)});
	}
	
	public long getMenuID(String nama_makanan) {
		long id = -1;
		Cursor cursor = database.query("menu", allColumns, "nama_makanan = ?", new String[] {nama_makanan}, null, null, null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getLong(0);
		}
		cursor.close();
		
		return id;
	}
	
	public Menu getMenu(String nama_makanan) {
		Menu menu = null;
		Cursor cursor = database.query("menu", allColumns, "nama_makanan = ?", new String[] {nama_makanan}, null, null, null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			menu = cursorToMenu(cursor);
		}
		cursor.close();
		
		return menu;
	}
	
	public Menu getMenu(Long id) {
		Menu menu = null;
		Cursor cursor = database.query("menu", allColumns, "id = ?", new String[] {Long.toString(id)}, null, null, null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			menu = cursorToMenu(cursor);
		}
		cursor.close();
		
		return menu;
	}
	
	public List<Menu> getAllMenu(Long id_restoran) {
		List<Menu> menus = new ArrayList<Menu>();
		
		Cursor cursor = database.query("menu", allColumns, "id_restoran = ?", new String[] {Long.toString(id_restoran)}, null, null, null);
	
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Menu menu = cursorToMenu(cursor);
			menus.add(menu);
			cursor.moveToNext();
		}
		
		cursor.close();
		return menus;
	}
	
	public List<Menu> getAllMakanan(Long id_restoran) {
		List<Menu> menus = new ArrayList<Menu>();
		
		Cursor cursor = database.query("menu", allColumns, "id_restoran = ? and jenis = ?", new String[] {Long.toString(id_restoran),"makanan"}, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Menu menu = cursorToMenu(cursor);
			menus.add(menu);
			cursor.moveToNext();
		}
		
		cursor.close();
		return menus;
	}
	
	public List<Menu> getAllMinuman(Long id_restoran) {
		List<Menu> menus = new ArrayList<Menu>();
		
		Cursor cursor = database.query("menu", allColumns, "id_restoran = ? and jenis = ?", new String[] {Long.toString(id_restoran),"minuman"}, null, null, null);
	
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Menu menu = cursorToMenu(cursor);
			menus.add(menu);
			cursor.moveToNext();
		}
		
		cursor.close();
		return menus;
	}

}
