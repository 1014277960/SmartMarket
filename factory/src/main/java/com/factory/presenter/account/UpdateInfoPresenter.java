package com.factory.presenter.account;

import com.common.factory.data.DataSource;
import com.common.factory.presenter.BasePresenter;
import com.factory.data.helper.AccountHelper;
import com.factory.model.api.account.UpdateInfoModel;
import com.factory.model.db.User;
import com.factory.net.UploadHelper;
import com.factory.persistence.Account;

/**
 * @author wulinpeng
 * @datetime: 18/2/14 下午2:31
 * @description:
 */
public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter, DataSource.Callback<User> {

    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }

    @Override
    public void onDataLoaded(User user) {
    }

    @Override
    public void onDataLoadFailed(String msg) {
    }

    @Override
    public void uploadPortrait(String path) {
        String url = uploadToOSS(path);
        Account.setPortrait(url);
        uploadInfo(url);
    }

    private String uploadToOSS(String path) {
        String url = UploadHelper.uploadPortrait(path);
        return url;
    }

    private void uploadInfo(String url) {
        UpdateInfoModel model = new UpdateInfoModel(url);
        AccountHelper.update(model, this);
    }


}
