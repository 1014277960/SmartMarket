package com.common.app.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.app.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author wulinpeng
 * @datetime: 17/11/12 下午10:26
 * @description: Fragment基类
 */
public abstract class BaseFragment extends Fragment {

    private View mRoot;

    private Unbinder mRootUnbinder;

    protected PlaceHolderView mPlaceHolderView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRoot == null) {
            int layoutId = getLayoutId();
            // 不绑定mRoot到container，返回mRoot后系统自动绑定
            mRoot = inflater.inflate(layoutId, container, false);
            initView(mRoot);
        } else if (mRoot.getParent() != null){
            // 如果mRoot存在且已经绑定在某parent上，解除绑定
            ((ViewGroup)mRoot.getParent()).removeView(mRoot);
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    protected abstract int getLayoutId();

    protected void initArgs(Bundle bundle) {

    }

    protected void initView(View view) {
        // 存储unbinder，以便接下来unbind
        mRootUnbinder = ButterKnife.bind(this, view);
    }

    protected void initData() {

    }

    /**
     * back触发时调用，返回true代表已经处理，activity不用关心
     * @return
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 设置占位布局
     * @param placeHolderView
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        mPlaceHolderView = placeHolderView;
    }
}
