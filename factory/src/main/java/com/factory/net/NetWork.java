package com.factory.net;

import android.text.TextUtils;


import com.common.Common;
import com.factory.Factory;
import com.factory.persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author wulinpeng
 * @datetime: 18/1/26 下午10:44
 * @description:
 */
public class NetWork {
    private static NetWork instance;
    private static Retrofit retrofit;

    static {
        instance = new NetWork();
    }

    private NetWork() {}

    public static Retrofit getRetrofit() {
        if (retrofit != null) {
            return retrofit;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                // 给所有请求添加一个拦截器
                // 添加"token"请求头
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            builder.addHeader("token", Account.getToken());
                        }
                        return chain.proceed(builder.build());
                    }
                }).build();

        Retrofit.Builder builder = new Retrofit.Builder();

        retrofit =  builder.baseUrl(Common.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

        return retrofit;
    }

    public static RemoteService remote() {
        return getRetrofit().create(RemoteService.class);
    }
}
