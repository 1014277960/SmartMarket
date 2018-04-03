package com.factory.presenter.account;

import com.common.factory.presenter.BaseContract;

/**
 * @author wulinpeng
 * @datetime: 18/2/14 下午2:30
 * @description:
 */
public interface UpdateInfoContract {

    interface View extends BaseContract.View<Presenter> {
    }

    interface Presenter extends BaseContract.Presenter {
        void uploadPortrait(String path);
    }
}
