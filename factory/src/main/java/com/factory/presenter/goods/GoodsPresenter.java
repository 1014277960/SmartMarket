package com.factory.presenter.goods;

import android.support.v7.util.DiffUtil;

import com.common.app.recycler.BaseRecyclerAdapter;
import com.common.factory.data.DBDataSource;
import com.common.factory.data.DataSource;
import com.common.factory.presenter.BaseSourcePresenter;
import com.factory.Factory;
import com.factory.data.goods.GoodsRepository;
import com.factory.data.helper.GoodsHelper;
import com.factory.model.db.Goods;
import com.factory.util.DiffUiDataCallback;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 18/2/9 下午2:42
 * @description:
 */
public class GoodsPresenter extends BaseSourcePresenter<Goods, Goods, DBDataSource<Goods>, GoodsContract.View>
        implements GoodsContract.Presenter, DataSource.Callback<List<Goods>> {


    public GoodsPresenter(GoodsContract.View view) {
        super(new GoodsRepository(Goods.class), view);
    }

    @Override
    public void start() {
        super.start();
        loadGoodsFromLocal();
        loadGoods();
    }

    private void loadGoodsFromLocal() {
        List<Goods> goodsList = SQLite.select()
                .from(Goods.class)
                .queryList();
        onDataLoaded(goodsList);
    }

    @Override
    public void onDataLoaded(final List<Goods> goodses) {
        Factory.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseRecyclerAdapter adapter = getView().getRecyclerAdapter();
                List<Goods> old = adapter.getItems();
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUiDataCallback(old, goodses));
                refreshData(result, goodses);
            }
        });
    }

    @Override
    public void loadGoods() {
        GoodsHelper.getAllGoods(this);
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
}
