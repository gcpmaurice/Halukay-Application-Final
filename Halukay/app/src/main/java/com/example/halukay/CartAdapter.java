package com.example.halukay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CartAdapter extends  RecyclerView.Adapter<MyHolder>  {
    Context mContext;
    ArrayList<ProductOrderModel> listCart;
    ArrayList<ProductOrderModel> checkedOrder = new ArrayList<>();

    public CartAdapter(Context mContext, ArrayList<ProductOrderModel> listCart) {
        this.mContext = mContext;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_cart,parent,false);
        MyHolder myHolder = new MyHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        ProductOrderModel productOrderModel = listCart.get(position);
        Glide.with(mContext).load(productOrderModel.getProductImg()).into(holder.pImg);
        holder.price.setText(productOrderModel.getProductPrice());
        holder.desc.setText(productOrderModel.getProductDesc());

      holder.setOrderListener((v, pos) -> {
          CheckBox check = (CheckBox) v;

          if (check.isChecked()){
              checkedOrder.add(listCart.get(pos));
          }else if (!check.isChecked()){
              checkedOrder.remove(listCart.get(pos));
          }
      });



    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

 /*   public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb;
        TextView price,desc;
        ImageView pImg;

        AdapterView.OnItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cb = itemView.findViewById(R.id.check_box);
            price = itemView.findViewById(R.id.price);
            desc = itemView.findViewById(R.id.desc);
            pImg = itemView.findViewById(R.id.product_img);
        }

        public  void setItemClickListener(AdapterView.OnItemClickListener ic){
            this.itemClickListener = ic;
        }
    }*/
}
