package com.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * @author wulinpeng
 * @datetime: 18/1/27 下午11:16
 * @description: 数据库基本信息
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase";
    public static final int VERSION = 3;

}
