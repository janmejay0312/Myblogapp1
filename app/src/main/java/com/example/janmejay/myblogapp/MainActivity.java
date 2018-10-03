package com.example.janmejay.myblogapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.LayoutInflater.*;

public class MainActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private CardView cardView;
private RecyclerView.LayoutManager layoutManager;
private DatabaseReference mDatabase;
private Query query;
   private Button share;
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
                 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

                 return new BlogViewHolder(view, getApplicationContext());
             }

             @Override
             protected void onBindViewHolder(@NonNull BlogViewHolder holder, int position, @NonNull final Blog model) {
                 holder.setDescription(model.getDescription());
                 holder.setTitle(model.getTitle());
                 holder.setImage(model.getImage(), getApplicationContext());
                 final ImageView image1=holder.image;
                 holder.itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent=new Intent(v.getContext(),Edit_Activity.class);
                         intent.putExtra("rowId", model.getId());
                         intent.putExtra("title", model.getTitle());
                         intent.putExtra("description", model.getDescription());
                         intent.putExtra("image", model.getImage());
                         v.getContext().startActivity(intent);
                     }
                 });
final Button button=holder.share;

                 button.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(final View v) {
                         PopupMenu popup = new PopupMenu(button.getContext(),button);
                         popup.inflate(R.menu.menu_item1);
                         popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                             @Override
                             public boolean onMenuItemClick(MenuItem item) {
                                 switch (item.getItemId()) {
                                     case R.id.action_edit:

                                         Context context = v.getContext();
                                         Intent intent = new Intent(context, PostActivity.class);
                                         intent.putExtra("rowId", model.getId());
                                         intent.putExtra("title", model.getTitle());
                                         intent.putExtra("description", model.getDescription());
                                         intent.putExtra("image", model.getImage());
                                         context.startActivity(intent);
                                         return true;

                                     case R.id.action_share:
                                         Intent in=new Intent(android.content.Intent.ACTION_SEND);
                                         in.putExtra(Intent.EXTRA_TEXT,model.getDescription());
                                         in.putExtra(Intent.EXTRA_TEXT,model.getImage());
                                         in.setType("text/plain");
                                         v.getContext().startActivity(Intent.createChooser(in,"share via"));
                                         break;
                                     case R.id.action_delete:
                                         mDatabase.child(model.getId()).removeValue();
                                         return true;
                                 }
                                 return false;
                             }

                         });
                         popup.show();
                     }

                 });
             }
             };

recyclerView.setAdapter(firebaseRecyclerAdapter);
         }


    @Override
    protected void onStart() {
        super.onStart();
       firebaseRecyclerAdapter.startListening();
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder  {
     View mView;
    Context context;
        Button share;
        ImageView image;
TextView itemTextView;
        BlogViewHolder(View itemView,Context context) {
            super(itemView);
            mView=itemView;
            this.context=context;
share=itemView.findViewById(R.id.share);
itemTextView=itemView.findViewById(R.id.itemTextView);
image=itemView.findViewById(R.id.image1);
        }

        public void setTitle(String title){
            TextView postTitle=mView.findViewById(R.id.text3);
            postTitle.setText(title);


        }
        public void setDescription(final String description){
            TextView postDesc=mView.findViewById(R.id.text4);
            postDesc.setText(description);

        }
public void setImage(String image,Context context){

    ImageView postImage=mView.findViewById(R.id.image1);
    Glide.with(context).load(image).into(postImage);
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
