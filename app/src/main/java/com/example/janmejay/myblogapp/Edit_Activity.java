package com.example.janmejay.myblogapp;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Edit_Activity extends AppCompatActivity {
    private TextView gData,mDes;
    private ImageView gImage;
private DatabaseReference databaseRef,rDatabase;
private static final int GALLERY_INTENT=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_);
        gData = findViewById(R.id.textView);
        mDes = findViewById(R.id.textView2);
        gImage=findViewById(R.id.imageView);
        databaseRef=FirebaseDatabase.getInstance().getReference();


        Bundle bundle=getIntent().getExtras();
        String Des = bundle.getString("description");
        String title=bundle.getString("title");
        String image=bundle.getString("image");
        Glide.with(this).load(image).into(gImage);
        gData.setText(Des);
        mDes.setText(title);

    }
}
