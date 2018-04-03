package com.common.app.recycler;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface AdapterCallback<Data> {
    void update(Data data, BaseRecyclerAdapter.BaseViewHolder<Data> holder);
}
