package com.example.banhangapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.banhangapp.Adapter.DonHangAdapter;
import com.example.banhangapp.R;
import com.example.banhangapp.Utils.ReferenceManager;
import com.example.banhangapp.Utils.Utils;
import com.example.banhangapp.model.DonHang;
import com.example.banhangapp.retrofit.ApiBanHang;
import com.example.banhangapp.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ViewOrderActivity extends AppCompatActivity {
    CompositeDisposable disposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    Toolbar toolbar;
    RecyclerView listOrder;
    DonHangAdapter adapter;
    ReferenceManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        anhXa();
        getToolbar();
        getData();
    }

    private void getToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        disposable.add(apiBanHang.viewOrder(manager.getInt("iduser"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            adapter = new DonHangAdapter(this,donHangModel.getResult());
                            listOrder.setAdapter(adapter);
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void anhXa() {
        toolbar = findViewById(R.id.tool_bar);
        listOrder = findViewById(R.id.listOrder);
        manager = new ReferenceManager(getApplicationContext());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}