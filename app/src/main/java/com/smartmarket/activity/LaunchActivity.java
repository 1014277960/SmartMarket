package com.smartmarket.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.common.app.view.BaseActivity;
import com.factory.persistence.Account;
import com.smartmarket.R;
import com.smartmarket.activity.account.AccountActivity;

public class LaunchActivity extends BaseActivity {

    public static void show(Context context) {
        context.startActivity(new Intent(context, LaunchActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initData() {
        super.initData();
        // todo 动画
        jump();
    }

    private void jump() {
        if (Account.isLogin()) {
            MainActivity.show(this);
        } else {
            AccountActivity.show(this);
        }
        finish();
    }

}
