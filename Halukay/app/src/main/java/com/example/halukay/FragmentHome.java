package com.example.halukay;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class FragmentHome extends Fragment {
    View v;
    CardView shoe,top,bottom,jacket;
    public static String pathProduct,category;


    public FragmentHome(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.home_fragment,container,false);

        shoe = v.findViewById(R.id.shoes);
        top = v.findViewById(R.id.tops);
        bottom = v.findViewById(R.id.bottoms);
        jacket = v.findViewById(R.id.jackets);


        shoe.setOnClickListener(view -> {
            pathProduct = "Shoe";
            category = "Shoes";
            Intent intent = new Intent(getActivity(),ProductContainer.class);
            intent.putExtra("PATH",pathProduct);
            intent.putExtra("CATEGORY",category);
            startActivity(intent);


        });
        top.setOnClickListener(view -> {
            pathProduct = "Top";
            category = "Tops";
            Intent intent = new Intent(getActivity(),ProductContainer.class);
            intent.putExtra("PATH",pathProduct);
            intent.putExtra("CATEGORY",category);
            startActivity(intent);

        });
        bottom.setOnClickListener(view -> {
            pathProduct = "Bottom";
            category = "Bottoms";
            Intent intent = new Intent(getActivity(),ProductContainer.class);
            intent.putExtra("PATH",pathProduct);
            intent.putExtra("CATEGORY",category);
            startActivity(intent);

        });
        jacket.setOnClickListener(view -> {
            pathProduct = "Jacket";
            category = "Jackets";
            Intent intent = new Intent(getActivity(),ProductContainer.class);
            intent.putExtra("PATH",pathProduct);
            intent.putExtra("CATEGORY",category);
            startActivity(intent);
        });

        return v;
    }
}
