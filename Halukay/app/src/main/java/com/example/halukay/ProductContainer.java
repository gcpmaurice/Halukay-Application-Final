package com.example.halukay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ProductContainer extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<ProductSellModel> listProduct;

    private ProductAdapter adapter;

    private FirebaseDatabase root;
    private DatabaseReference reference;
    static  String path,category;
    TextView cat;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_container);
        cat = findViewById(R.id.category);
        back = findViewById(R.id.back);



        Intent getData = getIntent();

        path = getData.getStringExtra("PATH");
        category = getData.getStringExtra("CATEGORY");

        cat.setText(category);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductContainer.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


        root = FirebaseDatabase.getInstance();
        reference = root.getReference(path);

        recyclerView = findViewById(R.id.datalist);
        recyclerView.setHasFixedSize(true);

        listProduct = new ArrayList<>();


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (snapshot.hasChildren()){
                        ProductSellModel productSellModel = new ProductSellModel();

                        productSellModel.setProductName(Objects.requireNonNull(dataSnapshot.child("productName").getValue()).toString());
                        productSellModel.setProductPrice(Objects.requireNonNull(dataSnapshot.child("productPrice").getValue()).toString());
                        productSellModel.setProductDesc(Objects.requireNonNull(dataSnapshot.child("productDesc").getValue()).toString());
                        productSellModel.setProductCat(Objects.requireNonNull(dataSnapshot.child("productCat").getValue()).toString());
                        productSellModel.setSellerId(Objects.requireNonNull(dataSnapshot.child("sellerId").getValue()).toString());
                        productSellModel.setProductId(Objects.requireNonNull(dataSnapshot.child("productId").getValue()).toString());
                        productSellModel.setProductImg(Objects.requireNonNull(dataSnapshot.child("productImg").getValue()).toString());
                        listProduct.add(productSellModel);

                    }else{
                        Toast.makeText(ProductContainer.this, "No Product in this category", Toast.LENGTH_SHORT).show();
                    }

                }
                adapter = new ProductAdapter(ProductContainer.this,listProduct);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductContainer.this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductContainer.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}