package com.factory.data.helper;

import com.common.utils.CollectionsUtil;
import com.factory.model.db.AppDatabase;
import com.factory.model.db.Goods;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wulinpeng
 * @datetime: 18/2/9 下午1:47
 * @description: 封装数据库操作，统一数据流分发
 */
public class DBHelper {

    private static DBHelper instance;

    static {
        instance = new DBHelper();
    }

    private DBHelper() {
    }

    private final Map<Class<?>, Set<DBDataListener>> dbListeners = new HashMap<>();

    public static DBHelper getInstance() {
        return instance;
    }

    /**
     * 添加数据监听者
     * @param dataClass
     * @param listener
     * @param <Data>
     */
    public <Data> void addDBDataListener(Class<Data> dataClass, DBDataListener<Data> listener) {
        Set<DBDataListener> listenerSet = dbListeners.get(dataClass);
        if (listenerSet == null) {
            listenerSet = new HashSet<>();
            dbListeners.put(dataClass, listenerSet);
        }
        listenerSet.add(listener);
    }

    /**
     * 移除数据监听者
     * @param dataClass
     * @param listener
     * @param <Data>
     */
    public <Data> void removeDBDataListener(Class<Data> dataClass, DBDataListener<Data> listener) {
        Set<DBDataListener> listenerSet = dbListeners.get(dataClass);
        if (listenerSet != null) {
            listenerSet.remove(listener);
        }
    }

    /**
     * 保存或者更新数据
     * @param dataClass
     * @param datas
     * @param <Data>
     */
    public <Data> void save(final Class<Data> dataClass, final Data... datas) {
        if (datas == null || datas.length == 0) {
            return;
        }
        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
        databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Data> adapter = FlowManager.getModelAdapter(dataClass);
                adapter.saveAll(Arrays.asList(datas));
                // 分发数据流
                notifySave(dataClass, datas);
            }

        }).build().execute();

    }

    /**
     * 删除数据
     * @param dataClass
     * @param datas
     * @param <Data>
     */
    public <Data> void delete(final Class<Data> dataClass, final Data... datas) {
        if (datas == null || datas.length == 0) {
            return;
        }
        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
        databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Data> adapter = FlowManager.getModelAdapter(dataClass);
                adapter.deleteAll(Arrays.asList(datas));
                // 分发数据流
                notifyDelete(dataClass, datas);
            }

        }).build().execute();
    }

    /**
     * 清楚某一类数据
     * @param dataClass
     * @param <Data>
     */
    public <Data> void clearAll(final Class<Data> dataClass) {
        List<Data> goodsList = SQLite.select()
                .from(dataClass)
                .queryList();
        if (goodsList != null && goodsList.size() > 0) {
            delete(dataClass, CollectionsUtil.listToArray(goodsList, dataClass));
        }

    }

    /**
     * 分发数据保存/修改操作
     * @param dataClass
     * @param datas
     * @param <Data>
     */
    private <Data> void notifySave(Class<Data> dataClass, Data[] datas) {
        Set<DBDataListener> listenerSet = dbListeners.get(dataClass);
        if (listenerSet != null && listenerSet.size() > 0) {
            for (DBDataListener dbDataListener : listenerSet) {
                dbDataListener.onDataSave(datas);
            }
        }
    }

    /**
     * 方法数据删除操作
     * @param dataClass
     * @param datas
     * @param <Data>
     */
    private <Data> void notifyDelete(Class<Data> dataClass, Data... datas) {
        Set<DBDataListener> listenerSet = dbListeners.get(dataClass);
        if (listenerSet != null && listenerSet.size() > 0) {
            for (DBDataListener dbDataListener : listenerSet) {
                dbDataListener.onDataDelete(datas);
            }
        }
    }

    public interface DBDataListener<Data> {
        void onDataSave(Data... datas);

        void onDataDelete(Data... datas);
    }
}
