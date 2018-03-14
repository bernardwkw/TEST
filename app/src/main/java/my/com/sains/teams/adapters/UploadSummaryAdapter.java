package my.com.sains.teams.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import my.com.sains.teams.modal.UploadSummaryModal;
import my.com.sains.teams.utils.Consts;

/**
 * Created by User on 4/1/2018.
 */

public class UploadSummaryAdapter extends RecyclerView.Adapter<UploadSummaryAdapter.DataObjectHolder>
        implements Filterable{

    private List<UploadSummaryModal> uploadSummaryModalLists;
    private List<UploadSummaryModal> filterLists;

    public UploadSummaryAdapter(List<UploadSummaryModal> uploadSummaryModalList){

        this.uploadSummaryModalLists = uploadSummaryModalList;
        this.filterLists = uploadSummaryModalList;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_summary_upload, parent, false);
        UploadSummaryAdapter.DataObjectHolder dataObjectHolder = new UploadSummaryAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        UploadSummaryModal uploadSummaryModal = uploadSummaryModalLists.get(position);
        holder.lpiTv.setText(uploadSummaryModal.getLogRegister().getLpi_no());
//        holder.longTv.setText(uploadSummaryModal.getMyInspectUpload().getGps_long().toString());
//        holder.latTv.setText(uploadSummaryModal.getMyInspectUpload().getGps_lat().toString());
        holder.jhTv.setText(uploadSummaryModal.getLogRegister().getCamp_code());
        holder.specTv.setText(uploadSummaryModal.getLogRegister().getSpecies_code());
        holder.pmTv.setText(uploadSummaryModal.getLogRegister().getPro_mark_reg_no());
        holder.length.setText(uploadSummaryModal.getLogRegister().getLength().toString());
        holder.diameter.setText(uploadSummaryModal.getLogRegister().getDiameter().toString());
        holder.remarkTv.setText(uploadSummaryModal.getMyInspectUpload().getRemarks());
        holder.userIdTv.setText(uploadSummaryModal.getMyInspectUpload().getUser_login_id());

        int passColor = Color.parseColor(Consts.PASS_GREEN);
        int failColor = Color.parseColor(Consts.FAIL_RED);
        int neutralColor = Color.parseColor(Consts.NEUTRAL_WHITE);

        if (uploadSummaryModal.getMyInspectUpload().getSpecies_chk().equals("P")){ // if inspection pass
            holder.specContainer.setBackgroundColor(passColor); //green
        }else if (uploadSummaryModal.getMyInspectUpload().getSpecies_chk().equals("F")){// if fail
            holder.specContainer.setBackgroundColor(failColor);//red
        }else {
            holder.specContainer.setBackgroundColor(neutralColor);// white if no inspection data
        }

        if (uploadSummaryModal.getMyInspectUpload().getJh_hammer_chk().equals("P")){
            holder.jhContainer.setBackgroundColor(passColor);
        }else if (uploadSummaryModal.getMyInspectUpload().getJh_hammer_chk().equals("F")){
            holder.jhContainer.setBackgroundColor(failColor);
        }else {
            holder.jhContainer.setBackgroundColor(neutralColor);
        }

        if (uploadSummaryModal.getMyInspectUpload().getPro_mark_chk().equals("P")){
            holder.pmContainer.setBackgroundColor(passColor);
        }else if (uploadSummaryModal.getMyInspectUpload().getPro_mark_chk().equals("F")){
            holder.pmContainer.setBackgroundColor(failColor);
        } else {
            holder.pmContainer.setBackgroundColor(neutralColor);
        }

        if (uploadSummaryModal.getMyInspectUpload().getDiameter_chk().equals("P")){
            holder.diameterContainer.setBackgroundColor(passColor);
        }else if (uploadSummaryModal.getMyInspectUpload().getDiameter_chk().equals("F")){
            holder.diameterContainer.setBackgroundColor(failColor);
        }else {
            holder.diameterContainer.setBackgroundColor(neutralColor);
        }

        if (uploadSummaryModal.getMyInspectUpload().getLength_chk().equals("P")){
            holder.lengthContainer.setBackgroundColor(passColor);
        }else if (uploadSummaryModal.getMyInspectUpload().getLength_chk().equals("F")){
            holder.lengthContainer.setBackgroundColor(failColor);
        }else {
            holder.lengthContainer.setBackgroundColor(neutralColor);
        }

        if (uploadSummaryModal.getMyInspectUpload().getLpi_chk().equals("P")){
            holder.lpiContainer.setBackgroundColor(passColor);
        }else if (uploadSummaryModal.getMyInspectUpload().getLpi_chk().equals("F")){
            holder.lpiContainer.setBackgroundColor(failColor);
        }else {
            holder.lpiContainer.setBackgroundColor(neutralColor);
        }

        int selectedColor = Color.parseColor(Consts.SELECTED_PINK);
        int unselectedColor = Color.parseColor(Consts.UNSELECTED_GREY);

        String specCheck = uploadSummaryModal.getLogRegister().getSpec_check();
        if(specCheck != null){
            if (uploadSummaryModal.getLogRegister().getSpec_check().equals("Y")){// if the log is selected for inspection
                holder.cardView.setCardBackgroundColor(selectedColor);// red color
            }else {
                holder.cardView.setCardBackgroundColor(unselectedColor);// grey color
            }
        }else {
            holder.cardView.setCardBackgroundColor(unselectedColor);
        }
    }

    @Override
    public int getItemCount() {
        return uploadSummaryModalLists.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView lpiTv, longTv, latTv, jhTv, specTv, pmTv, length, diameter,
                userIdTv, remarkTv;
        LinearLayout lpiContainer, jhContainer, specContainer, pmContainer, lengthContainer,diameterContainer;


        public DataObjectHolder(View itemView) {
            super(itemView);
            lpiTv = itemView.findViewById(R.id.lpi_tv);
//            longTv = itemView.findViewById(R.id.long_tv);
//            latTv = itemView.findViewById(R.id.lat_tv);
            jhTv = itemView.findViewById(R.id.jh_tv);
            specTv = itemView.findViewById(R.id.spec_tv);
            pmTv = itemView.findViewById(R.id.pm_tv);
            length = itemView.findViewById(R.id.length_tv);
            diameter= itemView.findViewById(R.id.diameter_tv);
            remarkTv = itemView.findViewById(R.id.remarks_tv);
            userIdTv = itemView.findViewById(R.id.user_tv);
            cardView = itemView.findViewById(R.id.upload_summary_card_view);

            lpiContainer = itemView.findViewById(R.id.lpi_container);
            jhContainer = itemView.findViewById(R.id.jh_container);
            specContainer = itemView.findViewById(R.id.spec_container);
            pmContainer = itemView.findViewById(R.id.pm_container);
            lengthContainer = itemView.findViewById(R.id.length_container);
            diameterContainer = itemView.findViewById(R.id.diameter_container);
        }

    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            List<UploadSummaryModal> tempList = new ArrayList<>();

            uploadSummaryModalLists = filterLists;

            if(charSequence != null && charSequence.length()>0
                    && !charSequence.equals("pass") && !charSequence.equals("fail")){
                for (UploadSummaryModal uploadSummaryModal: uploadSummaryModalLists){

                    if (uploadSummaryModal.getLogRegister().getSpecies_code().toUpperCase().contains(charSequence)
                            ||uploadSummaryModal.getLogRegister().getCoupe_no().toUpperCase().contains(charSequence)
                            ||uploadSummaryModal.getLogRegister().getBlock_no().toUpperCase().contains(charSequence)
                            ||uploadSummaryModal.getLogRegister().getCamp_code().toUpperCase().contains(charSequence)
                            ||uploadSummaryModal.getLogRegister().getLpi_no().toUpperCase().contains(charSequence)
                            ||uploadSummaryModal.getLogRegister().getLog_serial_no().toUpperCase().contains(charSequence)
                            ||uploadSummaryModal.getLogRegister().getSpecies_code().toUpperCase().contains(charSequence)
                            ||uploadSummaryModal.getLogRegister().getPro_mark_reg_no().toUpperCase().contains(charSequence)){

                        tempList.add(uploadSummaryModal);
                    }
                }

                filterResults.values = tempList;
                filterResults.count = tempList.size();
            } else if (charSequence.equals("pass")){

                for (UploadSummaryModal uploadSummaryModal: uploadSummaryModalLists){

                    if (!uploadSummaryModal.getMyInspectUpload().getLpi_chk().equals("F")
                            && !uploadSummaryModal.getMyInspectUpload().getLength_chk().equals("F")
                            && !uploadSummaryModal.getMyInspectUpload().getDiameter_chk().equals("F")
                            && !uploadSummaryModal.getMyInspectUpload().getPro_mark_chk().equals("F")
                            && !uploadSummaryModal.getMyInspectUpload().getJh_hammer_chk().equals("F")
                            && !uploadSummaryModal.getMyInspectUpload().getSpecies_chk().equals("F")){

                        tempList.add(uploadSummaryModal);
                    }
                }
                filterResults.values = tempList;
                filterResults.count = tempList.size();

            } else if(charSequence.equals("fail")){

                for (UploadSummaryModal uploadSummaryModal: uploadSummaryModalLists){

                    if (uploadSummaryModal.getMyInspectUpload().getLpi_chk().equals("F")
                            || uploadSummaryModal.getMyInspectUpload().getLength_chk().equals("F")
                            || uploadSummaryModal.getMyInspectUpload().getDiameter_chk().equals("F")
                            || uploadSummaryModal.getMyInspectUpload().getPro_mark_chk().equals("F")
                            || uploadSummaryModal.getMyInspectUpload().getJh_hammer_chk().equals("F")
                            || uploadSummaryModal.getMyInspectUpload().getSpecies_chk().equals("F")){

                        tempList.add(uploadSummaryModal);
                    }
                }
                filterResults.values = tempList;
                filterResults.count = tempList.size();

            } else {
                filterResults.values = filterLists;
                filterResults.count = filterLists.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            uploadSummaryModalLists = (List<UploadSummaryModal>) filterResults.values;
            notifyDataSetChanged();

        }
    };
}
