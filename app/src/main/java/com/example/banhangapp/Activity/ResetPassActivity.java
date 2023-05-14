package com.example.banhangapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banhangapp.R;
import com.example.banhangapp.Utils.ReferenceManager;
import com.example.banhangapp.Utils.Utils;
import com.example.banhangapp.model.User;
import com.example.banhangapp.retrofit.ApiBanHang;
import com.example.banhangapp.retrofit.RetrofitClient;

import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ResetPassActivity extends AppCompatActivity {
    EditText email;
    Button xacNhan;
    ApiBanHang apiBanHang;
    ReferenceManager manager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        anhXa();
        getResetEmail();
    }

    private void getResetEmail() {
        xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1 = email.getText().toString().trim();
                if(email1.length()<=0){
                    Toast.makeText(ResetPassActivity.this, "Nhập email", Toast.LENGTH_SHORT).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
                    Toast.makeText(ResetPassActivity.this, "Nhập sai email", Toast.LENGTH_SHORT).show();
                }else{
                    compositeDisposable.add(apiBanHang.resetPass(email1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            Toast.makeText(ResetPassActivity.this, userModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                            startActivity(intent);
                                        }else
                                            Toast.makeText(ResetPassActivity.this, userModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    },
                                    throwable -> {
                                        Toast.makeText(ResetPassActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void anhXa() {
        email = findViewById(R.id.email);
        xacNhan = findViewById(R.id.xacNhan);
        manager = new ReferenceManager(getApplicationContext());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}