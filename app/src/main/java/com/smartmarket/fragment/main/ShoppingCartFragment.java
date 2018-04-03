package com.smartmarket.fragment.main;


import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.app.recycler.BaseRecyclerAdapter;
import com.common.app.view.BaseFragment;
import com.common.app.view.BasePresenterFragment;
import com.common.app.widget.EmptyView;
import com.factory.data.helper.DBHelper;
import com.factory.model.api.cart.AddGoodsModel;
import com.factory.model.db.Cart;
import com.factory.model.db.Goods;
import com.factory.presenter.goods.CartContract;
import com.factory.presenter.goods.CartPresenter;
import com.smartmarket.App;
import com.smartmarket.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.himanshusoni.quantityview.QuantityView;
import retrofit2.http.Body;

/**
 * @author wulinpeng
 * @datetime: 18/2/3 下午2:43
 * @description: 购物车fragment
 */
public class ShoppingCartFragment extends BasePresenterFragment<CartContract.Presenter> implements CartContract.View, SwipeRefreshLayout.OnRefreshListener, BaseRecyclerAdapter.ItemClickListener<Cart> {

    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    public EmptyView mEmptyView;
    @BindView(R.id.refresh_layout)
    public SwipeRefreshLayout mRefreshLayout;

    private CartAdapter mAdapter;

    public boolean isEditing = false;
    private boolean isChange = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shopping_cart;
    }

    @Override
    protected CartContract.Presenter initPresenter() {
        return new CartPresenter(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        setupPlaceHolderView();
        mRefreshLayout.setOnRefreshListener(this);
        initRecyclerView();

        mPresenter.start();
    }

    private void initRecyclerView() {

        mAdapter = new CartAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setItemClickListener(this);

    }

    private void setupPlaceHolderView() {
        setPlaceHolderView(mEmptyView);
        mEmptyView.bind(mContent);
    }

    @Override
    public BaseRecyclerAdapter<Cart> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mRefreshLayout.setRefreshing(false);
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadCarts();
    }

    @Override
    public void onClick(BaseRecyclerAdapter.BaseViewHolder<Cart> viewHolder, int position) {

    }

    @Override
    public void onLongClick(BaseRecyclerAdapter.BaseViewHolder<Cart> viewHolder, int position) {
        showDeleteDialog(viewHolder.mData);
    }

    private void showDeleteDialog(final Cart cart) {
        new AlertDialog.Builder(getContext())
                .setTitle("删除")
                .setMessage("是否从购物车中删除该商品")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 删除操作
                        mPresenter.deleteCarts(cart);
                    }
                })
                .create().show();
    }

    public void edit() {
        if (mAdapter.getItems().size() == 0) {
            App.showToast("购物车中没有商品");
            return;
        }
        if (isEditing) {
            isEditing = false;
            doSave();
        } else {
            isEditing = true;
            doEdit();
        }
    }

    private void doEdit() {
        mAdapter.notifyDataSetChanged();
    }

    private void doSave() {
        mAdapter.notifyDataSetChanged();
        if (!isChange) {
            return;
        }
        List<AddGoodsModel.Item> items = buildItems(mAdapter.getItems());
        mPresenter.addCarts(items.toArray(new AddGoodsModel.Item[0]));
        isChange = false;
    }

    private List<AddGoodsModel.Item> buildItems(List<Cart> carts) {
        List<AddGoodsModel.Item> itemLIst = new ArrayList<>();
        for (Cart cart : carts) {
            itemLIst.add(new AddGoodsModel.Item(cart.getGoods().getId(), cart.getCount()));
        }
        return itemLIst;
    }

    class CartAdapter extends BaseRecyclerAdapter<Cart> {

        @Override
        protected int getIdFromType(int viewType) {
            return R.layout.cell_cart;
        }

        @Override
        protected BaseViewHolder<Cart> createViewHolder(View rootView, int viewType) {
            return new CartHolder(rootView);
        }

        class CartHolder extends BaseViewHolder<Cart> {

            @BindView(R.id.goods_pic)
            ImageView pic;
            @BindView(R.id.goods_name)
            TextView name;
            @BindView(R.id.goods_price)
            TextView price;
            @BindView(R.id.goods_count)
            TextView count;
            @BindView(R.id.quantity_view)
            QuantityView quantityView;

            public CartHolder(View itemView) {
                super(itemView);
            }

            @Override
            protected void onBind(final Cart cart) {
                // TODO: 18/2/22 封装图片加载框架，或者像portraitview一样
                Glide.with(getContext())
                        .load(cart.getGoods().getPicture())
                        .placeholder(R.drawable.ic_goods_place_holder)
                        .centerCrop()
                        .into(pic);
                name.setText(cart.getGoods().getName());
                price.setText(cart.getGoods().getPrice() + "元");
                // 不能用goods里面的count，因为这里是表示购物车的数量，而不是库存
                count.setText("X " + cart.getCount());
                count.setVisibility(isEditing? View.GONE: View.VISIBLE);
                quantityView.setVisibility(isEditing? View.VISIBLE: View.GONE);
                quantityView.setMinQuantity(1);
                quantityView.setMaxQuantity(mData.getGoods().getCount());
                quantityView.setQuantity(mData.getCount());
                quantityView.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
                    @Override
                    public void onQuantityChanged(int i, int i1, boolean b) {
                        isChange = true;
                        cart.setCount(i1);
                    }

                    @Override
                    public void onLimitReached() {

                    }
                });
            }
        }
    }
}
