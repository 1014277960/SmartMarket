package com.factory.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * @author wulinpeng
 * @datetime: 18/1/27 下午11:14
 * @description: DBFLow过滤字段
 */
public class DBFlowExclusionStrategies implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        // 被跳过的字段
        return f.getDeclaredClass().equals(ModelAdapter.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        // 被跳过的class
        return false;
    }
}
