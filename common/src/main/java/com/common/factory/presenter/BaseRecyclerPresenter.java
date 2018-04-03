package com.common.factory.presenter;

import android.content.Context;
import android.os.Looper;
import android.support.v7.util.DiffUtil;

import com.common.app.recycler.BaseRecyclerAdapter;

import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 18/2/9 下午2:25
 * @description: 刷新RecyclerVIew的presenter基类，封装刷新数据到adapter的方法
 */
public class BaseRecyclerPresenter<ViewMode, View extends BaseContract.RecyclerView> extends BasePresenter<View> {

    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    /**
     * 全局刷新
     * @param dataList
     */
    protected void refreshData(final List<ViewMode> dataList) {
        // TODO: 18/2/9 主线程判断
        View view = getView();
        if (view == null) {
            return;
        }
        view.getRecyclerAdapter().replace(dataList);
        view.onAdapterDataChanged();
    }

    /**
     * 增量刷新
     * @param diffResult
     * @param dataList
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult, final List<ViewMode> dataList) {
        // TODO: 18/2/9 主线程判断
        View view = getView();
        if (view == null) {
            return;
        }
        // 增量的更新数据,不使用replace，因为这个方法直接刷新界面了，我们使用diffResult刷新
        BaseRecyclerAdapter<ViewMode> adapter = getView().getRecyclerAdapter();
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);
        view.onAdapterDataChanged();

        diffResult.dispatchUpdatesTo(adapter);
    }
}
