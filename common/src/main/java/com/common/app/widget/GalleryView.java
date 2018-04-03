package com.common.app.widget;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.common.R;
import com.common.app.recycler.BaseRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 17/11/21 下午5:04
 * @description:
 */
public class GalleryView extends RecyclerView {

    private static final int LOADER_ID = 0x0111;

    private static final int MAX_IMAGE_COUNT = 3;

    private LoaderCallback mLoaderCallback;

    private Adapter mAdapter;

    private List<Image> mSelectedImages = new LinkedList<>();

    private SelectedChangedListener mSelectedChangedListener;

    public interface SelectedChangedListener {
        void onSelectedCountChanged(int count);
    }

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        mAdapter = new Adapter();
        setAdapter(mAdapter);
        mAdapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener<Image>() {
            @Override
            public void onClick(BaseRecyclerAdapter.BaseViewHolder<Image> viewHolder, int position) {
                if (onItemSelectClick(viewHolder.mData)) {
                    mAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onLongClick(BaseRecyclerAdapter.BaseViewHolder<Image> viewHolder, int position) {
            }
        });

        mLoaderCallback = new LoaderCallback();
    }

    private boolean onItemSelectClick(Image image) {
        boolean notifyRefresh;
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
            image.isSelect = false;
            notifyRefresh = true;
        } else if (mSelectedImages.size() >= MAX_IMAGE_COUNT) {
            notifyRefresh = false;
            // toast
        } else {
            mSelectedImages.add(image);
            image.isSelect = true;
            notifyRefresh = true;
        }
        if (notifyRefresh) {
            notifySelectedChanged();
        }
        return notifyRefresh;
    }

    private void notifySelectedChanged() {
        if (mSelectedChangedListener != null) {
            mSelectedChangedListener.onSelectedCountChanged(mSelectedImages.size());
        }
    }

    private class Image {
        int id;
        String path;
        long date;
        boolean isSelect;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;

        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    /**
     * 初始化方法
     * @param loaderManager
     * @return 返回一个loaderId用于销毁loader
     */
    public int setup(LoaderManager loaderManager, SelectedChangedListener listener) {
        mSelectedChangedListener = listener;
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        return LOADER_ID;
    }

    /**
     * 得到所有选中图片的地址
     * @return
     */
    public String[] getSelectedImagePath() {
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image: mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    public void clear() {
        for (Image image: mSelectedImages) {
            image.isSelect = false;
        }
        mSelectedImages.clear();
        mAdapter.notifyDataSetChanged();
    }

    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID, // id
                MediaStore.Images.Media.DATA, // 路径
                MediaStore.Images.Media.DATE_ADDED // 图片创建时间
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id != LOADER_ID) {
                return null;
            }
            return new CursorLoader(getContext(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION,
                    null,
                    null,
                    IMAGE_PROJECTION[2] + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            List<Image> images = new ArrayList<>();
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();

                    int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long date = data.getLong(indexDate);

                        File file = new File(path);
                        if (!file.exists()) {
                            continue;
                        }
                        Image image = new Image();
                        image.path = path;
                        image.date = date;
                        image.id = id;
                        images.add(image);
                    } while (data.moveToNext());
                }
            }
            mAdapter.replace(images);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private class Adapter extends BaseRecyclerAdapter<Image> {

        @Override
        protected int getIdFromType(int viewType) {
            return R.layout.cell_galley;
        }

        @Override
        protected BaseViewHolder<Image> createViewHolder(View rootView, int viewType) {
            return new ViewHolder(rootView);
        }
    }

    private class ViewHolder extends BaseRecyclerAdapter.BaseViewHolder<Image> {
        public ImageView mImage;
        private View mShade;
        private CheckBox mSelected;

        public ViewHolder(View rootView) {
            super(rootView);
            mImage = (ImageView) rootView.findViewById(R.id.im_image);
            mShade = rootView.findViewById(R.id.view_shade);
            mSelected = (CheckBox) rootView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(Color.parseColor("#eeeeee"))
                    .into(mImage);
            mShade.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
            mSelected.setChecked(image.isSelect);

        }
    }
}
