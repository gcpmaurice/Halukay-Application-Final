package com.example.halukay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<ProductSellModel> mData;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth fAuth;

    public ProductAdapter(Context mContext, ArrayList<ProductSellModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v ;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_product,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
        ProductSellModel productSellModel = mData.get(position);
        Glide.with(mContext).load(productSellModel.getProductImg()).into(holder.pImg);
        holder.price.setText(productSellModel.getProductPrice());
        holder.desc.setText(productSellModel.getProductDesc());

        root = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();

        String cust_id = fAuth.getCurrentUser().getUid();

        holder.product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                //set title
                builder.setTitle("Add To Cart");
                //set message
                builder.setMessage("Do you want to add this product to cart now?\nProduct: "+productSellModel.getProductName()
                +"\nPrice: "+productSellModel.getProductPrice()+"\nDescription: "+productSellModel.getProductDesc());
                //positive yes
                builder.setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cart
                        final String orderId = UUID.randomUUID().toString();

                        goToCart(productSellModel.getProductName(),
                                productSellModel.getProductPrice(),productSellModel.getProductDesc(),
                                productSellModel.getProductCat(),productSellModel.getSellerId(),productSellModel.getProductId(),
                                productSellModel.getProductImg(),cust_id,orderId);

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

    private void goToCart(String productName, String productPrice, String productDesc, String productCat, String sellerId, String productId, String productImg, String cust_id,String orderId) {
        reference = root.getReference(cust_id+"/MyCart");
        ProductOrderModel productOrderModel = new ProductOrderModel(productName, productPrice,productDesc, productCat,sellerId,productId, productImg,cust_id,orderId);
        reference.child(orderId).setValue(productOrderModel);

        Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView pImg;
        private TextView price, desc;
        LinearLayout product;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            product = itemView.findViewById(R.id.clicked_product);
            pImg = itemView.findViewById(R.id.item_img);
            price = itemView.findViewById(R.id.price);
            desc = itemView.findViewById(R.id.desc);




        }
    }
}
