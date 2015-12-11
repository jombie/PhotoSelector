package com.shubhendu.photoselector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
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

    private ArrayList<PhotoModel> pickedImages;
    private List<PhotoModel> allAlbumImages;
    private PickerController pickerController;
    private String combinedAlbumName;
    private PhotoSelectorDomain photoSelector;
    private ImageCaptureManager captureManager;
    private int SCROLL_THRESHOLD = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selector);
        albums = new ArrayList<>();
        rvImages = (RecyclerView) findViewById(R.id.rv_images);
        selectAlbumBtn = (Button) findViewById(R.id.select_album_btn);
        takePictureBtn = (Button)  findViewById(R.id.take_picture_btn);
        combinedAlbumName = getResources().getString(R.string.combined_album_name);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        Toolbar toolbarButtom = (Toolbar) findViewById(R.id.toolbar_bottom);
        setSupportActionBar(toolbarTop);
        toolbarTop.setBackgroundColor(PhotoSelectorConstants.ACTION_BAR_COLOR);
        toolbarButtom.setBackgroundColor(PhotoSelectorConstants.ACTION_BAR_COLOR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColor(this);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        photoSelector.updatePhotos(this);

        ArrayList<String> previousPaths = getIntent().getStringArrayListExtra(PhotoSelectorConstants.PREVIOUS_SELECTED_PATHS);
        if (previousPaths != null) {
            for (String previousPath: previousPaths) {
                pickedImages.add(new PhotoModel(previousPath, true));
            }
        }
        pickerController.setActionbarTitle(pickedImages.size());
        captureManager = new ImageCaptureManager(this);
        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("CCC", "On camera click listener");
                    Intent intent = captureManager.dispatchTakePictureIntent();
                    Log.d("Camera", "Intent " + intent);
                    startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Camera", "Error " + e.getMessage());
                }
            }
        });

        rvImages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Log.d(">>> Picker >>>", "dy = " + dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    Glide.with(PhotoSelectorActivity.this).pauseRequests();
                } else {
                    Glide.with(PhotoSelectorActivity.this).resumeRequests();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(PhotoSelectorActivity.this).resumeRequests();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            captureManager.galleryAddPic();
            allAlbumImages.add(0, new PhotoModel(captureManager.getCurrentPhotoPath(), true));
            photoGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_ok) {
            ArrayList<String> path = new ArrayList<>();
            for (int i = 0; i < pickedImages.size(); i++) {
                path.add(pickedImages.get(i).getImgPath());
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra(PhotoSelectorConstants.PREVIOUS_SELECTED_PATHS, path);
            setResult(RESULT_OK, intent);
            finish();
        } else if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
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
    public void onPhotoLoaded(List<PhotoModel> allAlbumImages) {
        this.allAlbumImages = allAlbumImages;
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
        photoSelector.updatePhotos(albumName, currentAlbum.getAlbumId(), PhotoSelectorActivity.this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(PhotoSelectorConstants.STATUS_BAR_COLOR);
    }
}
