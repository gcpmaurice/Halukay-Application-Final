package com.example.halukay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterCustomer extends AppCompatActivity {
    private EditText msignupemail,msignuppassword,mname,mphone,maddress;
    private Button msignup;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseDatabase root = FirebaseDatabase.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        //getSupportActionBar().hide();

        msignupemail=findViewById(R.id.email_sign_up);
        msignuppassword=findViewById(R.id.signup_password_sign_up);
        mname = findViewById(R.id.name_sign_up);
        mphone = findViewById(R.id.phone_sign_up);
        maddress = findViewById(R.id.address_sign_up);
        msignup=findViewById(R.id.signUpBtn);

        firebaseAuth= FirebaseAuth.getInstance();

        msignup.setOnClickListener(view -> {
            String mail=msignupemail.getText().toString().trim();
            String password=msignuppassword.getText().toString().trim();
            String phone=mphone.getText().toString();
            String name=mname.getText().toString();
            String address=maddress.getText().toString();
           
            if(mail.isEmpty()|| password.isEmpty() || address.isEmpty() || name.isEmpty() || phone.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"All Fields are Required",Toast.LENGTH_SHORT).show();
            }
            else if(password.length()<6)
            {
                Toast.makeText(getApplicationContext(),"Password Should Greater Than 6 Characters",Toast.LENGTH_SHORT).show();
            }
            else
            {

                firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();
                            UserModel userModel = new UserModel(name,mail,address,phone,uid);
                            reference = root.getReference(uid);
                            reference.child("info").setValue(userModel);
                            sendEmailVerification();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Failed To Register", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }

        });


    }

    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Verification Email is Sent,Verify and Log In Again",Toast.LENGTH_SHORT).show();

                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(RegisterCustomer.this,LoginActivity.class));
                }
            });
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Failed To Send Verification Email",Toast.LENGTH_SHORT).show();
        }

    }

    public void SignInText(View view) {
        Intent intent=new Intent(RegisterCustomer.this,LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}