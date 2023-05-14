package com.example.banhangapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ReferenceManager {
    final private SharedPreferences share;

    public ReferenceManager(Context context) {
        this.share = context.getSharedPreferences("share",Context.MODE_PRIVATE);
    }

    public String getString(String key){
        return share.getString(key,null);
    }

    public void putString(String key, String value){
        SharedPreferences.Editor edit = share.edit();
        edit.putString(key,value);
        edit.apply();
    }

    public int getInt(String key){
        return share.getInt(key,0);
    }

    public void putInt(String key, int value){
        SharedPreferences.Editor edit = share.edit();
        edit.putInt(key,value);
        edit.apply();
    }

    public void clear(){
        SharedPreferences.Editor edit = share.edit();
        edit.clear();
        edit.apply();
    }
}
