package com.example.halukay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ToDeliver extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<PurchaseModel> listProduct;

    private DeliverAdapter adapter;

    private FirebaseDatabase root;
    private DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_deliver);

        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();

        root = FirebaseDatabase.getInstance();
        reference = root.getReference(uid+"/ToDeliver");

        recyclerView = findViewById(R.id.datalist);
        recyclerView.setHasFixedSize(true);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDeliver.this,Profile.class);
                startActivity(intent);
                finish();

            }
        });

        listProduct = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (snapshot.hasChildren()){
                        PurchaseModel purchaseModel = new PurchaseModel();

                        purchaseModel.setProductName(Objects.requireNonNull(dataSnapshot.child("productName").getValue()).toString());
                        purchaseModel.setProductPrice(Objects.requireNonNull(dataSnapshot.child("productPrice").getValue()).toString());
                        purchaseModel.setProductDesc(Objects.requireNonNull(dataSnapshot.child("productDesc").getValue()).toString());
                        purchaseModel.setSellerId(Objects.requireNonNull(dataSnapshot.child("sellerId").getValue()).toString());
                        purchaseModel.setProductId(Objects.requireNonNull(dataSnapshot.child("productId").getValue()).toString());
                        purchaseModel.setProductImg(Objects.requireNonNull(dataSnapshot.child("productImg").getValue()).toString());
                        purchaseModel.setOrderId(Objects.requireNonNull(dataSnapshot.child("orderId").getValue()).toString());
                        purchaseModel.setCustomerId(Objects.requireNonNull(dataSnapshot.child("customerId").getValue()).toString());
                        purchaseModel.setStatus(Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString());
                        listProduct.add(purchaseModel);

                    }else{
                        Toast.makeText(ToDeliver.this, "No Orders yet", Toast.LENGTH_SHORT).show();
                    }

                }
                adapter = new DeliverAdapter(ToDeliver.this,listProduct);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ToDeliver.this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

}