package com.common.app.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author wulinpeng
 * @datetime: 17/11/12 下午11:12
 * @description:
 */
public abstract class BaseRecyclerAdapter<Data> extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder<Data>>
    implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {

    private List<Data> mDataList = new ArrayList<>();

    private ItemClickListener<Data> mItemClickListener;

    public void setItemClickListener(ItemClickListener<Data> listener) {
        mItemClickListener = listener;
    }

    /**
     * item点击事件的回调
     * @param <Data>
     */
    public interface ItemClickListener<Data> {
        public void onClick(BaseViewHolder<Data> viewHolder, int position);
        public void onLongClick(BaseViewHolder<Data> viewHolder, int position);
    }

    @Override
    public void onClick(View v) {
        BaseViewHolder<Data> viewHolder = (BaseViewHolder<Data>) v.getTag(R.id.tag_recycler_holder);
        int position = viewHolder.getAdapterPosition();
        if (mItemClickListener != null) {
            mItemClickListener.onClick(viewHolder, position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        BaseViewHolder<Data> viewHolder = (BaseViewHolder<Data>) v.getTag(R.id.tag_recycler_holder);
        int position = viewHolder.getAdapterPosition();
        if (mItemClickListener != null) {
            mItemClickListener.onLongClick(viewHolder, position);
        }
        return true;
    }

    public BaseRecyclerAdapter() {}

    /**
     * 构造函数
     * @param dataList
     * @param listener
     */
    public BaseRecyclerAdapter(List<Data> dataList, ItemClickListener<Data> listener) {
        replace(dataList);
        mItemClickListener = listener;
    }

    public BaseRecyclerAdapter(List<Data> dataList) {
        this(dataList, null);
    }

    @Override
    public BaseViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(getIdFromType(viewType), parent, false);
        BaseViewHolder<Data> viewHolder = createViewHolder(root, viewType);

        // 设置点击事件
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        // view和holder双向绑定
        root.setTag(R.id.tag_recycler_holder, viewHolder);

        viewHolder.mUnbinder = ButterKnife.bind(viewHolder, root);
        viewHolder.callback = this;
        return viewHolder;
    }

    /**
     * 子类复写方法，返回type－>layoutId的映射，以供onCreateViewHolder创建View
     * @param viewType
     * @return
     */
    protected abstract int getIdFromType(int viewType);

    /**
     * 子类复习ViewHolder创建方法，以供onCreateViewHolder创建ViewHolder
     * @param rootView
     * @param viewType
     * @return
     */
    protected abstract BaseViewHolder<Data> createViewHolder(View rootView, int viewType);

    @Override
    public void onBindViewHolder(BaseViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<Data> getItems() {
        return mDataList;
    }

    /**
     * 插入一条数据并更新
     * @param data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据并更新
     * @param dataList
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startIndex = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeChanged(startIndex, dataList.length);
        }

    }

    /**
     * 插入一堆数据并更新
     * @param dataList
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startIndex = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeChanged(startIndex, dataList.size());
        }
    }

    /**
     * 删除操作
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为新的集合
     * @param dataList
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void update(Data data, BaseViewHolder<Data> holder) {
        // 得到当前ViewHolder的坐标
        int pos = holder.getAdapterPosition();
        if (pos >= 0) {
            // 进行数据的移除与更新
            mDataList.remove(pos);
            mDataList.add(pos, data);
            // 通知这个坐标下的数据有更新
            notifyItemChanged(pos);
        }
    }

    public static abstract class BaseViewHolder<Data> extends RecyclerView.ViewHolder {
        public Data mData;
        private AdapterCallback<Data> callback;
        public Unbinder mUnbinder;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据的触发
         * @param data
         */
        void bind(Data data) {
            mData = data;
            onBind(data);
        }

        /**
         * 当绑定数据时的回调，必须复写
         * @param data
         */
        protected abstract void onBind(Data data);

        /**
         * Holder自己对自己对应的Data进行更新操作
         *
         * @param data Data数据
         */
        public void updateData(Data data) {
            if (this.callback != null) {
                this.callback.update(data, this);
            }
        }
    }
}
