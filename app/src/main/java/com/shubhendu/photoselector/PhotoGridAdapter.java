package com.shubhendu.photoselector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoGridAdapter
        extends RecyclerView.Adapter<PhotoGridAdapter.ViewHolder> {


    private Context context;
    private List<PhotoModel> pickedImages = new ArrayList<>();
    private List<PhotoModel> currentAlbumImages;
    private PickerController pickerController;
    private RelativeLayout.LayoutParams params;

    int width;
    int height;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        CheckBox cbPhoto;

        public ViewHolder(View view) {
            super(view);
            imgPhoto = (ImageView) view.findViewById(R.id.img_thumb);
            cbPhoto = (CheckBox) view.findViewById(R.id.cb_photo);
            imgPhoto.setLayoutParams(params);
        }
    }

    public PhotoGridAdapter(Context context, List<PhotoModel> currentAlbumImages,
                            List<PhotoModel> pickedImages, PickerController pickerController) {
        this.context = context;
        this.currentAlbumImages = currentAlbumImages;
        this.pickerController = pickerController;
        this.pickedImages = pickedImages;
        setSize(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_photo_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final PhotoModel currentImage = currentAlbumImages.get(position);
        for (int i = 0; i < pickedImages.size(); i++) {
            if(pickedImages.get(i).getImgPath().equals(currentImage.getImgPath()))
                currentImage.setChecked(pickedImages.get(i).isChecked());
        }
        holder.cbPhoto.setChecked(currentImage.isChecked());
        if (!TextUtils.isEmpty(currentImage.getImgPath())) {
            Glide.with(context)
                .load(currentImage.getImgPath())
                .override(width, height)
                .crossFade()
                .centerCrop()
                .into(holder.imgPhoto);

//            Picasso.with(context)
//                    .load(new File(imgPath))
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .placeholder(R.drawable.ic_picture_loading)
//                    .resize(width, height)
//                    .centerCrop()
//                    .into(holder.imgPhoto);
        }


        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.cbPhoto.isChecked()) {
                    holder.cbPhoto.setChecked(false);
                    currentImage.setChecked(false);
                    holder.imgPhoto.clearColorFilter();
                    pickedImages.remove(currentImage);
                } else {
                    if(pickedImages.size() >= PhotoSelectorConstants.ALBUM_SELECTOR_COUNT) {
                        String message = context.getResources().getString(R.string.max_img_limit_reached, PhotoSelectorConstants.ALBUM_SELECTOR_COUNT);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    holder.cbPhoto.setChecked(true);
                    holder.imgPhoto.setDrawingCacheEnabled(true);
                    holder.imgPhoto.buildDrawingCache();
                    holder.imgPhoto.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    currentImage.setChecked(true);
                    if(!pickedImages.contains(currentImage))
                        pickedImages.add(currentImage);
                }
                pickerController.setActionbarTitle(pickedImages.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return currentAlbumImages.size();
    }


    private void setSize(Context context) {
        width = context.getResources().getDisplayMetrics().widthPixels;
        final float scale = context.getResources().getDisplayMetrics().density;
        float dip = 20.0f;
        int marginPixel = (int) (dip * scale + 0.5f);
        width = width / 2 - marginPixel;
        int thWidth = 50;
        int thHeight = 30;
        height = width * thHeight / thWidth;
        params = new RelativeLayout.LayoutParams(width, height);
    }

}