package com.example.banhangapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.banhangapp.R;
import com.example.banhangapp.model.LoaiSP;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SPAdapter extends BaseAdapter{
    List<LoaiSP> list;
    Context context;
    int layout;

    public SPAdapter(List<LoaiSP> list, Context context, int layout) {
        this.list = list;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    class HolderLoaiSP{
        ImageView anh;
        TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderLoaiSP holder;
        if(convertView == null){
            holder = new HolderLoaiSP();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.anh = convertView.findViewById(R.id.item_image);
            holder.name = convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        }
        else {
            holder = (HolderLoaiSP) convertView.getTag();
        }
        LoaiSP sanPham = list.get(position);
        Picasso.get().load(sanPham.getHinhAnh())
                .placeholder(R.drawable.image)
                .into(holder.anh);
        holder.name.setText(sanPham.getTenSanPham());
        return convertView;
    }
}
