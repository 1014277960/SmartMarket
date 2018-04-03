package com.factory.presenter.account;

import android.text.TextUtils;

import com.common.Common;
import com.common.factory.data.DataSource;
import com.common.factory.presenter.BasePresenter;
import com.factory.Factory;
import com.factory.data.helper.AccountHelper;
import com.factory.model.api.account.RegisterModel;
import com.factory.model.db.User;
import com.factory.persistence.Account;


/**
 * @author wulinpeng
 * @datetime: 18/1/26 下午3:14
 * @description:
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User>{
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        start();

        if (!checkPhone(phone)) {
            getView().showError("error phone");
        } else if (name.length() < 2) {
            getView().showError("error name");
        } else if (password.length() < 6) {
            getView().showError("error password");
        } else {
            // 网络请求
            RegisterModel registerModel = new RegisterModel(phone, password, name);
            AccountHelper.register(registerModel, this);
        }
    }

    @Override
    public boolean checkPhone(String phone) {
        return !TextUtils.isEmpty(phone)
                && phone.matches(Common.REGEX_PHONE);
    }

    @Override
    public void onDataLoaded(User user) {
        if (getView() == null) {
            return;
        }
        Factory.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getView().registerSuccess();
            }
        });
    }

    @Override
    public void onDataLoadFailed(final String msg) {
        Factory.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getView().showError(msg);
            }
        });
    }
}
