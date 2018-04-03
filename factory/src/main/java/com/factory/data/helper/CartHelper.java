package com.factory.data.helper;

import com.common.factory.data.DataSource;
import com.factory.model.api.RspModel;
import com.factory.model.api.cart.AddGoodsModel;
import com.factory.model.api.cart.DeleteGoodsModel;
import com.factory.model.db.Cart;
import com.factory.model.db.Goods;
import com.factory.net.NetWork;
import com.factory.net.RemoteService;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wulinpeng
 * @datetime: 18/2/17 下午4:30
 * @description:
 */
public class CartHelper {

    public static void getCarts(final DataSource.Callback<List<Cart>> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<List<Cart>>> call = service.getCarts();
        call.enqueue(new AddCartCallback(callback));
    }

    public static void addGoods(AddGoodsModel model, final DataSource.Callback<List<Cart>> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<List<Cart>>> call = service.addGoods(model);
        call.enqueue(new AddCartCallback(callback));
    }

    public static void deleteGoods(DeleteGoodsModel model, final DataSource.Callback<List<Cart>> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<List<Cart>>> call = service.deleteGoods(model);
        call.enqueue(new DeleteCartCallback(callback));
    }

    private static class AddCartCallback extends CartRspCallback {

        public AddCartCallback(DataSource.Callback<List<Cart>> callback) {
            super(callback);
        }

        @Override
        protected void dealData(Cart... carts) {
            // 以网络数据为准，清除本地数据
            // 数据库写入，缓存绑定
            DBHelper.getInstance().clearAll(Cart.class);
            DBHelper.getInstance().save(Cart.class, carts);
                if (callback != null) {
                    callback.onDataLoaded(Arrays.asList(carts));
                }
        }
    }

    private static class DeleteCartCallback extends CartRspCallback {

        public DeleteCartCallback(DataSource.Callback<List<Cart>> callback) {
            super(callback);
        }

        /**
         * 删除操作，而不是add
         * @param carts
         */
        @Override
        protected void dealData(Cart... carts) {
            DBHelper.getInstance().delete(Cart.class, carts);
        }
    }


    /**
     * 返回的cart数据对add和delete的操作是不一样的，所以开放dealData操作给子类
     */
    private abstract static class CartRspCallback implements Callback<RspModel<List<Cart>>> {

        protected DataSource.Callback<List<Cart>> callback;

        public CartRspCallback(DataSource.Callback<List<Cart>> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<List<Cart>>> call, Response<RspModel<List<Cart>>> response) {
            RspModel<List<Cart>> rspModel = response.body();
            if (rspModel.success()) {
                List<Cart> cartList = rspModel.getResult();
                dealData(cartList.toArray(new Cart[0]));
            } else {
                // todo 对rspModel中的code解析，判断错误类型
                callback.onDataLoadFailed("unknow error");
            }
        }

        @Override
        public void onFailure(Call<RspModel<List<Cart>>> call, Throwable t) {
            if (callback != null) {
                callback.onDataLoadFailed("net work error");
            }
        }

        protected abstract void dealData(Cart... carts);
    }
}
