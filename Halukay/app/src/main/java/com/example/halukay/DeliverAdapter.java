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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DeliverAdapter extends RecyclerView.Adapter<DeliverAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<PurchaseModel> mData;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth fAuth;

    public static String mName,address,phone;

    public DeliverAdapter(Context mContext, ArrayList<PurchaseModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public DeliverAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v ;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_delivery,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliverAdapter.MyViewHolder holder, int position) {
        PurchaseModel purchaseModel = mData.get(position);
        Glide.with(mContext).load(purchaseModel.getProductImg()).into(holder.pImg);
        holder.price.setText(purchaseModel.getProductPrice());
        holder.status.setText(purchaseModel.getStatus());
        holder.name.setText(purchaseModel.getProductName());

        root = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();



        holder.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customer = purchaseModel.getCustomerId();
                reference = root.getReference(customer+"/info");

               // Toast.makeText(mContext, customer, Toast.LENGTH_SHORT).show();

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mName =  snapshot.child("name").getValue().toString();
                        address =  snapshot.child("address").getValue().toString();
                        phone =  snapshot.child("phone").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(view.getContext(), "DATABASE ERROR", Toast.LENGTH_SHORT).show();
                    }
                });

                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                //set title
                builder.setTitle("Delivered?");
                //set message
                builder.setMessage("Are you sure that this item is already received by:\nName: "+mName
                        +"\nAddres: "+address+"\nPhone: "+phone);
                //positive yes
                builder.setPositiveButton("Delivered", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //remove to deliver
                        String uid = fAuth.getCurrentUser().getUid();
                        reference = root.getReference(uid+"/ToDeliver");
                        reference.child(purchaseModel.getOrderId()).removeValue();
                        goStatus(purchaseModel.getCustomerId(),purchaseModel.getOrderId());
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

    private void goStatus(String customerId, String orderId) {
        reference = root.getReference(customerId+"/MyPurchase");
        HashMap<String,Object> update = new HashMap<>();
        update.put("status", "Delivered");
        reference.child(orderId).updateChildren(update);
        Intent intent = new Intent(mContext,ToDeliver.class);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView pImg;
        private TextView price, status,name;
        TextView delivered;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pImg = itemView.findViewById(R.id.my_img);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.myStatus);
            name = itemView.findViewById(R.id.pname);
            delivered = itemView.findViewById(R.id.deliverBtn);
        }
    }
}
