package com.factory.presenter.goods;

import android.support.v7.util.DiffUtil;

import com.common.app.recycler.BaseRecyclerAdapter;
import com.common.factory.data.DBDataSource;
import com.common.factory.presenter.BaseSourcePresenter;
import com.factory.Factory;
import com.factory.data.goods.CartRepository;
import com.factory.data.helper.CartHelper;
import com.factory.model.api.cart.AddGoodsModel;
import com.factory.model.api.cart.DeleteGoodsModel;
import com.factory.model.db.Cart;
import com.factory.model.db.Goods;
import com.factory.util.DiffUiDataCallback;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 18/2/17 下午4:26
 * @description:
 */
public class CartPresenter extends BaseSourcePresenter<Cart, Cart, DBDataSource<Cart>, CartContract.View>
    implements CartContract.Presenter, DBDataSource.Callback<List<Cart>>{
    public CartPresenter(CartContract.View view) {
        super(new CartRepository(Cart.class), view);
    }

    @Override
    public void start() {
        super.start();
        loadCartFromLocal();
        loadCarts();
    }

    private void loadCartFromLocal() {
        List<Cart> cartList = SQLite.select()
                .from(Cart.class)
                .queryList();
        onDataLoaded(cartList);
    }

    @Override
    public void onDataLoaded(final List<Cart> carts) {
        Factory.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseRecyclerAdapter adapter = getView().getRecyclerAdapter();
                List<Cart> old = adapter.getItems();
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUiDataCallback(old, carts));
                refreshData(result, carts);
            }
        });
    }

    @Override
    public void onDataLoadFailed(final String msg) {
        Factory.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getView().showError(msg);
            }
        });
    }

    @Override
    public void loadCarts() {
        CartHelper.getCarts(this);
    }

    @Override
    public void deleteCarts(Cart... carts) {
        CartHelper.deleteGoods(buildDeleteModel(carts), this);
    }

    private DeleteGoodsModel buildDeleteModel(Cart... carts) {
        DeleteGoodsModel model = new DeleteGoodsModel();
        List<DeleteGoodsModel.Item> items = new ArrayList<>();
        for (Cart cart : carts) {
            items.add(new DeleteGoodsModel.Item(cart.getGoods().getId(), 0));
        }
        model.setItems(items);
        return model;
    }

    @Override
    public void addCarts(AddGoodsModel.Item... items) {
        CartHelper.addGoods(buildAddModel(items), this);
    }

    private AddGoodsModel buildAddModel(AddGoodsModel.Item... items) {
        AddGoodsModel model = new AddGoodsModel();
        model.setItems(Arrays.asList(items));
        return model;
    }


}
