package com.example.halukay;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    CheckBox cb,cod;
    TextView price,desc;
    ImageView pImg;

    OrderListener itemClickListener;
    public MyHolder(@NonNull View itemView) {
        super(itemView);

        cb = itemView.findViewById(R.id.check_box);
        price = itemView.findViewById(R.id.price);
        desc = itemView.findViewById(R.id.desc);
        pImg = itemView.findViewById(R.id.product_img);



        cb.setOnClickListener(this);
    }

    public  void setOrderListener(OrderListener ol){
        this.itemClickListener = ol;
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onOrderClick(view,getLayoutPosition());
    }
}
