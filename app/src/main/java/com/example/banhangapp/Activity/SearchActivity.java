package com.example.banhangapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.banhangapp.Adapter.DienThoaiAdapter;
import com.example.banhangapp.R;
import com.example.banhangapp.Utils.Utils;
import com.example.banhangapp.model.SanPham;
import com.example.banhangapp.retrofit.ApiBanHang;
import com.example.banhangapp.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar tool;
    EditText search;
    RecyclerView listProduct;
    List<SanPham> sanPhamList;
    DienThoaiAdapter adapter;
    CompositeDisposable disposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        anhXa();
        getToolBar();
    }

    private void getToolBar() {
        setSupportActionBar(tool);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tìm kiếm");
        }
        tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void searchProduct(String search1) {
        disposable.add(apiBanHang.search(search1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()){
                                //sanPhamList = sanPhamModel.getResult();
                                adapter = new DienThoaiAdapter(sanPhamModel.getResult(),this);
                                listProduct.setAdapter(adapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void anhXa() {
        tool = findViewById(R.id.tool);
        search = findViewById(R.id.searchedt);
        listProduct = findViewById(R.id.listProduct);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        sanPhamList = new ArrayList<>();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    sanPhamList.clear();
                    adapter = new DienThoaiAdapter(sanPhamList,SearchActivity.this);
                    listProduct.setAdapter(adapter);
                }else{
                    searchProduct(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}