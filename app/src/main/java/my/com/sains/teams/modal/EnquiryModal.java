package my.com.sains.teams.modal;

import android.widget.TextView;

/**
 * Created by User on 16/1/2018.
 */

public class EnquiryModal {

    private String check_status;
    private TextView textview;
    private TextView titteTv;

    public String getCheck_status() {

        if (check_status == null)
            return "N";
        else
            return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }

    public TextView getTextview() {
        return textview;
    }

    public void setTextview(TextView textview) {
        this.textview = textview;
    }

    public TextView getTitteTv() {
        return titteTv;
    }

    public void setTitteTv(TextView titteTv) {
        this.titteTv = titteTv;
    }
}
