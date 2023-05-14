package com.example.banhangapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class DienThoaiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<SanPham> list;
    Context context;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOAD = 1;

    public DienThoaiAdapter(List<SanPham> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(context).inflate(R.layout.item_phone,parent,false);
            return new HolderDienThoai(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.load_progress,parent,false);
            return new HoderLoading(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HolderDienThoai){
            HolderDienThoai myHolder = (HolderDienThoai) holder;
            SanPham sanPham = list.get(position);
            Picasso.get().load(sanPham.getHinhanh())
                    .placeholder(R.drawable.image)
                    .into(myHolder.anh);
            myHolder.name.setText(sanPham.getTensp());
            myHolder.mota.setText(sanPham.getMota());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myHolder.gia.setText("Giá: "+decimalFormat.format(sanPham.getGiasp())+"Đ");
            myHolder.setItemClickListen(new ItemClickListen() {
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
        else{
            HoderLoading myHolder =(HoderLoading) holder;
            myHolder.load.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position) == null)
            return VIEW_TYPE_LOAD;
        else return VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderDienThoai extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView anh;
        TextView name,mota,gia;
        private ItemClickListen itemClickListen;
        public HolderDienThoai(@NonNull View itemView) {
            super(itemView);
            anh = itemView.findViewById(R.id.anhSp);
            name = itemView.findViewById(R.id.nameSp);
            mota = itemView.findViewById(R.id.motaSp);
            gia = itemView.findViewById(R.id.giaSp);
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
    class HoderLoading extends RecyclerView.ViewHolder {
        ProgressBar load;
        public HoderLoading(@NonNull View itemView) {
            super(itemView);
            load = itemView.findViewById(R.id.load);
        }
    }
}
