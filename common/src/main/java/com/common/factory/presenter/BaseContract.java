package com.common.factory.presenter;

import com.common.app.recycler.BaseRecyclerAdapter;

/**
 * @author wulinpeng
 * @datetime: 18/1/26 下午3:01
 * @description: MVP模式中公共的部分
 */
public interface BaseContract {

    interface View<T extends BaseContract.Presenter> {

        void showError(String errMsg);

        void showLoading();

        void setPresenter(T presenter);
    }

    interface Presenter {

        // 公用的开始方法
        void start();

        // 公用的销毁方法
        void destroy();
    }


    // 基本的一个列表的View的职责
    interface RecyclerView<T extends Presenter, ViewMode> extends View<T> {
        // 拿到一个适配器，然后自主刷新
        BaseRecyclerAdapter<ViewMode> getRecyclerAdapter();

        // 当适配器数据更改
        void onAdapterDataChanged();
    }
}
