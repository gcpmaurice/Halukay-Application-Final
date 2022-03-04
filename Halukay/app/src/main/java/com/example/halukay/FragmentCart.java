package com.example.halukay;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentCart extends Fragment{
    View v;
    private RecyclerView myRecyclerView;
    private ArrayList<ProductOrderModel> listCart;
    private ArrayList<PurchaseModel> orderList;

    private FirebaseDatabase root;
    private DatabaseReference reference;
    CartAdapter cartAdapter;
    StringBuffer sb = null;
    CheckBox cod;

    Button purchase;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public FragmentCart(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.cart_fragment,container,false);
        myRecyclerView = v.findViewById(R.id.recyclerview_cart);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        purchase = v.findViewById(R.id.purchaseBtn);
        cod = v.findViewById(R.id.cash_on_d);

        root = FirebaseDatabase.getInstance();
        String uid = fAuth.getCurrentUser().getUid();
        reference = root.getReference(uid+"/MyCart");

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cod.isChecked()){
                    purchase.setEnabled(true);
                }else {
                    purchase.setEnabled(false);
                }
            }
        });





        listCart = new ArrayList<>();
        orderList = new ArrayList<>();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (snapshot.hasChildren()){
                        ProductOrderModel productOrderModel = new ProductOrderModel();

                        productOrderModel.setProductName(Objects.requireNonNull(dataSnapshot.child("productName").getValue()).toString());
                        productOrderModel.setProductPrice(Objects.requireNonNull(dataSnapshot.child("productPrice").getValue()).toString());
                        productOrderModel.setProductDesc(Objects.requireNonNull(dataSnapshot.child("productDesc").getValue()).toString());
                        productOrderModel.setProductCat(Objects.requireNonNull(dataSnapshot.child("productCat").getValue()).toString());
                        productOrderModel.setSellerId(Objects.requireNonNull(dataSnapshot.child("sellerId").getValue()).toString());
                        productOrderModel.setProductId(Objects.requireNonNull(dataSnapshot.child("productId").getValue()).toString());
                        productOrderModel.setProductImg(Objects.requireNonNull(dataSnapshot.child("productImg").getValue()).toString());
                        productOrderModel.setCustomerId(Objects.requireNonNull(dataSnapshot.child("customerId").getValue()).toString());
                        productOrderModel.setOrderId(Objects.requireNonNull(dataSnapshot.child("orderId").getValue()).toString());
                        listCart.add(productOrderModel);

                    }else{
                        Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                    }

                }

                cartAdapter = new CartAdapter(getContext(),listCart);
                myRecyclerView.setAdapter(cartAdapter);

                cartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "DATABASE ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb = new StringBuffer();
               String pName ="";
                String pPrice ="" ,pDesc = "",pId="",pImg="",pOId="",pCId="",pSId="",pCat="";


                for (ProductOrderModel order: cartAdapter.checkedOrder){

                     pName = order.getProductName();
                    pPrice = order.getProductPrice();
                    pDesc = order.getProductDesc();
                    pId = order.getProductId();
                    pImg = order.getProductImg();
                    pOId = order.getOrderId();
                    pCId = order.getCustomerId();
                    pSId = order.getSellerId();
                    pCat = order.getProductCat();



                }

                if (cartAdapter.checkedOrder.size()>0){
                    PurchaseModel purchaseModel = new PurchaseModel(pName,pPrice,pDesc,pId,pImg,pOId,pCId,pSId,"To Receive",pCat);
                    reference = root.getReference(uid+"/MyPurchase");
                    reference.child(pOId).setValue(purchaseModel);
                    toDeliver(pName,pPrice,pDesc,pId,pImg,pOId,pCId,pSId,"To Receive",pCat);
                    Toast.makeText(getContext(), "See status in purchase dashboard", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Please select", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void toDeliver(String pName, String pPrice, String pDesc, String pId, String pImg, String pOId, String pCId, String pSId, String to_receive,String pCat) {
        reference = root.getReference(pSId+"/ToDeliver");
        PurchaseModel purchaseModel = new PurchaseModel(pName,pPrice,pDesc,pId,pImg,pOId,pCId,pSId,to_receive,pCat);
        reference.child(pOId).setValue(purchaseModel);
        goRemove(pCId,pOId);
    }

    private void goRemove(String pCId, String pOId) {
       reference = root.getReference(pCId+"/MyCart");
        reference.child(pOId).removeValue();
        Intent intent = new Intent(getContext(),MyPurchase.class);
        startActivity(intent);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
