package com.factory.data.helper;

import com.common.factory.data.DataSource;
import com.factory.model.api.RspModel;
import com.factory.model.db.Goods;
import com.factory.net.NetWork;
import com.factory.net.RemoteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wulinpeng
 * @datetime: 18/2/14 下午3:37
 * @description:
 */
public class GoodsHelper {

    public static void getAllGoods(final DataSource.Callback<List<Goods>> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<List<Goods>>> call = service.getAllGoods();
        call.enqueue(new GoodsListRspCallback(callback));
    }

    public static void getGoodsByCode(String code, final DataSource.Callback<Goods> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<Goods>> call = service.getGoodsByCode(code);
        call.enqueue(new GoodsRspCallback(callback));
    }

    private static class GoodsRspCallback implements Callback<RspModel<Goods>> {

        private DataSource.Callback<Goods> callback;

        public GoodsRspCallback(DataSource.Callback<Goods> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<Goods>> call, Response<RspModel<Goods>> response) {
            RspModel<Goods> rspModel = response.body();
            if (rspModel.success()) {
                Goods goods = rspModel.getResult();
                // 以网络数据为准，清除本地数据
                // 数据库写入，缓存绑定
                DBHelper.getInstance().clearAll(Goods.class);
                DBHelper.getInstance().save(Goods.class, goods);
                if (callback != null) {
                    callback.onDataLoaded(goods);
                }
            } else {
                // todo 对rspModel中的code解析，判断错误类型
                callback.onDataLoadFailed("unknow error");
            }
        }

        @Override
        public void onFailure(Call<RspModel<Goods>> call, Throwable t) {
            if (callback != null) {
                callback.onDataLoadFailed("net work error");
            }
        }
    }

    private static class GoodsListRspCallback implements Callback<RspModel<List<Goods>>> {

        private DataSource.Callback<List<Goods>> callback;

        public GoodsListRspCallback(DataSource.Callback<List<Goods>> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<List<Goods>>> call, Response<RspModel<List<Goods>>> response) {
            RspModel<List<Goods>> rspModel = response.body();
            if (rspModel.success()) {
                List<Goods> goodsList = rspModel.getResult();
                // 以网络数据为准，清除本地数据
                // 数据库写入，缓存绑定
                DBHelper.getInstance().clearAll(Goods.class);
                DBHelper.getInstance().save(Goods.class, goodsList.toArray(new Goods[0]));
//                if (callback != null) {
//                    callback.onDataLoaded(goodsList);
//                }
            } else {
                // todo 对rspModel中的code解析，判断错误类型
                callback.onDataLoadFailed("unknow error");
            }
        }

        @Override
        public void onFailure(Call<RspModel<List<Goods>>> call, Throwable t) {
            if (callback != null) {
                callback.onDataLoadFailed("net work error");
            }
        }
    }
}
