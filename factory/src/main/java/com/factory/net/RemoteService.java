package com.factory.net;

import com.factory.model.api.RspModel;
import com.factory.model.api.account.AccountRspModel;
import com.factory.model.api.account.LoginModel;
import com.factory.model.api.account.RegisterModel;
import com.factory.model.api.account.UpdateInfoModel;
import com.factory.model.api.cart.AddGoodsModel;
import com.factory.model.api.cart.DeleteGoodsModel;
import com.factory.model.db.Cart;
import com.factory.model.db.Goods;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author wulinpeng
 * @datetime: 18/1/26 下午10:51
 * @description:
 */
public interface RemoteService {

    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    @POST("account/update")
    Call<RspModel<AccountRspModel>> update(@Body UpdateInfoModel model);

    @GET("goods")
    Call<RspModel<List<Goods>>> getAllGoods();

    @GET("cart")
    Call<RspModel<List<Cart>>> getCarts();

    @PUT("cart/addGoodsToCart")
    Call<RspModel<List<Cart>>> addGoods(@Body AddGoodsModel model);

    @PUT("cart/deleteGoodsFromCart")
    Call<RspModel<List<Cart>>> deleteGoods(@Body DeleteGoodsModel model);

    @GET("goods/code/{code}")
    Call<RspModel<Goods>> getGoodsByCode(@Path(value = "code") String code);

}
