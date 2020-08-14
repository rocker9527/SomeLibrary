package com.licheedev.status;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;

/**
 * Created by lizhiyong on 2015/12/28.
 */
public class TitleBar extends ConstraintLayout {

    public final TextView titleText;
    public final ImageView leftImage;
    public final ImageView rightImage;
    public final TextView leftText;
    public final TextView rightText;
    private final View bottomLine;
    // 标记状态栏位置的参考线
    private Guideline statusGuideline;

    //private int mHeight;

    public static final int TITLE_TEXT = R.id.titleText;
    public static final int LEFT_IMAGE = R.id.leftImage;
    public static final int RIGHT_IMAGE = R.id.rightImage;
    public static final int LEFT_TEXT = R.id.leftText;
    public static final int RIGHT_TEXT = R.id.rightText;
    public static final int BOTTOM_LINE = R.id.bottomLine;
    public static final int STATUS_GUIDELINE = R.id.statusGuideline;

    private int mNewHeightSpec;
    /**
     * 状态栏配置代理
     */
    private StatusHeight mStatusHeight;

    public TitleBar(@NonNull Context context) {
        this(context, null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs,
        @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mStatusHeight = new StatusHeight(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        // 设置是否设配状态栏
        mStatusHeight.setFitStatusBar(
            typedArray.getBoolean(R.styleable.TitleBar_fitStatusBar, false));

        mNewHeightSpec = 0;

        // 在最底层填充一个布局
        int bottomLayout = typedArray.getResourceId(R.styleable.TitleBar_titleBottomLayout, 0);
        if (bottomLayout != 0) {
            LayoutInflater.from(context).inflate(bottomLayout, this, true);
        }

        // 填充标题栏
        View view = LayoutInflater.from(context)
            .inflate(R.layout.view_title_bar_fit_status_bar, this, true);

        titleText = view.findViewById(TITLE_TEXT);
        leftImage = view.findViewById(LEFT_IMAGE);
        rightImage = view.findViewById(RIGHT_IMAGE);
        leftText = view.findViewById(LEFT_TEXT);
        rightText = view.findViewById(RIGHT_TEXT);
        bottomLine = view.findViewById(BOTTOM_LINE);

        statusGuideline = view.findViewById(STATUS_GUIDELINE);

        setupTitleBar(context, typedArray);

        typedArray.recycle();
    }

    private void setupTitleBar(Context context, TypedArray ta) {

        // 配置一下状态栏参考线的位置
        if (mStatusHeight.toFitStatusBar()) {
            statusGuideline.setGuidelineBegin(mStatusHeight.getHeight());
        }

        Resources resources = getResources();

        // 标题
        int textColor =
            getColor(ta, R.styleable.TitleBar_titleTextColor, R.color.default_title_text_color);
        titleText.setTextColor(textColor);

        float titleSize =
            getDimen(ta, R.styleable.TitleBar_titleTextSize, R.dimen.default_title_text_size);
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);

        boolean boldTitleText = ta.getBoolean(R.styleable.TitleBar_titleTextBold, false);
        if (boldTitleText) {
            titleText.setTypeface(titleText.getTypeface(), Typeface.BOLD);
        }

        float titleMaxWidth =
            getDimen(ta, R.styleable.TitleBar_titleMaxWidth, R.dimen.default_title_max_width);
        titleText.setMaxWidth((int) titleMaxWidth);

        String titleTextString = ta.getString(R.styleable.TitleBar_titleText);
        titleText.setText(titleTextString);

        // 下方分隔线
        int lineColor =
            getColor(ta, R.styleable.TitleBar_titleLineColor, R.color.default_title_bottom_line_color);
        bottomLine.setBackgroundColor(lineColor);

        float lineHeight =
            getDimen(ta, R.styleable.TitleBar_titleBottomLineHeight, R.dimen.default_bottom_line_height);
        bottomLine.getLayoutParams().height = (int) lineHeight;

        boolean showLine = ta.getBoolean(R.styleable.TitleBar_titleShowLine, false);

        bottomLine.setVisibility(showLine ? VISIBLE : GONE);

        // 左侧按钮
        int leftDrawableRes = ta.getResourceId(R.styleable.TitleBar_titleLeftDrawable, 0);

        if (leftDrawableRes != 0) {
            leftImage.setImageResource(leftDrawableRes);
        }

        boolean showLeftImage =
            ta.getBoolean(R.styleable.TitleBar_titleShowLeftImage, leftDrawableRes != 0);

        leftImage.setVisibility(showLeftImage ? VISIBLE : GONE);

        // 右侧按钮
        int rightDrawableRes = ta.getResourceId(R.styleable.TitleBar_titleRightDrawable, 0);

        if (rightDrawableRes != 0) {
            rightImage.setImageResource(rightDrawableRes);
        }

        boolean showRightImage =
            ta.getBoolean(R.styleable.TitleBar_titleShowRightImage, rightDrawableRes != 0);

        rightImage.setVisibility(showRightImage ? VISIBLE : GONE);

        // 左右文本按钮边距
        int textPadding = (int) getDimen(ta, R.styleable.TitleBar_textButtonPadding,
            R.dimen.default_text_button_padding);
        if (textPadding > 0) {
            leftText.setPadding(textPadding, 0, textPadding, 0);
            rightText.setPadding(textPadding, 0, textPadding, 0);
        }

        // 按钮文字大小
        float buttonTextSize = resources.getDimension(R.dimen.default_title_button_text_size);
        buttonTextSize = ta.getDimension(R.styleable.TitleBar_titleButtonTextSize, buttonTextSize);

        // 左侧文字
        int leftTextColor = ta.getColor(R.styleable.TitleBar_titleLeftTextColor, textColor);
        leftText.setTextColor(leftTextColor);
        leftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);

        String leftString = ta.getString(R.styleable.TitleBar_titleLeftText);
        leftText.setText(leftString);

        boolean showLeftText =
            ta.getBoolean(R.styleable.TitleBar_titleShowLeftText, !TextUtils.isEmpty(leftString));
        leftText.setVisibility(showLeftText ? VISIBLE : GONE);

        // 右侧文字
        int rightTextColor = ta.getColor(R.styleable.TitleBar_titleRightTextColor, textColor);
        rightText.setTextColor(rightTextColor);

        rightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);

        String rightString = ta.getString(R.styleable.TitleBar_titleRightText);
        rightText.setText(rightString);
        boolean showRightText =
            ta.getBoolean(R.styleable.TitleBar_titleShowRightText, !TextUtils.isEmpty(rightString));

        rightText.setVisibility(showRightText ? VISIBLE : GONE);

        // 按钮背景
        int buttonBg =
            ta.getResourceId(R.styleable.TitleBar_titleImageBg, R.drawable.title_bar_button_bg);

        int textBg = ta.getResourceId(R.styleable.TitleBar_titleTextBg, R.drawable.title_bar_text_bg);

        setImageBackground(buttonBg);
        setTextBackground(textBg);
    }

    float getDimen(TypedArray ta, @StyleableRes int styleRes, @DimenRes int defRes) {
        float dimen = getResources().getDimension(defRes);
        return ta.getDimension(styleRes, dimen);
    }

    int getColor(TypedArray ta, @StyleableRes int styleRes, @ColorRes int defRes) {
        int color = ContextCompat.getColor(getContext(), defRes);
        return ta.getColor(styleRes, color);
    }

    public void setImageBackground(int resId) {
        leftImage.setBackgroundResource(resId);
        rightImage.setBackgroundResource(resId);
    }

    public void setTextBackground(int resId) {
        leftText.setBackgroundResource(resId);
        rightText.setBackgroundResource(resId);
    }

    public void setTitle(@StringRes int resId) {
        titleText.setText(resId);
    }

    public void setTitle(CharSequence title) {
        titleText.setText(title);
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        leftImage.setOnClickListener(l);
        rightImage.setOnClickListener(l);
        leftText.setOnClickListener(l);
        rightText.setOnClickListener(l);
    }

    /**
     * 设置左边按钮的点击事件
     *
     * @param l
     */
    public void setLeftOnClickListener(@Nullable View.OnClickListener l) {
        leftImage.setOnClickListener(l);
        leftText.setOnClickListener(l);
    }

    /**
     * 设置右边按钮的点击事件
     *
     * @param l
     */
    public void setRightOnClickListener(@Nullable View.OnClickListener l) {
        rightImage.setOnClickListener(l);
        rightText.setOnClickListener(l);
    }

    /**
     * 左边按钮退出
     *
     * @param activity
     */
    public void leftExit(final Activity activity) {
        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mStatusHeight.toFitStatusBar() && mNewHeightSpec == 0) {
            int height =
                View.MeasureSpec.getSize(heightMeasureSpec) + mStatusHeight.getHeight();
            mNewHeightSpec = View.MeasureSpec.makeMeasureSpec(height,
                View.MeasureSpec.getMode(heightMeasureSpec));
        }

        if (mNewHeightSpec != 0) {
            super.onMeasure(widthMeasureSpec, mNewHeightSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
