package com.smartmarket.fragment.main;


import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.app.widget.PortraitView;
import com.common.app.view.BasePresenterFragment;
import com.factory.persistence.Account;
import com.factory.presenter.account.UpdateInfoContract;
import com.factory.presenter.account.UpdateInfoPresenter;
import com.smartmarket.R;
import com.smartmarket.fragment.media.GalleryFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wulinpeng
 * @datetime: 18/2/3 下午2:43
 * @description: 个人信息fragment
 */
public class ProfileFragment extends BasePresenterFragment<UpdateInfoContract.Presenter> implements UpdateInfoContract.View {

    @BindView(R.id.im_portrait)
    public PortraitView mPortraitView;
    @BindView(R.id.txt_name)
    public TextView mName;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mPortraitView.setup(Glide.with(this), Account.getPortrait());
        mName.setText(Account.getName());
    }

    @OnClick(R.id.im_portrait)
    public void onChoosePortrait() {
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        mPortraitView.setup(Glide.with(getContext()), path);
                        mPresenter.uploadPortrait(path);
                    }
                }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }
}
