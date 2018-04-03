package com.factory.data;

/**
 * @author wulinpeng
 * @datetime: 18/2/9 下午2:50
 * @description:
 */

import android.util.Log;

import com.common.factory.data.DBDataSource;
import com.factory.data.helper.DBHelper;
import com.factory.util.DiffUiDataCallback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 18/2/9 下午2:48
 * @description: 数据源基础实现类，保存数据集合，对save／delete的数据选择性的更新到维护的数据集合
 */
public abstract class BaseDBRepository<Data extends DiffUiDataCallback.UiDataDiffer> implements DBDataSource<Data>, DBHelper.DBDataListener<Data> {

    private SucceedCallback<List<Data>> callback;

    // 当前缓存数据
    final private List<Data> dataList = new LinkedList<>();

    // 当前范型对应真实的class信息
    private Class<Data> dataClass;

    public BaseDBRepository(Class<Data> dataClass) {
        this.dataClass = dataClass;
    }

    @Override
    public void load(SucceedCallback<List<Data>> callback) {
        this.callback = callback;
        DBHelper.getInstance().addDBDataListener(dataClass, this);
    }

    @Override
    public void dispose() {
        DBHelper.getInstance().removeDBDataListener(dataClass, this);
    }

    // 数据库通知的地方:增加/修改
    @Override
    public void onDataSave(Data... list) {
        boolean isChanged = false;
        for (Data data : list) {
            if (isRequire(data)) {
                insertOrUpdate(data);
                isChanged = true;
            }
        }
        if (isChanged) {
            notifyDataChange();
        }
    }

    private void insertOrUpdate(Data data) {
        int index = indexOf(data);
        if (index >= 0) {
            replace(index, data);
        } else {
            insert(data);
        }
        Log.d("Debug", index + " " + data.toString());
    }

    private void insert(Data data) {
        dataList.add(data);
    }

    private void replace(int index, Data data) {
        dataList.remove(index);
        dataList.add(index, data);
    }

    private int indexOf(Data data) {
        for (int i = 0; i < dataList.size(); i++) {
            if (data.isItemSame(dataList.get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onDataDelete(Data... datas) {
        boolean isChange = false;

        if (datas != null && datas.length > 0) {
            for (Data data : datas) {
                dataList.remove(data);
                isChange = true;
            }
        }
        if (isChange) {
            notifyDataChange();
        }
    }

    private void notifyDataChange() {
        if (callback != null) {
            callback.onDataLoaded(dataList);
        }
    }

    /**
     * 拦截器功能
     * @param data
     * @return
     */
    protected abstract boolean isRequire(Data data);
}