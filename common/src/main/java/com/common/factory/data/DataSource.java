package com.common.factory.data;

/**
 * @author wulinpeng
 * @datetime: 18/2/13 下午2:04
 * @description:
 */
public interface DataSource {
    interface Callback<T> extends SucceedCallback<T>, FailedCallback {

    }

    interface SucceedCallback<T> {
        void onDataLoaded(T t);
    }

    interface FailedCallback {
        void onDataLoadFailed(String msg);
    }

    void dispose();
}
