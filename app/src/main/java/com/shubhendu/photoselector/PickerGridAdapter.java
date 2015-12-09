package com.shubhendu.photoselector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PickerGridAdapter
        extends RecyclerView.Adapter<PickerGridAdapter.ViewHolder> {


    private Context context;
    private ArrayList<PhotoModel> pickedImages = new ArrayList<>();
    private PhotoModel[] imageBeans;
    private PickerController pickerController;

    int width;
    int height;
    RelativeLayout.LayoutParams params;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView txtPickCount;

        public ViewHolder(View view) {
            super(view);
            imgPhoto = (ImageView) view.findViewById(R.id.img_thumb);
            txtPickCount = (TextView) view.findViewById(R.id.txt_pick_count);

            imgPhoto.setLayoutParams(params);
            txtPickCount.setLayoutParams(params);
        }
    }

    public PickerGridAdapter(Context context, PhotoModel[] imageBeans, ArrayList<PhotoModel> pickedImages, PickerController pickerController) {
        this.context = context;
        this.imageBeans = imageBeans;
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

        final PhotoModel imageBean = imageBeans[position];
        final String imgPath = imageBean.getOriginalPath();

        if (!imageBean.isInit()) {
            imageBean.setIsInit(true);
            for (int i = 0; i < pickedImageBeans.size(); i++) {
                if (imgPath.equals(pickedImageBeans.get(i).getImgPath())) {
                    imageBean.setImgOrder(i + 1);
                    pickedImageBeans.get(i).setImgPosition(position);
                    break;
                }
            }
        }


        if (imageBean.getImgOrder() != -1) {
            holder.txtPickCount.setVisibility(View.VISIBLE);
            if (Define.ALBUM_PICKER_COUNT == 1)
                holder.txtPickCount.setText("");
            else
                holder.txtPickCount.setText(String.valueOf(imageBean.getImgOrder()));
        } else
            holder.txtPickCount.setVisibility(View.GONE);


        if (imgPath != null && !imgPath.equals("")) {
            Glide
                    .with(context)
                    .load(imgPath)
//                        .thumbnail(0.7f)
//                        .placeholder(R.drawable.loading_img)
                    .override(width, height)
                    .crossFade()
                    .centerCrop()
                    .into(holder.imgPhoto);
        }


        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txtPickCount.getVisibility() == View.GONE &&
                        Define.ALBUM_PICKER_COUNT > pickedImageBeans.size()) {
                    holder.txtPickCount.setVisibility(View.VISIBLE);
                    pickedImageBeans.add(new PickedImageBean(pickedImageBeans.size() + 1, imgPath, position));

                    if (Define.ALBUM_PICKER_COUNT == 1)
                        holder.txtPickCount.setText("");
                    else
                        holder.txtPickCount.setText(String.valueOf(pickedImageBeans.size()));

                    imageBean.setImgOrder(pickedImageBeans.size());
                    pickerController.setActionbarTitle(pickedImageBeans.size());
                } else if (holder.txtPickCount.getVisibility() == View.VISIBLE) {
                    pickerController.setRecyclerViewClickable(false);
                    pickedImageBeans.remove(imageBean.getImgOrder() - 1);
                    if (Define.ALBUM_PICKER_COUNT != 1)
                        setOrder(Integer.valueOf(holder.txtPickCount.getText().toString()) - 1);
                    else
                        setOrder(0);
                    imageBean.setImgOrder(-1);
                    holder.txtPickCount.setVisibility(View.GONE);
                    pickerController.setActionbarTitle(pickedImageBeans.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageBeans.length;
    }


    private void setOrder(int removePosition) {
        for (int i = removePosition; i < pickedImageBeans.size(); i++) {
            if (pickedImageBeans.get(i).getImgPosition() != -1) {
                imageBeans[pickedImageBeans.get(i).getImgPosition()]
                        .setImgOrder(i + 1);
                notifyItemChanged(pickedImageBeans.get(i).getImgPosition());
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pickerController.setRecyclerViewClickable(true);
            }
        }, 500);

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