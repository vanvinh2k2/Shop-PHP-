package com.example.banhangapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhangapp.R;
import com.example.banhangapp.model.DonHang;
import com.example.banhangapp.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.HolderDetail> {
    List<Item> itemList;

    public DetailAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public HolderDetail onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail,parent,false);
        return new HolderDetail(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDetail holder, int position) {
        Item item = itemList.get(position);
        Picasso.get().load(item.getHinhanh())
                .placeholder(R.drawable.image)
                .into(holder.image);
        holder.name.setText(item.getTensp());
        holder.soluong.setText("Số lượng: "+item.getSoluong());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class HolderDetail extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name,soluong;
        public HolderDetail(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            soluong = itemView.findViewById(R.id.soluong);
        }
    }
}
