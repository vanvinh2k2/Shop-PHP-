package com.example.banhangapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhangapp.EventBus.TinhTongEvent;
import com.example.banhangapp.Interface.ItemClickButton;
import com.example.banhangapp.R;
import com.example.banhangapp.Utils.Utils;
import com.example.banhangapp.model.GioHang;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.HolderGioHang>{
    List<GioHang> list;
    Context context;

    public GioHangAdapter(List<GioHang> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderGioHang onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gio_hang,parent,false);
        return new HolderGioHang(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGioHang holder, int position) {
        GioHang gioHang = list.get(position);
        Picasso.get().load(gioHang.getHinhanh())
                .placeholder(R.drawable.image)
                .into(holder.anhimg);
        holder.nametxt.setText(gioHang.getTen());
        holder.soLuongtxt.setText(gioHang.getSoluong()+"");
        long giaSp = gioHang.getGia()/gioHang.getSoluong();
        DecimalFormat format = new DecimalFormat("###,###,###");
        holder.tongTientxt.setText("Tổng tiền: "+format.format(gioHang.getGia())+"Đ");
        holder.giatxt.setText("Giá: "+format.format(giaSp)+"Đ");

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Utils.muaHang.add(gioHang);
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }else{
                    for(int i = 0;i<Utils.muaHang.size();i++){
                        if(Utils.muaHang.get(i).getIdsanpham() == gioHang.getIdsanpham()){
                            Utils.muaHang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });

        holder.setListenner(new ItemClickButton() {
            @Override
            public void onImageClick(View view, int pos, int giaTri) {
                if(giaTri == 1){
                    if(list.get(pos).getSoluong() < 11){
                        int soluongmoi = list.get(pos).getSoluong()+1;
                        list.get(pos).setSoluong(soluongmoi);
                        list.get(pos).setGia(list.get(pos).getGia()+giaSp);
                    }
                }
                else if(giaTri == 2){
                    if(list.get(pos).getSoluong()>1){
                        int soluongmoi = list.get(pos).getSoluong()-1;
                        list.get(pos).setSoluong(soluongmoi);
                        list.get(pos).setGia(list.get(pos).getGia()-giaSp);
                    }
                }
                else if(giaTri == 3){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("Thông báo");
                    builder.setIcon(R.drawable.ic_notifications);
                    builder.setMessage("Bạn muốn xóa sản phẩm này khỏi giỏ hàng");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utils.gioHang.remove(pos);
                            notifyDataSetChanged();
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                holder.soLuongtxt.setText(list.get(pos).getSoluong()+"");
                holder.tongTientxt.setText("Tổng tiền: "+format.format(gioHang.getGia())+"Đ");
                EventBus.getDefault().postSticky(new TinhTongEvent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderGioHang extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView anhimg,add,tru;
        ItemClickButton listenner;
        Button huybtn;
        CheckBox check;
        TextView nametxt,giatxt,tongTientxt,soLuongtxt;
        public HolderGioHang(@NonNull View itemView) {
            super(itemView);
            anhimg = itemView.findViewById(R.id.anhSp);
            add = itemView.findViewById(R.id.add);
            tru = itemView.findViewById(R.id.tru);
            huybtn = itemView.findViewById(R.id.xoa);
            nametxt = itemView.findViewById(R.id.tenSp);
            giatxt = itemView.findViewById(R.id.giaSp);
            check = itemView.findViewById(R.id.check);
            tongTientxt = itemView.findViewById(R.id.tongTienSp);
            soLuongtxt = itemView.findViewById(R.id.soSp);
            add.setOnClickListener(this);
            tru.setOnClickListener(this);
            huybtn.setOnClickListener(this);
        }

        public void setListenner(ItemClickButton listenner) {
            this.listenner = listenner;
        }

        @Override
        public void onClick(View v) {
            if(v == add){
                listenner.onImageClick(v,getAdapterPosition(),1);
            }
            else if(v == tru){
                listenner.onImageClick(v,getAdapterPosition(),2);
            }
            else if(v == huybtn){
                listenner.onImageClick(v,getAdapterPosition(),3);
            }
        }

    }
}
