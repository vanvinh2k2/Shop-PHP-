package com.example.banhangapp.model;

public class LoaiSP {
    private int Id;
    private String TenSanPham, HinhAnh;

    public LoaiSP(int id, String tensanpham, String hinhanh) {
        this.Id = id;
        this.TenSanPham = tensanpham;
        this.HinhAnh = hinhanh;
    }

    public LoaiSP() {}

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getTenSanPham() {
        return TenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.TenSanPham = tenSanPham;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.HinhAnh = hinhAnh;
    }
}
