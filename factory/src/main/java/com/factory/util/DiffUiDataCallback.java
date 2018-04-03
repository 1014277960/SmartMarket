package com.factory.util;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 18/2/2 下午11:05
 * @description:
 */
public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {
    List<T> oldData, newData;

    public DiffUiDataCallback(List<T> oldData, List<T> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    // 旧的数据大小
    @Override
    public int getOldListSize() {
        return oldData.size();
    }

    // 新的数据大小
    @Override
    public int getNewListSize() {
        return newData.size();
    }

    // 两个item是否是同一个东西，比如id相同的user
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldData.get(oldItemPosition);
        T newItem = newData.get(newItemPosition);
        return newItem.isItemSame(oldItem);
    }

    // 在经过相等判断后，进一步判断内容，比如同一个user的name不同
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldData.get(oldItemPosition);
        T newItem = newData.get(newItemPosition);
        return newItem.isContentSame(oldItem);
    }

    // 用于比较的T必须实现这些方法
    public interface UiDataDiffer<T> {
        boolean isItemSame(T old);

        boolean isContentSame(T old);
    }
}
