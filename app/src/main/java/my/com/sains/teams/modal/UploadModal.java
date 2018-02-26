package my.com.sains.teams.modal;

import java.util.List;

import my.com.sains.teams.db.MobileDoc;
import my.com.sains.teams.db.MyInspectUpload;

/**
 * Created by User on 19/12/2017.
 */

public class UploadModal {

    private MobileDoc mobileDocMember;
    private boolean isChecked;
    private int specCheckProgress;
    private List<MyInspectUpload> myInspectUploadList;
    private String startTime;
    private String endTime;
    private int totalSpecCheck;
    private int specChecked;

    public MobileDoc getMobileDocMember() {
        return mobileDocMember;
    }

    public void setMobileDocMember(MobileDoc mobileDocMember) {
        this.mobileDocMember = mobileDocMember;
    }

    public List<MyInspectUpload> getMyInspectUploadList() {
        return myInspectUploadList;
    }

    public void setMyInspectUploadList(List<MyInspectUpload> myInspectUploadList) {
        this.myInspectUploadList = myInspectUploadList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getSpecCheckProgress() {
        return specCheckProgress;
    }

    public void setSpecCheckProgress(int specCheckProgress) {
        this.specCheckProgress = specCheckProgress;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTotalSpecCheck() {
        return totalSpecCheck;
    }

    public void setTotalSpecCheck(int totalSpecCheck) {
        this.totalSpecCheck = totalSpecCheck;
    }

    public int getSpecChecked() {
        return specChecked;
    }

    public void setSpecChecked(int specChecked) {
        this.specChecked = specChecked;
    }
}
