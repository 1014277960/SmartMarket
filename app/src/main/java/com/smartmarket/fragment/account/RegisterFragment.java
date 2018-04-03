package com.smartmarket.fragment.account;


import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.common.app.view.BaseFragment;
import com.common.app.view.BasePresenterFragment;
import com.factory.presenter.account.RegisterContract;
import com.factory.presenter.account.RegisterPresenter;
import com.smartmarket.R;
import com.smartmarket.activity.MainActivity;
import com.smartmarket.activity.account.AccountActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wulinpeng
 * @datetime: 18/1/31 下午4:43
 * @description:
 */
public class RegisterFragment extends BasePresenterFragment<RegisterContract.Presenter> implements RegisterContract.View  {

    private AccountActivity mRootActivity;

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.edit_password)
    EditText mPassword;

    @BindView(R.id.submit_register)
    TextView mSubmit;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mRootActivity = (AccountActivity) context;
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @OnClick(R.id.submit_register)
    public void onSubmit() {
        String phone = mPhone.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();
        mPresenter.register(phone, name, password);
    }


    @OnClick(R.id.text_go_login)
    public void onGoLogin() {
        mRootActivity.triggerView();
    }

    @Override
    public void showError(String errMsg) {
        super.showError(errMsg);

        mSubmit.setText("注册");

        mPassword.setEnabled(true);
        mName.setEnabled(true);
        mPhone.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        mSubmit.setText("loading...");

        mPassword.setEnabled(false);
        mName.setEnabled(false);
        mPhone.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void registerSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
