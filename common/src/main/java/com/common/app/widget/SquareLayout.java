package com.common.app.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author wulinpeng
 * @datetime: 17/11/21 下午5:14
 * @description:
 */
public class SquareLayout extends FrameLayout {

    public SquareLayout(Context context) {
        super(context);
    }

    public SquareLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置高宽一致
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
