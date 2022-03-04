package com.example.halukay;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class FragmentSell extends Fragment {
    View v;
    EditText p_name,p_price,p_desc;
    Spinner p_cat;
    Button sellItem;
    ImageView p_img;

    public static Uri imageUri;

    private StorageReference storageReference;
    private FirebaseStorage storage;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth fAuth;

    public FragmentSell(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sell_fragment,container,false);
        p_name = v.findViewById(R.id.product_name);
        p_desc = v.findViewById(R.id.product_desc);
        p_cat = v.findViewById(R.id.product_cat);
        sellItem = v.findViewById(R.id.sellBtn);
        p_img = v.findViewById(R.id.product_image);
        p_price = v.findViewById(R.id.product_price);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        root = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();


        p_img.setOnClickListener(view -> choosePicture());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        p_cat.setAdapter(adapter);



        sellItem.setOnClickListener(view -> {
            String pName = p_name.getText().toString();
            String pPrice = p_price.getText().toString();
            String pDesc = p_desc.getText().toString();
            String pCat = p_cat.getSelectedItem().toString();


            if(imageUri!=null && !TextUtils.isEmpty(pName) && !TextUtils.isEmpty(pDesc)){
                uploadPicture(imageUri,pName,pPrice,pDesc,pCat);
                p_name.getText().clear();
                p_price.getText().clear();
                p_desc.getText().clear();
                imageUri = null;
                p_img.setImageResource(R.drawable.add_img);


            }else {
                Toast.makeText(getContext(),"Please Select Image and fill required fields",Toast.LENGTH_SHORT).show();
            }


        });




        return v;
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            p_img.setImageURI(imageUri);
        }
    }

    private void uploadPicture(Uri uri, String pName,String pPrice,String pDesc,String pCat) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();

// Create a reference to 'images''
        String myId = fAuth.getCurrentUser().getUid();
        final String productId = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("productImages/"+productId+"."+getfileExtension(uri));
        reference = root.getReference(myId+"/myItem");
        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri1) {
                    progressDialog.dismiss();
                    ProductSellModel productSellModel = new ProductSellModel(pName,pPrice,pDesc,pCat,myId,productId, uri1.toString());
                    reference.child(productId).setValue(productSellModel);

                    gotoPublic(pName,pPrice,pDesc,pCat,myId,productId, uri1.toString());



                }
            });


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Snackbar.make(getActivity().findViewById(android.R.id.content),"Error While Saving", Snackbar.LENGTH_SHORT).show();
            }
        })
                .addOnProgressListener(snapshot -> {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressDialog.setMessage("Progress: "+progressPercent +"%");
                    Snackbar.make(getActivity().findViewById(android.R.id.content),"Saved", Snackbar.LENGTH_SHORT).show();


                });
    }

    private void gotoPublic(String pName,String pPrice, String pDesc, String pCat,String myId, String productId, String uri) {
        reference = root.getReference(pCat);
        ProductSellModel productSellModel = new ProductSellModel(pName,pPrice,pDesc,pCat,myId,productId,uri);
        reference.child(productId).setValue(productSellModel);

        Toast.makeText(getContext(), "Product is Successfully Added", Toast.LENGTH_SHORT).show();

    }

    private String getfileExtension(Uri mUri){
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
