package com.licheedev.status;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * 一个高度等于状态栏高度的View
 */
public class StatusHolderView extends View {

    private int mStatusHeight;

    /**
     * 一个高度等于状态栏高度的View
     *
     * @param context
     */
    public StatusHolderView(Context context) {
        super(context);
        init(context);
    }

    /**
     * 一个高度等于状态栏高度的View
     *
     * @param context
     * @param attrs
     */
    public StatusHolderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 一个高度等于状态栏高度的View
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public StatusHolderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 一个高度等于状态栏高度的View
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StatusHolderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 一个高度等于状态栏高度的View
     *
     * @param context
     */
    private void init(Context context) {

        StatusHeight statusHeight = new StatusHeight(context);
        mStatusHeight = statusHeight.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int newHeightSpec;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            newHeightSpec = MeasureSpec.makeMeasureSpec(mStatusHeight, MeasureSpec.EXACTLY);
        } else {
            newHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, newHeightSpec);
    }
}
