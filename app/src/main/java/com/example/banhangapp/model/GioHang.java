package com.example.banhangapp.model;

public class GioHang {
    private String ten,hinhanh;
    private int idsanpham,soluong;
    private long gia;

    public GioHang(){}

    public GioHang(String ten, String hinhanh, int id, int soluong, long gia) {
        this.ten = ten;
        this.hinhanh = hinhanh;
        this.idsanpham = id;
        this.soluong = soluong;
        this.gia = gia;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public int getIdsanpham() {
        return idsanpham;
    }

    public void setIdsanpham(int idsanpham) {
        this.idsanpham = idsanpham;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public long getGia() {
        return gia;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }
}
