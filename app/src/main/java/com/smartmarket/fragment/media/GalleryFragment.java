package com.smartmarket.fragment.media;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.app.widget.GalleryView;
import com.smartmarket.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangedListener {

    private GalleryView mGalleryView;

    private OnSelectedListener mListener;

    public interface OnSelectedListener {
        void onSelectedImage(String path);
    }

    public GalleryFragment setListener(OnSelectedListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGalleryView = (GalleryView) view.findViewById(R.id.gallery_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGalleryView.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        if (count > 0) {
            // 隐藏销毁dialog和fragment
            dismiss();
            if (mListener != null) {
                mListener.onSelectedImage(mGalleryView.getSelectedImagePath()[0]);
                mListener = null;
            }
        }
    }
}
