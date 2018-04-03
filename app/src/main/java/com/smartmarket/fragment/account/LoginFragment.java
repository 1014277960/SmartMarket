package com.smartmarket.fragment.account;


import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.common.app.view.BaseFragment;
import com.common.app.view.BasePresenterFragment;
import com.factory.presenter.account.LoginContract;
import com.factory.presenter.account.LoginPresenter;
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
public class LoginFragment extends BasePresenterFragment<LoginContract.Presenter> implements LoginContract.View {

    private AccountActivity mRootActivity;

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;

    @BindView(R.id.submit_login)
    TextView mSubmit;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mRootActivity = (AccountActivity) context;
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @OnClick(R.id.submit_login)
    public void onSubmit() {
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        mPresenter.login(phone, password);
    }

    @OnClick(R.id.text_go_register)
    public void onGoRegister() {
        mRootActivity.triggerView();
    }

    @Override
    public void showError(String errMsg) {
        super.showError(errMsg);

        mSubmit.setText("登陆");

        mPassword.setEnabled(true);
        mPhone.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        mSubmit.setText("loading...");

        mPassword.setEnabled(false);
        mPhone.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
