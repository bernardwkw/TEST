package my.com.sains.teams.modal;

import my.com.sains.teams.db.MobileDoc;

/**
 * Created by User on 26/12/2017.
 */

public class DownloadModal {

    private MobileDoc mobileDoc;
    private boolean isDownloaded;
    private boolean isSelected;

    public MobileDoc getMobileDoc() {
        return mobileDoc;
    }

    public void setMobileDoc(MobileDoc mobileDoc) {
        this.mobileDoc = mobileDoc;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
