package com.example.banhangapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banhangapp.R;
import com.example.banhangapp.Utils.ReferenceManager;
import com.example.banhangapp.Utils.Utils;
import com.example.banhangapp.retrofit.ApiBanHang;
import com.example.banhangapp.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    String tongtien;
    int countItem;
    TextView tien,email,phone;
    EditText diachi;
    Toolbar toolbar;
    Button datHang;
    ReferenceManager manager;
    CompositeDisposable disposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        anhXa();
        getToolBar();
        getData();
        getThongTin();
    }

    private void getToolBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
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
        tien = findViewById(R.id.tien);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.sdt);
        diachi = findViewById(R.id.diachi);
        datHang = findViewById(R.id.datHang);
        toolbar = findViewById(R.id.tool1);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        manager = new ReferenceManager(getApplicationContext());
    }

    private void getThongTin() {
        DecimalFormat format = new DecimalFormat("###,###,###");
        tien.setText(format.format(Double.parseDouble(tongtien))+"Đ");
        email.setText(manager.getString("email"));
        phone.setText(manager.getString("mobile"));
        datHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!diachi.getText().toString().trim().isEmpty()){
                    String address = diachi.getText().toString().trim();
                    //Log.d("test",new Gson().toJson(Utils.gioHang));
                    disposable.add(apiBanHang.createOrder(manager.getInt("iduser"),address,manager.getString("mobile"),
                                    manager.getString("email"),countItem,tongtien,new Gson().toJson(Utils.muaHang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(ThanhToanActivity.this, userModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    },
                                    throwable -> {
                                        Toast.makeText(ThanhToanActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }else Toast.makeText(getApplicationContext(), "Nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        tongtien = intent.getStringExtra("tien");
        //Toast.makeText(ThanhToanActivity.this, tongtien+"", Toast.LENGTH_SHORT).show();
        for(int i=0;i<Utils.muaHang.size();i++){
            countItem += Utils.muaHang.get(i).getSoluong();
        }
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}