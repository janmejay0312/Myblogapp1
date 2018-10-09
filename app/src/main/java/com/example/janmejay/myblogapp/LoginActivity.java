package com.example.janmejay.myblogapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
private EditText fEmail,fPassword;
Button newUser;
       private Button mLogin;
        private FirebaseAuth firebaseAuth;
      private   DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fEmail=findViewById(R.id.Email);
        fPassword=findViewById(R.id.password);
        newUser=findViewById(R.id.new_user);
        mLogin=findViewById(R.id.Login);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("User");
mLogin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        checkLogin();
    }
});
newUser.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent in=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(in);
    }
});
    }

    private void checkLogin() {
        String s=fEmail.getText().toString().trim();
        String p=fPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(s)&&!TextUtils.isEmpty(p))
        {
            firebaseAuth.signInWithEmailAndPassword(s,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
checkUserExistence();
                    }
                    else
                    {
   Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }

    private void checkUserExistence() {
      final   String q=firebaseAuth.getCurrentUser().getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(q)){
    Intent intent =new Intent(LoginActivity.this,MainActivity.class);
     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     startActivity(intent);
                }
                else
                    Toast.makeText(LoginActivity.this,"does not exist",Toast.LENGTH_LONG);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
