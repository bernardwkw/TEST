package my.com.sains.teams.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.db.InspectUpload;
import my.com.sains.teams.db.LogRegister;
import my.com.sains.teams.db.LogRegisterQuery;
import my.com.sains.teams.modal.EnquiryResultModal;
import my.com.sains.teams.modal.UploadSummaryModal;
import my.com.sains.teams.utils.Consts;

/**
 * Created by User on 4/1/2018.
 */

public class EnquiryResultAdapter extends RecyclerView.Adapter<EnquiryResultAdapter.DataObjectHolder> {

    private List<InspectUpload> inspectUploadLists;
    private List<InspectUpload> filterLists;
    private LogRegisterQuery logRegisterQuery;

    public EnquiryResultAdapter(EnquiryResultModal enquiryResult){

        this.inspectUploadLists = enquiryResult.getInspectUpload();
        this.filterLists = enquiryResult.getInspectUpload();
        this.logRegisterQuery = enquiryResult.getLogRegisterQuery();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_summary_downloaded, parent, false);
        EnquiryResultAdapter.DataObjectHolder dataObjectHolder = new EnquiryResultAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

//        EnquiryResultModal result = inspectUploadLists.get(position);
//        LogRegisterQuery logRegisterQuery = result.getLogRegisterQuery();
//        InspectUpload inspectUpload = result.getInspectUpload();
        holder.lpiTv.setText(logRegisterQuery.getLpi_no());
        holder.coupeTv.setText(logRegisterQuery.getCoupe_no());
        holder.blockTv.setText(logRegisterQuery.getBlock_no());
        holder.campTv.setText(logRegisterQuery.getCamp_code());
        holder.specTv.setText(logRegisterQuery.getSpecies_code());
        holder.pmTv.setText(logRegisterQuery.getProperty_mark());
        holder.length.setText(logRegisterQuery.getLength().toString());
        holder.diameter.setText(logRegisterQuery.getDiameter().toString());

        int passColor = Color.parseColor(Consts.PASS_GREEN);
        int failColor = Color.parseColor(Consts.FAIL_RED);
        int neutralColor = Color.parseColor(Consts.NEUTRAL_WHITE);

        InspectUpload inspectUpload = inspectUploadLists.get(position);

        if (inspectUpload.getSpecies_chk().equals("P")){ // if inspection pass
            holder.specContainer.setBackgroundColor(passColor); //green
        }else if (inspectUpload.getSpecies_chk().equals("F")){// if fail
            holder.specContainer.setBackgroundColor(failColor);//red
        }else {
            holder.specContainer.setBackgroundColor(neutralColor);// white if no inspection data
        }

        if (inspectUpload.getJh_hammer_chk().equals("P")){
            holder.jhContainer.setBackgroundColor(passColor);
        }else if (inspectUpload.getJh_hammer_chk().equals("F")){
            holder.jhContainer.setBackgroundColor(failColor);
        }else {
            holder.jhContainer.setBackgroundColor(neutralColor);
        }

        if (inspectUpload.getPro_mark_chk().equals("P")){
            holder.pmContainer.setBackgroundColor(passColor);
        }else if (inspectUpload.getPro_mark_chk().equals("F")){
            holder.pmContainer.setBackgroundColor(failColor);
        } else {
            holder.pmContainer.setBackgroundColor(neutralColor);
        }

        if (inspectUpload.getDiameter_chk().equals("P")){
            holder.diameterContainer.setBackgroundColor(passColor);
        }else if (inspectUpload.getDiameter_chk().equals("F")){
            holder.diameterContainer.setBackgroundColor(failColor);
        }else {
            holder.diameterContainer.setBackgroundColor(neutralColor);
        }

        if (inspectUpload.getLength_chk().equals("P")){
            holder.lengthContainer.setBackgroundColor(passColor);
        }else if (inspectUpload.getLength_chk().equals("F")){
            holder.lengthContainer.setBackgroundColor(failColor);
        }else {
            holder.lengthContainer.setBackgroundColor(neutralColor);
        }

        if (inspectUpload.getLpi_chk().equals("P")){
            holder.lpiContainer.setBackgroundColor(passColor);
        }else if (inspectUpload.getLpi_chk().equals("F")){
            holder.lpiContainer.setBackgroundColor(failColor);
        }else {
            holder.lpiContainer.setBackgroundColor(neutralColor);
        }

//        int selectedColor = Color.parseColor(Consts.SELECTED_PINK);
//        int unselectedColor = Color.parseColor(Consts.UNSELECTED_GREY);
//
//        String specCheck = uploadSummaryModal.getLogRegister().getSpec_check();
//        if(specCheck != null){
//            if (uploadSummaryModal.getLogRegister().getSpec_check().equals("Y")){// if the log is selected for inspection
//                holder.cardView.setCardBackgroundColor(selectedColor);// red color
//            }else {
//                holder.cardView.setCardBackgroundColor(unselectedColor);// grey color
//            }
//        }else {
//            holder.cardView.setCardBackgroundColor(unselectedColor);
//        }

    }

    @Override
    public int getItemCount() {
        return inspectUploadLists.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView lpiTv, coupeTv, blockTv, campTv, specTv, pmTv, length, diameter;
        LinearLayout lpiContainer, jhContainer, specContainer, pmContainer, lengthContainer,diameterContainer;

        public DataObjectHolder(View itemView) {
            super(itemView);
            lpiTv = itemView.findViewById(R.id.lpi_tv);
            coupeTv = itemView.findViewById(R.id.coupe_tv);
            blockTv = itemView.findViewById(R.id.block_tv);
            campTv = itemView.findViewById(R.id.camp_tv);
            specTv = itemView.findViewById(R.id.spec_tv);
            pmTv = itemView.findViewById(R.id.pm_tv);
            length = itemView.findViewById(R.id.length_tv);
            diameter= itemView.findViewById(R.id.diameter_tv);
            cardView = itemView.findViewById(R.id.downloaded_summary_card_view);

            lpiContainer = itemView.findViewById(R.id.lpi_container);
            jhContainer = itemView.findViewById(R.id.jh_container);
            specContainer = itemView.findViewById(R.id.spec_container);
            pmContainer = itemView.findViewById(R.id.pm_container);
            lengthContainer = itemView.findViewById(R.id.length_container);
            diameterContainer = itemView.findViewById(R.id.diameter_container);
        }

    }

}
