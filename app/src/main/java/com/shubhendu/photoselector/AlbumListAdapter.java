package com.shubhendu.photoselector;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumListAdapter extends BaseAdapter {

    private Context context;
    private List<AlbumModel> albumList;
    private List<String> thumbList = new ArrayList<String>();
    private String thumbNailPath;
    private LayoutInflater mLayoutInflater;

    public AlbumListAdapter(Context context, List<AlbumModel> albumList) {
        this.context = context;
        this.albumList = albumList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return albumList.size();
    }


    @Override
    public AlbumModel getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return albumList.get(position).hashCode();
    }


    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_album_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(albumList.get(position));

        return convertView;
    }

    public class ViewHolder {

        private ImageView imgAlbum;
        private ImageView selectTickIcon;
        private TextView txtAlbum;
        private TextView txtAlbumCount;
        private RelativeLayout areaAlbum;


        public ViewHolder(View view) {
            imgAlbum = (ImageView) view.findViewById(R.id.img_album);
            selectTickIcon = (ImageView) view.findViewById(R.id.select_tick_icon);
            txtAlbum = (TextView) view.findViewById(R.id.txt_album);
            txtAlbumCount = (TextView) view.findViewById(R.id.txt_album_count);
        }

        public void bindData(AlbumModel albumModel) {
            if (context instanceof Activity && ((Activity) context).isFinishing()) {
                return;
            }
            if(!TextUtils.isEmpty(albumModel.getThumbNailPath())) {
                Log.d("AAA", albumModel.getThumbNailPath());
//                Picasso.with(context)
//                        .load(new File(albumModel.getThumbNailPath()))
//                        .placeholder(R.drawable.ic_picture_loading)
//                        .into(imgAlbum);
                Glide.with(context)
                    .load(albumModel.getThumbNailPath())
                    .asBitmap()
                    .override(PhotoSelectorConstants.ALBUM_THUMBNAIL_SIZE, PhotoSelectorConstants.ALBUM_THUMBNAIL_SIZE)
                    .placeholder(R.drawable.ic_picture_loading)
                    .into(imgAlbum);
            }
            txtAlbum.setText(albumModel.getAlbumName());
            txtAlbumCount.setText(Integer.toString(albumModel.getAlbumTotalImages()));
            if(albumModel.isAlbumSelected()) {
                selectTickIcon.setVisibility(View.VISIBLE);
            } else {
                selectTickIcon.setVisibility(View.GONE);
            }
        }
    }
}
