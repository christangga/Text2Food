package com.example.texttofood;

public class Restoran {
	private long id;
	private String nama_restoran;
	private String nomor_kontak;
	
	public long getID() {
		return id;
	}
	public void setID(long id) {
		this.id = id;
	}
	public String getNamaRestoran() {
		return nama_restoran;
	}
	public void setNamaRestoran(String nama_restoran) {
		this.nama_restoran = nama_restoran;
	}
	public String getNomorKontak() {
		return nomor_kontak;
	}
	public void setNomorKontak(String nomor_kontak) {
		this.nomor_kontak = nomor_kontak;
	}
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return nama_restoran;
		//return "ID = " + Long.toString(id) + " " + nama_restoran + " (" + nomor_kontak + ")";
	}
}
