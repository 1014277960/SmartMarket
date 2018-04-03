package com.common.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.common.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author wulinpeng
 * @datetime: 17/11/16 下午7:43
 * @description:
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 封装添加图片方法
     * @param requestManager
     * @param resourceId
     * @param url
     */
    public void setup(RequestManager requestManager, int resourceId, String url) {
        if (url == null) {
            url = "";
        }
        requestManager
                .load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate()
                // 直接into(this)会发生第一次加载ok，后面重新回到头像页面虽然不触发set图片的方法，但是图片就会变成默认的
                // 没有找出原因，可能是因为PortraitView是继承CircleView的自定义view
                // .into(this);
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        PortraitView.this.setImageDrawable(resource);
                    }
                });
    }

    public void setup(RequestManager requestManager, String url) {
        this.setup(requestManager, R.drawable.default_portrait, url);
    }
}
