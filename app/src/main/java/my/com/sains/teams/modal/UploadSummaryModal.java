package my.com.sains.teams.modal;

import my.com.sains.teams.db.LogRegister;
import my.com.sains.teams.db.MyInspectUpload;

/**
 * Created by User on 8/1/2018.
 */

public class UploadSummaryModal {

    private LogRegister logRegister;
    private MyInspectUpload myInspectUpload;

    public LogRegister getLogRegister() {
        return logRegister;
    }

    public void setLogRegister(LogRegister logRegister) {
        this.logRegister = logRegister;
    }

    public MyInspectUpload getMyInspectUpload() {
        return myInspectUpload;
    }

    public void setMyInspectUpload(MyInspectUpload myInspectUpload) {
        this.myInspectUpload = myInspectUpload;
    }
}
