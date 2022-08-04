package dev.azsoft.wifiattendancesystemadmin.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import dev.azsoft.wifiattendancesystemadmin.R;

public class ExtendedButton extends RelativeLayout {
    private TypedArray attributes;
    private Context mContext;
    private MaterialButton extBtn;
    private RelativeLayout extRL;
    private CircularProgressIndicator extCircularProgressIndicator;
    private LayoutParams layoutParams;
    private static final int DEFAULT_VALUE = 168;


    public ExtendedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;

        inflate(context, R.layout.extended_button, this);

        attributes = context.obtainStyledAttributes(attrs, R.styleable.ExtendedButton);
        extBtn = findViewById(R.id.extended_btn);
        extRL = findViewById(R.id.extended_rl);
        extCircularProgressIndicator = findViewById(R.id.extended_circular_progress_indicator);

        CharSequence text = getText();
        setText(text);

        int textColor = getTextColor();
        setTextColor(textColor);

        boolean isProcessing = isProcessing();
        setProcessing(isProcessing);

        int trackThickness = getTrackThickness();
        setTrackThickness(trackThickness);

        int indicatorColor = getIndicatorColor();
        setIndicatorColor(indicatorColor);

        int cornerRadius = getCornerRadius();
        setCornerRadius(cornerRadius);

        int indicatorSize = getIndicatorSize();
        setIndicatorSize(indicatorSize);

        int backgroundTint = getBackgroundTint();
        setBackgroundTint(backgroundTint);

        extSetLayoutParams();

        attributes.recycle();

    }

    public CharSequence getText() {
        CharSequence value = attributes.getText(R.styleable.ExtendedButton_android_text);
        return value != null ? value : "Button";
    }

    public void setText(CharSequence text) {
        extBtn.setText(text);
    }

    public int getTextColor() {
        return attributes.getInt(R.styleable.ExtendedButton_android_textColor, getThemeColor(R.color.color_on_primary));
    }

    public void setTextColor(int textColor) {
        extBtn.setTextColor(textColor);
    }

    public boolean isProcessing() {
        return attributes.getBoolean(R.styleable.ExtendedButton_isProcessing, false);
    }

    public void setProcessing(boolean processing) {
        if (processing) {
            extBtn.setClickable(false);
            extCircularProgressIndicator.setVisibility(VISIBLE);
        } else {
            extBtn.setClickable(true);
            extCircularProgressIndicator.setVisibility(GONE);
        }
    }

    public int getCornerRadius() {
        return attributes.getInt(R.styleable.ExtendedButton_extCornerRadius, (int) getResources().getDimension(R.dimen.default_radius));
    }

    public void setCornerRadius(int cornerRadius) {
        extBtn.setCornerRadius(cornerRadius);
    }

    public int getIndicatorSize() {
        return attributes.getInt(R.styleable.ExtendedButton_extIndicatorSize, (int) getResources().getDimension(R.dimen.default_indicatorSize));
    }

    public void setIndicatorSize(int indicatorSize) {
        extCircularProgressIndicator.setIndicatorSize(indicatorSize);
    }

    public int getTrackThickness() {
        return attributes.getInt(R.styleable.ExtendedButton_extIndicatorSize, (int) getResources().getDimension(R.dimen.default_trackThickness));
    }

    public void setTrackThickness(int trackThickness) {
        extCircularProgressIndicator.setTrackThickness(trackThickness);

    }

    public int getBackgroundTint() {
        return attributes.getInt(R.styleable.ExtendedButton_android_backgroundTint, getThemeColor(R.color.color_primary));
    }

    public void setBackgroundTint(int backgroundTint) {
        extBtn.setBackgroundTintList(ColorStateList.valueOf(backgroundTint));
    }

    public int getIndicatorColor() {
        return attributes.getInt(R.styleable.ExtendedButton_extIndicatorColor, getThemeColor(R.color.color_on_primary));
    }

    public void setIndicatorColor(int indicatorColor) {
        extCircularProgressIndicator.setIndicatorColor(indicatorColor);
    }


    public void setExtLayoutParams(LayoutParams layoutParams) {
        layoutParams.setMargins(0, 0, 0, 0);
        extRL.setLayoutParams(layoutParams);
    }

    private void extSetLayoutParams() {
        System.out.println("ExtendedButton.init: width " + attributes.getString(R.styleable.ExtendedButton_android_layout_width));
        System.out.println("ExtendedButton.init: height " + attributes.getString(R.styleable.ExtendedButton_android_layout_height));
        String height = attributes.getString(R.styleable.ExtendedButton_android_layout_height);
        String width = attributes.getString(R.styleable.ExtendedButton_android_layout_width);
        LayoutParams layoutParams;
//        LayoutParams.WRAP_CONTENT = -2
//        LayoutParams.MATCH_PARENT = -1
        if ((width.equals("-1") || width.equals("-2")) && (!height.equals("-2") && !height.equals("-1"))) {
            int pixelHeight = attributes.getDimensionPixelSize(R.styleable.ExtendedButton_android_layout_height, LayoutParams.MATCH_PARENT);
            if (width.equals("-2")) {
                extBtn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, pixelHeight));
            }
            layoutParams = new LayoutParams(Integer.parseInt(width), pixelHeight);
        } else if ((!width.equals("-1") && !width.equals("-2")) && (height.equals("-2") || height.equals("-1"))) {
            int pixelWidth = attributes.getDimensionPixelSize(R.styleable.ExtendedButton_android_layout_width, LayoutParams.MATCH_PARENT);
            layoutParams = new LayoutParams(pixelWidth, height.equals("-2") ? DEFAULT_VALUE : Integer.parseInt(height));
        } else if ((width.equals("-1") || width.equals("-2")) && (height.equals("-2") || height.equals("-1"))) {
            layoutParams = new LayoutParams(Integer.parseInt(width), height.equals("-2") ? DEFAULT_VALUE : Integer.parseInt(height));
            if (width.equals("-2")) {
                extBtn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, height.equals("-2") ? LayoutParams.MATCH_PARENT : Integer.parseInt(height)));
            }
        } else {
            System.out.println("ExtendedButton.extSetLayoutParams");
            int pixelHeight = attributes.getDimensionPixelSize(R.styleable.ExtendedButton_android_layout_height, LayoutParams.MATCH_PARENT);
            int pixelWidth = attributes.getDimensionPixelSize(R.styleable.ExtendedButton_android_layout_width, LayoutParams.MATCH_PARENT);
            layoutParams = new LayoutParams(pixelWidth, pixelHeight);
        }
        layoutParams.setMargins(0, 0, 0, 0);
        extRL.setLayoutParams(layoutParams);
    }

    private OnClickListener listener;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (listener != null) listener.onClick(this);
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (listener != null) listener.onClick(this);
        }
        return super.dispatchKeyEvent(event);
    }


    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    private int getThemeColor(int colorId) {
        if (colorId == R.color.color_primary) {
            return MaterialColors.getColor(mContext, com.google.android.material.R.attr.colorPrimary, mContext.getResources().getColor(R.color.color_primary, mContext.getTheme()));
        } else {
            return MaterialColors.getColor(mContext, com.google.android.material.R.attr.colorOnPrimary, mContext.getResources().getColor(R.color.color_on_primary, mContext.getTheme()));
        }
    }
}
