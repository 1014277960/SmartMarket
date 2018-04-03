package com.common.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author wulinpeng
 * @datetime: 17/11/12 下午10:26
 * @description: Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();

        if (initArgs(getIntent())) {
            setContentView(getLayoutId());
            initView();
            initData();
        } else {
            finish();
        }

    }

    /**
     * 在界面未初始化之前调用的初始化数据
     */
    protected void initWindows() {

    }

    /**
     * 初始化参数
     * @param intent
     * @return 参数正确返回true，否则返回false
     */
    protected boolean initArgs(Intent intent) {
        return true;
    }

    protected abstract int getLayoutId();

    protected void initView() {
        ButterKnife.bind(this);
    }

    protected void initData() {

    }

    /**
     * 点击导航栏上一页，finish当前界面
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * back触发，考虑fragment的触发
     */
    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment: fragments) {
                if (fragment instanceof BaseFragment && ((BaseFragment) fragment).onBackPressed()) {
                    // fragment处理了back，直接返回
                    return;
                }
            }
        }

        super.onBackPressed();
        finish();
    }
}
