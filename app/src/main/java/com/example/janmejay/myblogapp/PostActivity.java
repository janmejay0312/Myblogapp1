package com.example.janmejay.myblogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {
    private ImageButton addImage;
    private Button submit,update;
    private EditText title;
    private EditText description;
    private Uri uri = null;
    private String string;
    private Intent intent;

    public static final int GALLERY_REQUEST = 1;
    private DatabaseReference mDatabase,rdatabase;
    private StorageReference mstorage;
    private ProgressDialog progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        addImage = findViewById(R.id.imageview);
        submit = findViewById(R.id.button);
        update=findViewById(R.id.update);
        title = findViewById(R.id.editText);
        description = findViewById(R.id.editText2);
        mstorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
       intent = this.getIntent();
        if (intent != null) {
            String Des = intent.getStringExtra("description");
            String mTitle = intent.getStringExtra("title");
            String image = intent.getStringExtra("image");
            Glide.with(this).load(image).into(addImage);
            description.setText(Des);
            title.setText(mTitle);
        }
        progressBar = new ProgressDialog(this);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startPosting();
                }

            });


        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK&&data.getData()!=null) {
            uri = data.getData();
            addImage.setImageURI(uri);
        }
    }

    private void startPosting() {
        progressBar.setMessage("Uploading...");
        progressBar.show();
        final String s = title.getText().toString().trim();
        final String q = description.getText().toString().trim();
        if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(q) && uri != null) {
            final StorageReference storageReference = mstorage.child("Photos").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {


                    databaseReference = mDatabase.push();
                    databaseReference.child("title").setValue(s);
                    databaseReference.child("description").setValue(q);
                    progressBar.dismiss();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("image").setValue(uri.toString());
                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                        }
                    });

                }
            });
        }

             

    }
}
