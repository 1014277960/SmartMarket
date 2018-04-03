package com.common.app;

import android.content.Context;
import android.widget.Toast;

/**
 * @author wulinpeng
 * @datetime: 18/2/13 下午2:25
 * @description:
 */
public class Application extends android.app.Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static void showToast(String msg) {
        // TODO: 18/2/25 失效
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
