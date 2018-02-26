package my.com.sains.teams.modal;

import android.widget.LinearLayout;
import android.widget.TextView;

import my.com.sains.teams.slider.SlideView;

/**
 * Created by User on 8/12/2017.
 */

public class StatusModal {

    private SlideView slideView;
    private TextView textView;
    private TextView tittleTv;
    private LinearLayout linearLayout;
    private String status;
    private String statusName;

    public SlideView getSlideView() {
        return slideView;
    }

    public void setSlideView(SlideView slideView) {
        this.slideView = slideView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public String getStatus() {
        if (status == null)
            return "N";
        else
            return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public TextView getTittleTv() {
        return tittleTv;
    }

    public void setTittleTv(TextView tittleTv) {
        this.tittleTv = tittleTv;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }
}
