package com.example.halukay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<PurchaseModel> mData;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth fAuth;

    public MyItemAdapter(Context mContext, ArrayList<PurchaseModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v ;
        v = LayoutInflater.from(mContext).inflate(R.layout.my_items,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemAdapter.MyViewHolder holder, int position) {
        PurchaseModel purchaseModel = mData.get(position);
        Glide.with(mContext).load(purchaseModel.getProductImg()).into(holder.pImg);
        holder.price.setText(purchaseModel.getProductPrice());
        holder.name.setText(purchaseModel.getProductName());
        holder.desc.setText(purchaseModel.getProductDesc());

        root = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                //set title
                builder.setTitle("Remove Item");
                //set message
                builder.setMessage("Are you sure you want to remove this item?");
                //positive yes
                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //remove to deliver
                        String uid = fAuth.getCurrentUser().getUid();
                        reference = root.getReference(uid+"/myItem");
                        reference.child(purchaseModel.getProductId()).removeValue();
                        goToPublic(purchaseModel.getProductCat(),purchaseModel.getProductId());

                    }
                });
                //negative no
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    //dismiss dialog
                    dialog.dismiss();
                });
                builder.show();
            }
        });
    }

    private void goToPublic(String productCat, String productId) {
        reference = root.getReference(productCat);
        reference.child(productId).removeValue();
        Toast.makeText(mContext, "Removed Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(mContext, MyItem.class);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView pImg;
        private TextView name, price, desc;
        TextView remove;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pImg = itemView.findViewById(R.id.my_img);
            price = itemView.findViewById(R.id.price);
            desc = itemView.findViewById(R.id.product_desc);
            name = itemView.findViewById(R.id.product_name);
            remove = itemView.findViewById(R.id.remove);
        }
    }
}
