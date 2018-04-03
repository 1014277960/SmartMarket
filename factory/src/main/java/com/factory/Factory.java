package com.factory;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.common.app.Application;
import com.factory.persistence.Account;
import com.factory.util.DBFlowExclusionStrategies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * @author wulinpeng
 * @datetime: 18/2/9 下午2:29
 * @description:
 */
public class Factory {

    private static Gson gson;

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public static void setup() {
        // 数据库初始化
        FlowManager.init(new FlowConfig.Builder(app()).openDatabasesOnInit(true).build());
        // 初始持久化信息
        Account.load(app());

        initGson();
    }

    private static void initGson() {
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .setExclusionStrategies(new DBFlowExclusionStrategies())
                .create();
    }

    public static Context app() {
        return Application.context;
    }

    public static Gson getGson() {
        return gson;
    }
}
