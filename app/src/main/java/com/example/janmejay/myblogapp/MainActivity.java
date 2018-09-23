package com.example.janmejay.myblogapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import static android.view.LayoutInflater.*;

public class MainActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private RecyclerView.LayoutManager layoutManager;
private DatabaseReference mDatabase;
private Query query;
private   FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase=FirebaseDatabase.getInstance().getReference();
        recyclerView=findViewById(R.id.recyclerview);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(mDatabase, Blog.class)
                        .build();
         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(options) {

            @NonNull
            @Override
            public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
                return new BlogViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BlogViewHolder holder, int position, @NonNull Blog model) {
                holder.setDescription(model.getDescription());
                holder.setTitle(model.getTitle());
                holder.setImage(model.getImage(), getApplicationContext());

            }


        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
       firebaseRecyclerAdapter.startListening();
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{
View mView;
        private BlogViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setTitle(String title){
            TextView postTitle=mView.findViewById(R.id.text3);
            postTitle.setText(title);
        }
        public void setDescription(String description){
            TextView postDesc=mView.findViewById(R.id.text4);
            postDesc.setText(description);
        }
public void setImage(String image,Context context){

    ImageView postImage=mView.findViewById(R.id.image1);
     Uri file=Uri.parse(image);
    Glide.with(context).load(file).into(postImage);
}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add){
            Intent intent=new Intent(MainActivity.this,PostActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
