package com.common.factory.data;

import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 18/2/9 下午2:17
 * @description: 基础的数据库数据源接口定义
 */
public interface DBDataSource<Data> extends DataSource {

    /**
     * 数据源即获取数据的地方，有一个基本的数据源加载方法
     * @param callback 一般回调到presenter
     */
    void load(DataSource.SucceedCallback<List<Data>> callback);
}
