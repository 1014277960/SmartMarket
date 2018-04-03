package com.smartmarket;

import android.app.Application;

import com.factory.Factory;

/**
 * @author wulinpeng
 * @datetime: 18/2/13 下午1:39
 * @description:
 */
public class App extends com.common.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Factory.setup();
    }
}
