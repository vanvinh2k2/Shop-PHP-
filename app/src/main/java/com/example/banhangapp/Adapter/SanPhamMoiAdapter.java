package com.example.banhangapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhangapp.Activity.ChiTietActivity;
import com.example.banhangapp.Interface.ItemClickListen;
import com.example.banhangapp.R;
import com.example.banhangapp.model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.SanPhamHolder>{
    List<SanPham> list;
    Context context;

    public SanPhamMoiAdapter(List<SanPham> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SanPhamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham,parent,false);
        return new SanPhamHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamHolder holder, int position) {
        SanPham sanPham = list.get(position);
        Picasso.get().load(sanPham.getHinhanh())
                .placeholder(R.drawable.image)
                .into(holder.anh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.gia.setText("Giá: "+decimalFormat.format(sanPham.getGiasp())+"Đ");
        holder.name.setText(sanPham.getTensp()+"");
        holder.setItemClickListen(new ItemClickListen() {
            @Override
            public void setOnItemClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("dulieu",sanPham);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SanPhamHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView anh;
        TextView gia,name;
        ItemClickListen itemClickListen;
        public SanPhamHolder(@NonNull View itemView) {
            super(itemView);
            anh = itemView.findViewById(R.id.anhSp);
            gia = itemView.findViewById(R.id.giaSp);
            name = itemView.findViewById(R.id.nameSp);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListen.setOnItemClick(v,getAdapterPosition(),false);
        }

        public void setItemClickListen(ItemClickListen itemClickListen) {
            this.itemClickListen = itemClickListen;
        }
    }
}
