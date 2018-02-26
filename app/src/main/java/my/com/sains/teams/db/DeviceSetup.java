package my.com.sains.teams.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "DEVICE_SETUP".
 */
@Entity
public class DeviceSetup {

    @Id
    private String device_id;
    private String device_name;
    private String device_model;
    private String device_serial_no;
    private String mobile_id;
    private String created_by;
    private String created_date;
    private String modified_by;
    private String modified_date;
    private String modified_src;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public DeviceSetup() {
    }

    public DeviceSetup(String device_id) {
        this.device_id = device_id;
    }

    @Generated
    public DeviceSetup(String device_id, String device_name, String device_model, String device_serial_no, String mobile_id, String created_by, String created_date, String modified_by, String modified_date, String modified_src) {
        this.device_id = device_id;
        this.device_name = device_name;
        this.device_model = device_model;
        this.device_serial_no = device_serial_no;
        this.mobile_id = mobile_id;
        this.created_by = created_by;
        this.created_date = created_date;
        this.modified_by = modified_by;
        this.modified_date = modified_date;
        this.modified_src = modified_src;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }

    public String getDevice_serial_no() {
        return device_serial_no;
    }

    public void setDevice_serial_no(String device_serial_no) {
        this.device_serial_no = device_serial_no;
    }

    public String getMobile_id() {
        return mobile_id;
    }

    public void setMobile_id(String mobile_id) {
        this.mobile_id = mobile_id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getModified_src() {
        return modified_src;
    }

    public void setModified_src(String modified_src) {
        this.modified_src = modified_src;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
