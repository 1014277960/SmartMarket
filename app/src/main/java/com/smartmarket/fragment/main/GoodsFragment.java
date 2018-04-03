package com.smartmarket.fragment.main;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.app.recycler.BaseRecyclerAdapter;
import com.common.app.view.BaseFragment;
import com.common.app.view.BasePresenterFragment;
import com.common.app.widget.EmptyView;
import com.factory.model.db.Goods;
import com.factory.presenter.goods.GoodsContract;
import com.factory.presenter.goods.GoodsPresenter;
import com.smartmarket.R;
import com.smartmarket.activity.goods.GoodsDetailActivity;

import butterknife.BindView;

/**
 * @author wulinpeng
 * @datetime: 18/2/3 下午2:43
 * @description: 商品列表fragment
 */
public class GoodsFragment extends BasePresenterFragment<GoodsContract.Presenter>
        implements GoodsContract.View, SwipeRefreshLayout.OnRefreshListener, BaseRecyclerAdapter.ItemClickListener<Goods> {

    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    public EmptyView mEmptyView;
    @BindView(R.id.refresh_layout)
    public SwipeRefreshLayout mRefreshLayout;

    private GoodsAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        setupPlaceHolderView();
        mRefreshLayout.setOnRefreshListener(this);
        initRecyclerView();

        mPresenter.start();
    }

    private void setupPlaceHolderView() {
        setPlaceHolderView(mEmptyView);
        mEmptyView.bind(mRecyclerView);
    }

    private void initRecyclerView() {
        mAdapter = new GoodsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setItemClickListener(this);
    }

    @Override
    protected GoodsContract.Presenter initPresenter() {
        return new GoodsPresenter(this);
    }

    @Override
    public BaseRecyclerAdapter<Goods> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadGoods();
    }

    @Override
    public void onClick(BaseRecyclerAdapter.BaseViewHolder<Goods> viewHolder, int position) {
        GoodsDetailActivity.show(getContext(), viewHolder.mData.getId(), viewHolder.mData.getBarcode());
    }

    @Override
    public void onLongClick(BaseRecyclerAdapter.BaseViewHolder<Goods> viewHolder, int position) {

    }

    class GoodsAdapter extends BaseRecyclerAdapter<Goods> {

        @Override
        protected int getIdFromType(int viewType) {
            return R.layout.cell_goods;
        }

        @Override
        protected BaseViewHolder<Goods> createViewHolder(View rootView, int viewType) {
            return new GoodsHolder(rootView);
        }

        class GoodsHolder extends BaseViewHolder<Goods> {

            @BindView(R.id.goods_pic)
            ImageView pic;
            @BindView(R.id.goods_name)
            TextView name;
            @BindView(R.id.goods_desc)
            TextView desc;
            @BindView(R.id.goods_price)
            TextView price;

            public GoodsHolder(View itemView) {
                super(itemView);
            }

            @Override
            protected void onBind(Goods goods) {
                // TODO: 18/2/22 封装图片加载框架，或者像portraitview一样
                Glide.with(getContext())
                        .load(goods.getPicture())
                        .placeholder(R.drawable.ic_goods_place_holder)
                        .centerCrop()
                        .into(pic);
                name.setText(goods.getName());
                desc.setText(goods.getDescription());
                price.setText(goods.getPrice() + "元");
            }
        }
    }
}
