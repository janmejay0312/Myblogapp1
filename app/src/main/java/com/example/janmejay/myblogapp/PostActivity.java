package com.example.janmejay.myblogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {
 private ImageButton addImage;
 private Button submit;
 private EditText title;
 private EditText description;
 private Uri uri=null;
 public static final int GALLERY_REQUEST=1;
 private DatabaseReference mDatabase;
 private StorageReference mstorage;
 private ProgressDialog progressBar;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        addImage=findViewById(R.id.imageview);
        submit=findViewById(R.id.button);
        title=findViewById(R.id.editText);
        description=findViewById(R.id.editText2);
        mstorage= FirebaseStorage.getInstance().getReference();
        mDatabase=FirebaseDatabase.getInstance().getReference();
        progressBar=new ProgressDialog(this);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
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
        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            uri=data.getData();
            addImage.setImageURI(uri);
        }
    }
    private void startPosting()
    { progressBar.setMessage("Uploading...");
    progressBar.show();
        final String s=title.getText().toString().trim();
        final String q=description.getText().toString().trim();
        if(!TextUtils.isEmpty(s)&&!TextUtils.isEmpty(q)&&uri!=null){
StorageReference storageReference=mstorage.child("Photos").child(uri.getLastPathSegment());
storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Uri downloadurl=taskSnapshot.getUploadSessionUri();
        DatabaseReference databaseReference=mDatabase.push();
        databaseReference.child("title").setValue(s);
        databaseReference.child("description").setValue(q);
        databaseReference.child("image").setValue(downloadurl.toString());
      progressBar.dismiss();
      startActivity(new Intent(PostActivity.this,MainActivity.class));
    }
});
        }
    }
}
