package com.example.banhangapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.banhangapp.Adapter.DienThoaiAdapter;
import com.example.banhangapp.Interface.ItemClickListen;
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

public class DienThoaiActivity extends AppCompatActivity{
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPham> list;
    DienThoaiAdapter adapter;
    RecyclerView recycleView;
    Toolbar toolbar;
    Handler handler = new Handler();
    LinearLayoutManager linearLayoutManager;
    boolean isload = false;
    int loai;
    int page = 1;
    int vt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        anhXa();
        getLoai();
        getToolbar();
        getSanPham(page);
        getLoading();
    }

    private void getLoai() {
        Intent intent = getIntent();
        loai = intent.getIntExtra("loai",1);
    }

    private void getLoading() {
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(isload == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size()-1){
                        isload = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                list.add(null);
                vt = list.size()-1;
                adapter.notifyItemInserted(list.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.remove(vt);
                adapter.notifyItemRemoved(list.size());
                page = page + 1;
                getSanPham(page);
                adapter.notifyDataSetChanged();
                isload = false;
            }
        },1000);
    }

    private void getToolbar() {
        setSupportActionBar(toolbar);
        if(loai == 2) getSupportActionBar().setTitle("Laptop");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getSanPham(int page) {
        compositeDisposable.add(apiBanHang.getDienThoai(page,loai)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                sanPhamModel -> {
                                    if(sanPhamModel.isSuccess()){
                                        if(adapter == null){
                                            list = sanPhamModel.getResult();
                                            adapter = new DienThoaiAdapter(list,DienThoaiActivity.this);
                                            recycleView.setAdapter(adapter);
                                        }else{
                                            int vitri = list.size()-1;
                                            int soluong = sanPhamModel.getResult().size();
                                            for(int i=0; i<soluong; i++){
                                                list.add(sanPhamModel.getResult().get(i));
                                                adapter.notifyItemInserted(vitri+i);
                                            }
                                            //adapter.notifyItemRangeInserted(vitri,soluong);
                                        }
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(DienThoaiActivity.this, "Ket noi bi loi!", Toast.LENGTH_SHORT).show();
                                }
        ));
    }

    private void anhXa() {
        recycleView = findViewById(R.id.list_phone);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycleView.setLayoutManager(linearLayoutManager);
        toolbar = findViewById(R.id.toolbar2);
        list = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(DienThoaiActivity.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}