package com.example.banhangapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.banhangapp.Adapter.SPAdapter;
import com.example.banhangapp.Adapter.SanPhamMoiAdapter;
import com.example.banhangapp.R;
import com.example.banhangapp.Utils.ReferenceManager;
import com.example.banhangapp.Utils.Utils;
import com.example.banhangapp.model.LoaiSP;
import com.example.banhangapp.model.SanPham;
import com.example.banhangapp.retrofit.ApiBanHang;
import com.example.banhangapp.retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper flipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView;
    DrawerLayout draw;
    SPAdapter spAdapter;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    List<LoaiSP> loaiSPList;
    List<SanPham> sanPhamList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    NotificationBadge tb;
    ImageView cart;
    ImageButton search;
    ReferenceManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        anhXa();
        actionBar();
        if(!isConnected(this)){
            Toast.makeText(this, "Ko co ket noi Internet", Toast.LENGTH_SHORT).show();
        }else {
            actionViewFlipper();
            //Toast.makeText(this, "Ok Internet", Toast.LENGTH_SHORT).show();
            getLoaiSanPham();
            getLoaiSanPhamMoi();
            getClickNavigation();
            moveCart();
        }
    }

    private void moveCart() {
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SearchActivity.class));
            }
        });
    }

    private void getClickNavigation() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else if(position == 1){
                    Intent intent = new Intent(getApplicationContext(),DienThoaiActivity.class);
                    intent.putExtra("loai",1);
                    startActivity(intent);
                }
                else if(position == 2){
                    Intent intent = new Intent(getApplicationContext(),DienThoaiActivity.class);
                    intent.putExtra("loai",2);
                    startActivity(intent);
                }
                else if(position == 3){

                }
                else if (position == 5){
                    Intent intent  = new Intent(getApplicationContext(),ViewOrderActivity.class);
                    startActivity(intent);
                }
                else if (position == 6){
                    manager.putString("pass",null);
                    Intent intent  = new Intent(getApplicationContext(),DangNhapActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getLoaiSanPhamMoi() {
        compositeDisposable.add(apiBanHang.getSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()){
                                sanPhamList = sanPhamModel.getResult();
                                sanPhamMoiAdapter = new SanPhamMoiAdapter(sanPhamList,MainActivity.this);
                                recyclerView.setAdapter(sanPhamMoiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(MainActivity.this, "Ket noi bi loi 1!", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSP()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSPModel -> {
                            if(loaiSPModel.isSuccess()){
                                loaiSPList = loaiSPModel.getResult();
                                spAdapter = new SPAdapter(loaiSPList,MainActivity.this,R.layout.demo_san_pham);
                                listView.setAdapter(spAdapter);
                            }
                            //Toast.makeText(this, loaiSPModel.getResult().get(0).getTenSanPham()+"", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Toast.makeText(MainActivity.this, "Ket noi bi loi 2!", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void actionViewFlipper() {
        List<String> quangCao = new ArrayList<>();
        quangCao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        quangCao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        quangCao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i=0;i<quangCao.size();i++){
            ImageView anh = new ImageView(MainActivity.this);
            Picasso.get().load(quangCao.get(i)).into(anh);
            anh.setScaleType(ImageView.ScaleType.FIT_XY);
            flipper.addView(anh);
        }
        flipper.setFlipInterval(3000);
        flipper.setAutoStart(true);
        Animation animationIn = AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_in_right);
        Animation animationout = AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_out_right);
        flipper.setInAnimation(animationIn);
        flipper.setOutAnimation(animationout);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    draw.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    private void anhXa() {
        toolbar = findViewById(R.id.tool_bar);
        flipper = findViewById(R.id.view_flipper);
        recyclerView = findViewById(R.id.list_san_pham);
        navigationView = findViewById(R.id.navigation);
        listView = findViewById(R.id.list_man_hinh);
        draw = findViewById(R.id.drawer);
        tb = findViewById(R.id.soluong);
        cart = findViewById(R.id.cart);
        search = findViewById(R.id.search);
        manager = new ReferenceManager(getApplicationContext());
        loaiSPList = new ArrayList<>();
        if(Utils.gioHang == null){
            Utils.gioHang = new ArrayList<>();
        }else{
            tb.setText(String.valueOf(Utils.gioHang.size()));
        }
    }
    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) /*|| (mobile != null && mobile.isConnected())*/) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        tb.setText(String.valueOf(Utils.gioHang.size()));
        super.onResume();
    }
}