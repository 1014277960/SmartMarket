package com.smartmarket.activity.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.common.app.view.BaseActivity;
import com.smartmarket.R;
import com.smartmarket.activity.LaunchActivity;
import com.smartmarket.fragment.account.LoginFragment;
import com.smartmarket.fragment.account.RegisterFragment;

import butterknife.BindView;

/**
 * @author wulinpeng
 * @datetime: 18/1/31 下午4:43
 * @description: 用户界面，负责登陆注册
 */
public class AccountActivity extends BaseActivity {

    private Fragment mCurrentFragment;

    private LoginFragment mLoginFragment;

    private RegisterFragment mRegisterFragment;

    @BindView(R.id.im_bg)
    public ImageView mBg;

    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initView() {
//        super.initView();
        mBg = (ImageView) findViewById(R.id.im_bg);
        mCurrentFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mCurrentFragment)
                .commit();

        // 初始化背景
        Glide.with(this).load(R.drawable.bg_src_tianjin)
                .centerCrop()
                .into(new ViewTarget<ImageView, GlideDrawable>(mBg) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Drawable drawable = resource.getCurrent();
                        drawable = DrawableCompat.wrap(drawable);
                        drawable.setColorFilter(getResources().getColor(R.color.colorAccent),
                                PorterDuff.Mode.SCREEN);
                        this.view.setImageDrawable(drawable);
                    }
                });
    }

    /**
     * 登陆／注册布局切换
     */
    public void triggerView() {
        if (mCurrentFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                mRegisterFragment = new RegisterFragment();
            }
            mCurrentFragment = mRegisterFragment;
        } else {
            mCurrentFragment = mLoginFragment;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mCurrentFragment)
                .commit();

    }
}
