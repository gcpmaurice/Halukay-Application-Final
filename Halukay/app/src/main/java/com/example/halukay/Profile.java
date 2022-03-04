package com.example.halukay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

public class Profile extends AppCompatActivity {
    TextView update,change,toDeliver,myItem;
    ImageView back;
    EditText nameEt,addrEt,phoneEt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Button logout;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        update = findViewById(R.id.update);
        change = findViewById(R.id.changePass);
        toDeliver = findViewById(R.id.toDeliver);
        myItem = findViewById(R.id.myItem);
        nameEt = findViewById(R.id.name);
        addrEt = findViewById(R.id.address);
        phoneEt = findViewById(R.id.phone);
        back = findViewById(R.id.back);
        logout = findViewById(R.id.logout);

        String uid = firebaseAuth.getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference(uid);



        Intent getData = getIntent();

        String myName = getData.getStringExtra("NAME");
        String myAddr = getData.getStringExtra("ADDRESS");
        String myPhone = getData.getStringExtra("PHONE");

        nameEt.setText(myName);
        addrEt.setText(myAddr);
        phoneEt.setText(myPhone);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = nameEt.getText().toString();
                String newAddr = addrEt.getText().toString();
                String newPhone = phoneEt.getText().toString();

                if (newName.trim().length()==0 || newAddr.trim().length()==0 || newPhone.trim().length()==0){
                    Toast.makeText(Profile.this, "Make sure to input valid info", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String,Object> update = new HashMap<>();
                    update.put("name", newName);
                    update.put("address",newAddr);
                    update.put("phone",newPhone);
                    reference.child("info").updateChildren(update);
                    Toast.makeText(Profile.this, "Info Updated", Toast.LENGTH_SHORT).show();
                    recreate();
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //content here
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Change Password");
                passwordResetDialog.setMessage("Enter your Email");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Proceed", (dialog, which) -> {
                    //extract emailand sent rest link
                    String mail = resetMail.getText().toString();
                    firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText( Profile.this, "Check your email.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText( Profile.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                });

                passwordResetDialog.setNegativeButton("Cancel", (dialog, which) -> {
                    //close dialog
                });
                passwordResetDialog.create().show();


            }
        });

        toDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,ToDeliver.class);
                startActivity(intent);

            }
        });
        myItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,MyItem.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(Profile.this,LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}