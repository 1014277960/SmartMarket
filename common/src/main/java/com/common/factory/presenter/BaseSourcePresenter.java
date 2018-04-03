package com.common.factory.presenter;

import com.common.factory.data.DBDataSource;

import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 18/2/9 下午2:35
 * @description:
 */
public abstract class BaseSourcePresenter<Data, ViewMode, Source extends DBDataSource<Data>,View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewMode, View >
        implements DBDataSource.SucceedCallback<List<Data>>{

    private Source source;

    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.source = source;
    }

    @Override
    public void start() {
        super.start();
        if (source != null) {
            // 绑定SucceedCallback
            source.load(this);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        source.dispose();
        source = null;
    }

}
