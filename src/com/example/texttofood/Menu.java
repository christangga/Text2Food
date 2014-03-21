package com.example.texttofood;

public class Menu {
	private long id;
	private long id_restoran;
	private String nama_makanan;
	private String jenis;
	private long harga;
	private long quantity;
	
	public long getID() {
		return id;
	}
	public void setID(long id) {
		this.id = id;
	}
	public long getIDRestoran() {
		return id_restoran;
	}
	public void setIDRestoran(long id_restoran) {
		this.id_restoran = id_restoran;
	}
	public String getNamaMakanan() {
		return nama_makanan;
	}
	public void setNamaMakanan(String nama_makanan) {
		this.nama_makanan = nama_makanan;
	}
	public String getJenis() {
		return jenis;
	}
	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
	public long getHarga() {
		return harga;
	}
	public void setHarga(long harga) {
		this.harga = harga;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return nama_makanan + " (" + Long.toString(harga) + ")";
	}

}
