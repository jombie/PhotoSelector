package com.shubhendu.photoselector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> path = new ArrayList<>();
    ImageView imgMain;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgMain = (ImageView) findViewById(R.id.main_selected_image);
        recyclerView = (RecyclerView) findViewById(R.id.rv_selected_images);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mainAdapter = new MainAdapter(this, path);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);

        switch (requestCode) {
            case PhotoSelectorConstants.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    path = imageData.getStringArrayListExtra(PhotoSelectorConstants.PREVIOUS_SELECTED_PATHS);
                    mainAdapter.changePath(path);
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_plus) {

            PhotoSelector.with(MainActivity.this)
                    .setMaxSelectImages(10)
                    .setPreviousSelectedPaths(path)
                    .startAlbum();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
        Context context;
        ArrayList<String> imagePaths;

        MainAdapter(Context context, ArrayList<String> imagePaths) {
            this.context = context;
            this.imagePaths = imagePaths;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_main_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final String imagePath = imagePaths.get(position);
            Glide.with(context).load(imagePath).centerCrop().into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(context).load(imagePath).fitCenter().into(imgMain);
                }
            });
        }

        public void changePath(ArrayList<String> imagePaths) {
            this.imagePaths = imagePaths;
            Glide.with(context).load(imagePaths.get(0)).fitCenter().into(imgMain);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return imagePaths.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.img_item);
            }
        }
    }
}
