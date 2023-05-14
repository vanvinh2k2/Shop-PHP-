package com.example.banhangapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhangapp.Activity.ChiTietActivity;
import com.example.banhangapp.R;
import com.example.banhangapp.model.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.HolderDonhang> {
    Context context;
    List<DonHang> donHangList;

    public DonHangAdapter(Context context, List<DonHang> donHangList) {
        this.context = context;
        this.donHangList = donHangList;
    }
    @NonNull
    @Override
    public HolderDonhang onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order,parent,false);
        return new HolderDonhang(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDonhang holder, int position) {
        DonHang donHang = donHangList.get(position);
        DetailAdapter adapter = new DetailAdapter(donHang.getItem());
        holder.listDetail.setAdapter(adapter);
        holder.madon.setText("Mã hóa đơn: "+donHang.getId());
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    class HolderDonhang extends RecyclerView.ViewHolder{
        TextView madon;
        RecyclerView listDetail;
        public HolderDonhang(@NonNull View itemView) {
            super(itemView);
            madon = itemView.findViewById(R.id.idOrder);
            listDetail = itemView.findViewById(R.id.listItem);
        }
    }
}
