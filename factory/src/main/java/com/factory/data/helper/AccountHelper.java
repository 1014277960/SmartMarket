package com.factory.data.helper;

import android.util.Log;

import com.common.factory.data.DataSource;
import com.factory.model.api.RspModel;
import com.factory.model.api.account.AccountRspModel;
import com.factory.model.api.account.LoginModel;
import com.factory.model.api.account.RegisterModel;
import com.factory.model.api.account.UpdateInfoModel;
import com.factory.model.card.UserCard;
import com.factory.model.db.User;
import com.factory.net.NetWork;
import com.factory.net.RemoteService;
import com.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wulinpeng
 * @datetime: 18/2/13 下午2:02
 * @description:
 */
public class AccountHelper {

    public static void register(final RegisterModel model, final DataSource.Callback<User> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        call.enqueue(new AccountRspCallback(callback));
    }

    public static void login(final LoginModel model, final DataSource.Callback<User> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        call.enqueue(new AccountRspCallback(callback));
    }

    public static void update(final UpdateInfoModel model, final DataSource.Callback<User> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<AccountRspModel>> call = service.update(model);
        call.enqueue(new AccountRspCallback(callback));
    }

    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {

        private DataSource.Callback<User> callback;

        public AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                AccountRspModel accountRspModel = rspModel.getResult();
                UserCard user = accountRspModel.getUserCard();
                // 数据库写入，缓存绑定
                DBHelper.getInstance().save(User.class, user.buildUser());
                Account.saveUser(accountRspModel);
                if (callback != null) {
                    callback.onDataLoaded(user.buildUser());
                }
            } else {
                // todo 对rspModel中的code解析，判断错误类型

            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            if (callback != null) {
                callback.onDataLoadFailed("net work error");
            }
        }
    }
}
