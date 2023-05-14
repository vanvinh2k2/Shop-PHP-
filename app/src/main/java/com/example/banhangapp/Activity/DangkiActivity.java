package com.example.banhangapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.banhangapp.R;
import com.example.banhangapp.Utils.ReferenceManager;
import com.example.banhangapp.Utils.Utils;
import com.example.banhangapp.retrofit.ApiBanHang;
import com.example.banhangapp.retrofit.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangkiActivity extends AppCompatActivity {
    EditText email,phone,name,pass,conpass;
    Button xacNhan;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    ReferenceManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);
        anhXa();
        getDangKi();
    }

    private void getDangKi() {
        xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1 = email.getText().toString().trim();
                String phone1 = phone.getText().toString().trim();
                String name1 = name.getText().toString().trim();
                String pass1 = pass.getText().toString().trim();
                String conpass1 = conpass.getText().toString().trim();

                if(email1.isEmpty()) {
                    showToast("Nhập email");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
                    showToast("Nhập sai email");
                } else if (pass1.isEmpty()) {
                    showToast("Nhập password");
                }else if (pass1.length()<=6) {
                    showToast("Nhập password có độ dài tối thiểu là 6");
                } else if (conpass1.isEmpty()) {
                    showToast("Nhập import password");
                } else if (name1.isEmpty()) {
                    showToast("Nhập tên");
                } else if (phone1.isEmpty()) {
                    showToast("Nhập số điện thoại");
                } else if (phone1.length()!=10) {
                    showToast("Nhập sai số điện thoại");
                } else{
                    if(conpass1.equals(pass1)){
                        compositeDisposable.add(apiBanHang.setDangKi(email1,pass1,name1,phone1)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        userModel -> {
                                            if(userModel.isSuccess()){
                                                showToast(userModel.getMessage()+"");
                                                Intent intent = new Intent(DangkiActivity.this,DangNhapActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                showToast(userModel.getMessage()+"");
                                            }
                                        },
                                        throwable -> {
                                            showToast("Lỗi");
                                        }
                                ));
                    }

                }
            }
        });
    }

    private void anhXa() {
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        xacNhan = findViewById(R.id.xacNhan);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        conpass = findViewById(R.id.conPass);
        manager = new ReferenceManager(getApplicationContext());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    private void showToast(String value) {
        Toast.makeText(DangkiActivity.this, value+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}