package com.example.banhangapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.banhangapp.EventBus.TinhTongEvent;
import com.example.banhangapp.R;
import com.example.banhangapp.Utils.Utils;
import com.example.banhangapp.Adapter.GioHangAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {
    Toolbar tool;
    TextView tk,tongTien;
    Button thanhToan;
    RecyclerView recyclerView;
    GioHangAdapter adapter;
    String tongTien1 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        anhXa();
        getToolBar();
        getData();
        getTong();
        getThanhToan();
    }

    private void getThanhToan() {
        thanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.muaHang.clear();
                Intent intent = new Intent(getApplicationContext(),ThanhToanActivity.class);
                intent.putExtra("tien",tongTien1);
                startActivity(intent);
            }
        });
    }

    private void getTong() {
        long tong = 0;
        for(int i=0;i<Utils.muaHang.size();i++){
            tong = tong+Utils.muaHang.get(i).getGia()/**Utils.gioHang.get(i).getSoluong()*/;
        }
        DecimalFormat format = new DecimalFormat("###,###,###");
        tongTien.setText(format.format(tong)+"Đ");
        tongTien1 = tong+"";
    }

    private void getData() {
        if(Utils.gioHang.size()<=0){
            tk.setVisibility(View.VISIBLE);
        }else {
            adapter = new GioHangAdapter(Utils.gioHang, getApplicationContext());
            recyclerView.setAdapter(adapter);
        }
    }

    private void getToolBar() {
        setSupportActionBar(tool);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Giỏ hàng");
        }
        tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.muaHang.clear();
                finish();
            }
        });
    }

    private void anhXa() {
        tool = findViewById(R.id.tool);
        tk = findViewById(R.id.kt);
        tongTien = findViewById(R.id.tongtien);
        thanhToan = findViewById(R.id.thanhtoan);
        recyclerView = findViewById(R.id.list);
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinh(TinhTongEvent event){
        if(event!=null){
            getTong();
        }
    }
}