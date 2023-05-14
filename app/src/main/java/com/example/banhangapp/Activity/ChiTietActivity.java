package com.example.banhangapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.banhangapp.R;
import com.example.banhangapp.Utils.Utils;
import com.example.banhangapp.model.GioHang;
import com.example.banhangapp.model.SanPham;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp, giasp, mota;
    Button thembtn;
    ImageView anhimg,cart;
    Spinner spinner;
    Toolbar toolbar;
    SanPham sanPham;
    NotificationBadge tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        anhXa();
        setToolBar();
        getData();
        getGioHang();
        getCart();
    }

    private void getCart() {
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getGioHang() {
        thembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if(Utils.gioHang.size()>0){
            boolean flag2 = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i=0;i<Utils.gioHang.size();i++){
                if(Utils.gioHang.get(i).getIdsanpham() == sanPham.getId()){
                    Utils.gioHang.get(i).setSoluong(soluong+Utils.gioHang.get(i).getSoluong());
                    long gia  = sanPham.getGiasp()*Utils.gioHang.get(i).getSoluong();
                    Utils.gioHang.get(i).setGia(gia);
                    flag2 = true;
                }
            }
            if(flag2 == false){
                long gia = sanPham.getGiasp()*soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGia(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsanpham(sanPham.getId());
                gioHang.setTen(sanPham.getTensp());
                gioHang.setHinhanh(sanPham.getHinhanh());
                Utils.gioHang.add(gioHang);
            }

        } else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = sanPham.getGiasp()*soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGia(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsanpham(sanPham.getId());
            gioHang.setTen(sanPham.getTensp());
            gioHang.setHinhanh(sanPham.getHinhanh());
            Utils.gioHang.add(gioHang);
        }
        tb.setText(String.valueOf(Utils.gioHang.size()));
    }

    private void getData() {
        Intent intent = getIntent();
        sanPham = (SanPham) intent.getSerializableExtra("dulieu");
        tensp.setText(sanPham.getTensp());
        DecimalFormat format = new DecimalFormat("###,###,###");
        giasp.setText("Giá: "+format.format(sanPham.getGiasp())+"Đ");
        mota.setText(sanPham.getMota());
        Picasso.get().load(sanPham.getHinhanh())
                .placeholder(R.drawable.image)
                .into(anhimg);
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapter);
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Chi tiết sản phẩm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhXa() {
        tensp = findViewById(R.id.name);
        giasp = findViewById(R.id.gia);
        mota = findViewById(R.id.mota);
        thembtn = findViewById(R.id.giohang);
        anhimg = findViewById(R.id.sanpham);
        spinner = findViewById(R.id.spinner);
        toolbar = findViewById(R.id.toolbar);
        tb = findViewById(R.id.soluong);
        cart = findViewById(R.id.cart);
        tb.setText(String.valueOf(Utils.gioHang.size()));
    }

    @Override
    protected void onResume() {
        tb.setText(String.valueOf(Utils.gioHang.size()));
        super.onResume();
    }
}