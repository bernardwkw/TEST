package my.com.sains.teams.slider;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import my.com.sains.teams.R;

import static my.com.sains.teams.slider.Util.spToPx;


/**
 * @author Kizito Nwose
 */

public class SlideView extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {

    protected Slider slider;
    protected Drawable slideBackground;
    protected Drawable buttonBackground;
    protected Drawable buttonImage;
    protected Drawable buttonImageDisabled;
    protected TextView slideTextView, slideTextView2;
    protected LayerDrawable buttonLayers;
//    protected ColorStateList slideBackgroundColor;
    protected Drawable slideBackgroundColor;

    protected ColorStateList buttonBackgroundColor;
    protected boolean animateSlideText;

    public SlideView(Context context) {
        super(context);
        init(null, 0);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.sv_slide_view, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.slide_view_bg));
        } else {
            setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.slide_view_bg));
        }
        slideTextView = (TextView) findViewById(R.id.slideText);
        slideTextView2 = (TextView) findViewById(R.id.slideText2);
        slider = (Slider) findViewById(R.id.slider);
        slider.setOnSeekBarChangeListener(this);
        slideBackground = getBackground();
        buttonLayers = (LayerDrawable) slider.getThumb();
        buttonBackground = buttonLayers.findDrawableByLayerId(R.id.buttonBackground);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SlideView,
                defStyle, defStyle);

        int strokeColor;
        float slideTextSize = spToPx(16, getContext());
        float slideTextSize2 = spToPx(16, getContext());
        String slideText , slideText2;
        boolean reverseSlide;
        ColorStateList sliderTextColor, sliderTextColor2;
        try {
            animateSlideText = a.getBoolean(R.styleable.SlideView_animateSlideText, true);
            reverseSlide = a.getBoolean(R.styleable.SlideView_reverseSlide, false);
            strokeColor = a.getColor(R.styleable.SlideView_strokeColor, ContextCompat.
                    getColor(getContext(), R.color.stroke_color_default));


            slideText = a.getString(R.styleable.SlideView_slideText);
            slideText2 = a.getString(R.styleable.SlideView_slideText2);

            sliderTextColor = a.getColorStateList(R.styleable.SlideView_slideTextColor);
            sliderTextColor2 = a.getColorStateList(R.styleable.SlideView_slideTextColor2);

            slideTextSize = a.getDimension(R.styleable.SlideView_slideTextSize, slideTextSize);
            slideTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, slideTextSize);

            //added value
            slideTextSize2 = a.getDimension(R.styleable.SlideView_slideTextSize2, slideTextSize2);
            slideTextView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, slideTextSize2);

            setText(slideText);
            setTextColor(sliderTextColor == null ? slideTextView.getTextColors() : sliderTextColor);

            setText2(slideText2);
            setTextColor2(sliderTextColor2 == null ? slideTextView2.getTextColors() : sliderTextColor2);


            int buttonImageId = a.getResourceId(R.styleable.SlideView_buttonImage, R.drawable.ic_chevron_double_right);
            setButtonImage(ContextCompat.getDrawable(getContext(), buttonImageId));
            setButtonImageDisabled(ContextCompat.getDrawable(getContext(), a.getResourceId
                    (R.styleable.SlideView_buttonImageDisabled, buttonImageId)));

            setButtonBackgroundColor(a.getColorStateList(R.styleable.SlideView_buttonBackgroundColor));
//            setSlideBackgroundColor(a.getColorStateList(R.styleable.SlideView_slideBackgroundColor));

            if (a.hasValue(R.styleable.SlideView_strokeColor)) {
                Util.setDrawableStroke(slideBackground, strokeColor);
            }
            if (reverseSlide) {
                slider.setRotation(180);
                LayoutParams params = ((LayoutParams) slideTextView.getLayoutParams());
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                }
                slideTextView.setLayoutParams(params);
            }
        } finally {
            a.recycle();
        }
    }

    public void setTextColor(@ColorInt int color) {
        slideTextView.setTextColor(color);
    }

    public void setTextColor(ColorStateList colors) {
        slideTextView.setTextColor(colors);
    }

    public void setTextColor2(ColorStateList colors2) {
        slideTextView.setTextColor(colors2);
    }


    public void setText(CharSequence text) {
        slideTextView.setText(text);
    }

    public void setText2(CharSequence text2){
        slideTextView2.setText(text2);
    }

    public void setTextSize(int size) { slideTextView.setTextSize(size); }

    public TextView getTextView() { return slideTextView; }

    public void removeText(){
        slideTextView.setVisibility(GONE);
        slideTextView2.setGravity(Gravity.CENTER);
        slider.setVisibility(INVISIBLE);
    }

    public void removeText2(){
        slideTextView2.setVisibility(GONE);
        slideTextView.setGravity(Gravity.CENTER);
        slider.setVisibility(INVISIBLE);
    }

    public void setButtonImage(Drawable image) {
        buttonImage = image;
        buttonLayers.setDrawableByLayerId(R.id.buttonImage, image);
    }

    public void setButtonImageDisabled(Drawable image) {
        buttonImageDisabled = image;
    }


    public void setButtonBackgroundColor(ColorStateList color) {
        buttonBackgroundColor = color;
        Util.setDrawableColor(buttonBackground, color.getDefaultColor());
    }

//    public void setSlideBackgroundColor(ColorStateList color) {
//        slideBackgroundColor = color;
//        Util.setDrawableColor(slideBackground, color.getDefaultColor());
//    }

    public void setSlideBackgroundColor(int drawable) {
        //Util.setDrawableColor(slideBackground, color);
        setBackground(ContextCompat.getDrawable(getContext(), drawable));
    }

    public Slider getSlider() {
        return slider;
    }

    public void setOnSlideCompleteListener(OnSlideCompleteListener listener) {
        slider.setOnSlideCompleteListenerInternal(listener, this);
    }


    public interface OnSlideCompleteListener {
        void onSlideComplete(SlideView slideView, boolean status);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setEnabled(enabled);
        }
        buttonLayers.setDrawableByLayerId(R.id.buttonImage, enabled ? buttonImage :
                buttonImageDisabled == null ? buttonImage : buttonImageDisabled);
        Util.setDrawableColor(buttonBackground, buttonBackgroundColor.getColorForState(
                enabled ? new int[]{android.R.attr.state_enabled} : new int[]{-android.R.attr.state_enabled}
                , ContextCompat.getColor(getContext(), R.color.button_color_default)));


//        Util.setDrawableColor(slideBackground, slideBackgroundColor.getColorForState(
//                enabled ? new int[]{android.R.attr.state_enabled} : new int[]{-android.R.attr.state_enabled}
//                , ContextCompat.getColor(getContext(), R.color.button_color_default)));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        if (animateSlideText) {
//            slideTextView.setAlpha(1 - (progress / 100f));
//        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
