package com.factory.presenter.account;

import android.text.TextUtils;

import com.common.factory.data.DataSource;
import com.common.factory.presenter.BasePresenter;
import com.factory.Factory;
import com.factory.data.helper.AccountHelper;
import com.factory.model.api.account.LoginModel;
import com.factory.model.db.User;
import com.factory.persistence.Account;

/**
 * @author wulinpeng
 * @datetime: 18/1/27 下午10:26
 * @description:
 */
public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter, DataSource.Callback<User>{
    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(phone)) {
            getView().showError("invalid parameter");
        } else {
            LoginModel model = new LoginModel(phone, password);
            AccountHelper.login(model, this);
        }
    }

    @Override
    public void onDataLoaded(User user) {
        if (getView() == null) {
            return;
        }
        Factory.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getView().loginSuccess();
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
