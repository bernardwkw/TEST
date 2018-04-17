package my.com.sains.teams.modal;

import java.util.List;

import my.com.sains.teams.db.InspectUpload;
import my.com.sains.teams.db.LogRegisterQuery;

/**
 * Created by User on 27/3/2018.
 */

public class EnquiryResultModal {

    private List<InspectUpload> inspectUpload;
    private LogRegisterQuery logRegisterQuery;

    public List<InspectUpload> getInspectUpload() {
        return inspectUpload;
    }

    public void setInspectUpload(List<InspectUpload> inspectUpload) {
        this.inspectUpload = inspectUpload;
    }

    public LogRegisterQuery getLogRegisterQuery() {
        return logRegisterQuery;
    }

    public void setLogRegisterQuery(LogRegisterQuery logRegisterQuery) {
        this.logRegisterQuery = logRegisterQuery;
    }
}
