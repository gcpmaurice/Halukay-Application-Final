package com.example.halukay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<PurchaseModel> mData;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth fAuth;

    public PurchaseAdapter(Context mContext, ArrayList<PurchaseModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public PurchaseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v ;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_purchase,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.MyViewHolder holder, int position) {
        PurchaseModel purchaseModel = mData.get(position);
        Glide.with(mContext).load(purchaseModel.getProductImg()).into(holder.pImg);
        holder.price.setText(purchaseModel.getProductPrice());
        holder.status.setText(purchaseModel.getStatus());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView pImg;
        private TextView price, status;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pImg = itemView.findViewById(R.id.my_img);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.myStatus);
        }
    }
}
