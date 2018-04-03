package com.factory.presenter.account;


import com.common.factory.presenter.BaseContract;

/**
 * @author wulinpeng
 * @datetime: 18/1/26 下午2:56
 * @description:
 */
public interface RegisterContract {
    interface View extends BaseContract.View<Presenter> {
        void registerSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        // 发起注册
        void register(String phone, String name, String password);

        boolean checkPhone(String phone);
    }
}
