package my.com.sains.teams.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "LOG_REGISTER_QUERY".
 */
@Entity
public class LogRegisterQuery {

    @Id
    private String regis_id;
    private String device_id;
    private String office_id;
    private String licensee;
    private String property_mark;
    private String pec_ref_no;
    private String lpi_no;
    private String dpr_ref_no;
    private String ra_ref_no;
    private String harvest_date;
    private String coupe_no;
    private String block_no;
    private String camp_code;
    private String log_serial_no;
    private String species_code;
    private Double length;
    private Double diameter;
    private Double defect_dia;
    private Double net_vol;
    private Double compute_vol;
    private String ra_processing_office;
    private String hammer_mark_no;
    private String place_ra_marking;
    private String trp_ref_no;
    private String name;
    private String tran_mode;
    private String by_name;
    private String rp_no;
    private String to_loc;
    private String frm_loc;
    private String sync_status;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public LogRegisterQuery() {
    }

    public LogRegisterQuery(String regis_id) {
        this.regis_id = regis_id;
    }

    @Generated
    public LogRegisterQuery(String regis_id, String device_id, String office_id, String licensee, String property_mark, String pec_ref_no, String lpi_no, String dpr_ref_no, String ra_ref_no, String harvest_date, String coupe_no, String block_no, String camp_code, String log_serial_no, String species_code, Double length, Double diameter, Double defect_dia, Double net_vol, Double compute_vol, String ra_processing_office, String hammer_mark_no, String place_ra_marking, String trp_ref_no, String name, String tran_mode, String by_name, String rp_no, String to_loc, String frm_loc, String sync_status) {
        this.regis_id = regis_id;
        this.device_id = device_id;
        this.office_id = office_id;
        this.licensee = licensee;
        this.property_mark = property_mark;
        this.pec_ref_no = pec_ref_no;
        this.lpi_no = lpi_no;
        this.dpr_ref_no = dpr_ref_no;
        this.ra_ref_no = ra_ref_no;
        this.harvest_date = harvest_date;
        this.coupe_no = coupe_no;
        this.block_no = block_no;
        this.camp_code = camp_code;
        this.log_serial_no = log_serial_no;
        this.species_code = species_code;
        this.length = length;
        this.diameter = diameter;
        this.defect_dia = defect_dia;
        this.net_vol = net_vol;
        this.compute_vol = compute_vol;
        this.ra_processing_office = ra_processing_office;
        this.hammer_mark_no = hammer_mark_no;
        this.place_ra_marking = place_ra_marking;
        this.trp_ref_no = trp_ref_no;
        this.name = name;
        this.tran_mode = tran_mode;
        this.by_name = by_name;
        this.rp_no = rp_no;
        this.to_loc = to_loc;
        this.frm_loc = frm_loc;
        this.sync_status = sync_status;
    }

    public String getRegis_id() {
        return regis_id;
    }

    public void setRegis_id(String regis_id) {
        this.regis_id = regis_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getLicensee() {
        return licensee;
    }

    public void setLicensee(String licensee) {
        this.licensee = licensee;
    }

    public String getProperty_mark() {
        return property_mark;
    }

    public void setProperty_mark(String property_mark) {
        this.property_mark = property_mark;
    }

    public String getPec_ref_no() {
        return pec_ref_no;
    }

    public void setPec_ref_no(String pec_ref_no) {
        this.pec_ref_no = pec_ref_no;
    }

    public String getLpi_no() {
        return lpi_no;
    }

    public void setLpi_no(String lpi_no) {
        this.lpi_no = lpi_no;
    }

    public String getDpr_ref_no() {
        return dpr_ref_no;
    }

    public void setDpr_ref_no(String dpr_ref_no) {
        this.dpr_ref_no = dpr_ref_no;
    }

    public String getRa_ref_no() {
        return ra_ref_no;
    }

    public void setRa_ref_no(String ra_ref_no) {
        this.ra_ref_no = ra_ref_no;
    }

    public String getHarvest_date() {
        return harvest_date;
    }

    public void setHarvest_date(String harvest_date) {
        this.harvest_date = harvest_date;
    }

    public String getCoupe_no() {
        return coupe_no;
    }

    public void setCoupe_no(String coupe_no) {
        this.coupe_no = coupe_no;
    }

    public String getBlock_no() {
        return block_no;
    }

    public void setBlock_no(String block_no) {
        this.block_no = block_no;
    }

    public String getCamp_code() {
        return camp_code;
    }

    public void setCamp_code(String camp_code) {
        this.camp_code = camp_code;
    }

    public String getLog_serial_no() {
        return log_serial_no;
    }

    public void setLog_serial_no(String log_serial_no) {
        this.log_serial_no = log_serial_no;
    }

    public String getSpecies_code() {
        return species_code;
    }

    public void setSpecies_code(String species_code) {
        this.species_code = species_code;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getDiameter() {
        return diameter;
    }

    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }

    public Double getDefect_dia() {
        return defect_dia;
    }

    public void setDefect_dia(Double defect_dia) {
        this.defect_dia = defect_dia;
    }

    public Double getNet_vol() {
        return net_vol;
    }

    public void setNet_vol(Double net_vol) {
        this.net_vol = net_vol;
    }

    public Double getCompute_vol() {
        return compute_vol;
    }

    public void setCompute_vol(Double compute_vol) {
        this.compute_vol = compute_vol;
    }

    public String getRa_processing_office() {
        return ra_processing_office;
    }

    public void setRa_processing_office(String ra_processing_office) {
        this.ra_processing_office = ra_processing_office;
    }

    public String getHammer_mark_no() {
        return hammer_mark_no;
    }

    public void setHammer_mark_no(String hammer_mark_no) {
        this.hammer_mark_no = hammer_mark_no;
    }

    public String getPlace_ra_marking() {
        return place_ra_marking;
    }

    public void setPlace_ra_marking(String place_ra_marking) {
        this.place_ra_marking = place_ra_marking;
    }

    public String getTrp_ref_no() {
        return trp_ref_no;
    }

    public void setTrp_ref_no(String trp_ref_no) {
        this.trp_ref_no = trp_ref_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTran_mode() {
        return tran_mode;
    }

    public void setTran_mode(String tran_mode) {
        this.tran_mode = tran_mode;
    }

    public String getBy_name() {
        return by_name;
    }

    public void setBy_name(String by_name) {
        this.by_name = by_name;
    }

    public String getRp_no() {
        return rp_no;
    }

    public void setRp_no(String rp_no) {
        this.rp_no = rp_no;
    }

    public String getTo_loc() {
        return to_loc;
    }

    public void setTo_loc(String to_loc) {
        this.to_loc = to_loc;
    }

    public String getFrm_loc() {
        return frm_loc;
    }

    public void setFrm_loc(String frm_loc) {
        this.frm_loc = frm_loc;
    }

    public String getSync_status() {
        return sync_status;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
