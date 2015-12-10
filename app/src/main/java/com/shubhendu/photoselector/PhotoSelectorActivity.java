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
    private PhotoGridAdapter photoGridAdapter;

    private List<String> pickedImages;
    private PickerController pickerController;
    private String combinedAlbumName;
    private PhotoSelectorDomain photoSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selector);
        albums = new ArrayList<>();
        rvImages = (RecyclerView) findViewById(R.id.rv_images);
        selectAlbumBtn = (Button) findViewById(R.id.select_album_btn);
        takePictureBtn = (Button)  findViewById(R.id.take_picture_btn);
        combinedAlbumName = getResources().getString(R.string.combined_album_name);
        getSupportActionBar().setTitle(combinedAlbumName);

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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, PhotoSelectorConstants.PHOTO_SPAN_COUNT);
        rvImages.setLayoutManager(gridLayoutManager);
        rvImages.setAdapter(null);
        rvImages.setItemAnimator(new DefaultItemAnimator());
        pickedImages = new ArrayList<>();
        pickerController = new PickerController(this, getSupportActionBar(), rvImages);

        photoSelector = new PhotoSelectorDomain(this);
        photoSelector.updateAlbum(this);
        photoSelector.updatePhotos(null, this);

        ArrayList<String> previousPaths = getIntent().getStringArrayListExtra(PhotoSelectorConstants.PREVIOUS_SELECTED_PATHS);
        if (previousPaths != null) {
            pickedImages.addAll(previousPaths);
        }
        pickerController.setActionbarTitle(pickedImages.size());
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
    public void onPhotoLoaded(List<String> allAlbumImages) {
        photoGridAdapter = new PhotoGridAdapter(PhotoSelectorActivity.this, allAlbumImages, pickedImages, pickerController);
        rvImages.setAdapter(photoGridAdapter);
        photoGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlbumModel currentAlbum = albums.get(position);
        for (int i = 0; i < parent.getCount(); i++) {
            AlbumModel otherAlbum = (AlbumModel) parent.getItemAtPosition(i);
            otherAlbum.setAlbumSelected((i == position));
        }
        albumListAdapter.notifyDataSetChanged();
        listPopupWindow.dismiss();
        String albumName = currentAlbum.getAlbumName();
        getSupportActionBar().setTitle(albumName);
        albumName = albumName.equals(combinedAlbumName) ? null : albumName;
        photoSelector.updatePhotos(albumName, PhotoSelectorActivity.this);
    }
}
