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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    EditText email,pass;
    Button xacNhan;
    TextView dangKi,resetPass;
    ReferenceManager manager;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        anhXa();
        getDangKi();
        getXacNhan();
        getResetPass();
    }

    private void getResetPass() {
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this,ResetPassActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getXacNhan() {
        if(manager.getString("email")!=null && manager.getString("pass")!=null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1 = email.getText().toString().trim();
                String pass1 = pass.getText().toString().trim();
                if(isValidSignUpDetails()){
                    compositeDisposable.add(apiBanHang.setDangNhap(email1,pass1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            showToast(userModel.getMessage()+"");
                                            User user = userModel.getResult().get(0);
                                            manager.putString("email",user.getEmail());
                                            manager.putString("mobile",user.getMobile());
                                            manager.putString("pass",user.getPass());
                                            manager.putString("name",user.getUsername());
                                            manager.putInt("iduser",user.getId());
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                        else showToast(userModel.getMessage());
                                    },
                                    throwable -> {
                                        showToast(throwable.getMessage()+"");
                                    }
                            ));
                }
            }
        });
    }

    private Boolean isValidSignUpDetails() {
        if (email.getText().toString().trim().isEmpty()) {
            showToast("Nhập email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            showToast("Nhập sai email");
            return false;
        } else if (pass.getText().toString().trim().isEmpty()) {
            showToast("Nhập password");
            return false;
        } else return true;
    }

    private void showToast(String value) {
        Toast.makeText(DangNhapActivity.this, value+"", Toast.LENGTH_SHORT).show();
    }

    private void getDangKi() {
        dangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this,DangkiActivity.class);
                startActivity(intent);
            }
        });
    }

    private void anhXa() {
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        xacNhan = findViewById(R.id.xacNhan);
        dangKi = findViewById(R.id.dangKi);
        manager = new ReferenceManager(getApplicationContext());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email.setText(manager.getString("email"));
        //pass.setText(manager.getString("pass"));
        resetPass = findViewById(R.id.resetPass);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}