package com.factory.presenter.account;


import com.common.factory.presenter.BaseContract;

/**
 * @author wulinpeng
 * @datetime: 18/1/26 下午3:00
 * @description:
 */
public interface LoginContract {

    interface View extends BaseContract.View<Presenter> {
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        // 发起登陆
        void login(String phone, String password);
    }
}
