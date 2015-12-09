package com.shubhendu.photoselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class PhotoSelectorActivity extends AppCompatActivity
        implements PhotoSelectorDomain.AlbumListener,
        PhotoSelectorDomain.PhotoListener,
        AdapterView.OnItemClickListener {

    private ListPopupWindow listPopupWindow;
    private List<AlbumModel> albums;
    private RecyclerView rvImages;
    private Button selectAlbumBtn;
    private Button takePictureBtn;
    private AlbumListAdapter albumListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selector);
        albums = new ArrayList<>();
        rvImages = (RecyclerView) findViewById(R.id.rv_images);
        selectAlbumBtn = (Button) findViewById(R.id.select_album_btn);
        takePictureBtn = (Button)  findViewById(R.id.take_picture_btn);

        PhotoSelectorDomain photoSelector = new PhotoSelectorDomain(this);
        photoSelector.updateAlbum(this);

        albumListAdapter = new AlbumListAdapter(this, albums);
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        listPopupWindow.setAnchorView(selectAlbumBtn);
        listPopupWindow.setAdapter(albumListAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setDropDownGravity(Gravity.BOTTOM);
        listPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
        listPopupWindow.setOnItemClickListener(this);

        selectAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listPopupWindow.isShowing()) {
                    listPopupWindow.dismiss();
                } else if (!PhotoSelectorActivity.this.isFinishing()) {
                    //listPopupWindow.setHeight(Math.round(get * 0.8f));
                    listPopupWindow.show();
                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, PickerController.PHOTO_SPAN_COUNT);
        rvImages.setLayoutManager(gridLayoutManager);
        rvImages.setAdapter(null);
        rvImages.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onAlbumLoaded(List<AlbumModel> currentAlbums) {
        Log.d("AAA", "Loaded albums " + albums.size());
        albums.clear();
        albums.addAll(currentAlbums);
        albumListAdapter = new AlbumListAdapter(PhotoSelectorActivity.this, albums);
        listPopupWindow.setAdapter(albumListAdapter);
        albumListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listPopupWindow.dismiss();
        AlbumModel albumModel = albums.get(position);
        getSupportActionBar().setTitle(albumModel.getAlbumName());

//        btSwitchDirectory.setText(albumModel.getAlbumName());
//        photoGridAdapter.setCurrentDirectoryIndex(position);
//        photoGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPhotoLoaded(List<PhotoModel> photos) {
        Log.d("AAA", "Loaded Photos " + photos.size());

    }
}
