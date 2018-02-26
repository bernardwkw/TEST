package my.com.sains.teams.slider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class Slider extends SeekBar {

    private Drawable thumb;
    private SlideView.OnSlideCompleteListener listener;
    private SlideView slideView;

    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
        setProgress(50);
    }

    @Override
    public void setThumb(Drawable thumb) {
        this.thumb = thumb;
        super.setThumb(thumb);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (thumb.getBounds().contains((int) event.getX(), (int) event.getY())) {
                // This fixes an issue where the parent view (e.g ScrollView) receives
                // touch events along with the SlideView
                getParent().requestDisallowInterceptTouchEvent(true);
                super.onTouchEvent(event);
            } else {
                return false;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getProgress() > 85) {
                if (listener != null) listener.onSlideComplete(slideView, true);
            }else if (getProgress()<15){
                if (listener != null) listener.onSlideComplete(slideView, false);
            }
            getParent().requestDisallowInterceptTouchEvent(false);
            setProgress(50);
        } else
            super.onTouchEvent(event);

        return true;
    }

    void setOnSlideCompleteListenerInternal(SlideView.OnSlideCompleteListener listener, SlideView slideView) {
        this.listener = listener;
        this.slideView = slideView;
    }

    @Override
    public Drawable getThumb() {
        // getThumb method was added in SDK16 but our minSDK is 14
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return super.getThumb();
        } else {
            return thumb;
        }
    }
}
