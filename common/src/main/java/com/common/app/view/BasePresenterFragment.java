package com.common.app.view;

import android.content.Context;

import com.common.app.Application;
import com.common.factory.presenter.BaseContract;


/**
 * @author wulinpeng
 * @datetime: 18/1/26 下午3:09
 * @description: MVP中作为View的fragment的基类，实现BaseContract的基础方法
 */
public abstract class BasePresenterFragment<T extends BaseContract.Presenter> extends BaseFragment
        implements BaseContract.View<T> {

    protected T mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initPresenter();
    }

    // 初始化presenter
    protected abstract T initPresenter();

    @Override
    public void showError(String errMsg) {
        // 显示错误，优先使用占位布局
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(errMsg);
        } else {
            Application.showToast(errMsg);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }
}
