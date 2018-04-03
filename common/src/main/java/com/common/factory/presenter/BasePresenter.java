package com.common.factory.presenter;

/**
 * @author wulinpeng
 * @datetime: 18/1/26 下午3:17
 * @description:
 */
public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {

    protected T mView;

    public BasePresenter(T view) {
        setView(view);
    }

    protected void setView(T view) {
        this.mView = view;
        mView.setPresenter(this);
    }

    protected final T getView() {
        return mView;
    }

    @Override
    public void start() {
        if (mView != null) {
            mView.showLoading();
        }
    }

    /**
     * 销毁操作
     */
    @Override
    public void destroy() {
        if (mView != null) {
            mView.setPresenter(null);
            mView = null;
        }
    }
}
