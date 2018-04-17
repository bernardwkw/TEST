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
import my.com.sains.teams.db.InspectUpload;
import my.com.sains.teams.db.LogRegisterQuery;
import my.com.sains.teams.utils.Consts;

/**
 * Created by User on 4/1/2018.
 */

public class EnquiryResultAdapter extends RecyclerView.Adapter<EnquiryResultAdapter.DataObjectHolder>
        implements Filterable {

    private List<InspectUpload> inspectUploadLists;
    private List<InspectUpload> filterLists;
    private LogRegisterQuery logRegisterQuery;

    public EnquiryResultAdapter(List<InspectUpload> inspectUploadLists,  LogRegisterQuery logRegisterQuery){

        this.inspectUploadLists = new ArrayList<>();
        this.filterLists = new ArrayList<>();
        this.inspectUploadLists.addAll(inspectUploadLists) ;
        this.filterLists.addAll(inspectUploadLists);

        this.logRegisterQuery = logRegisterQuery;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_summary_enquiry_result, parent, false);
        EnquiryResultAdapter.DataObjectHolder dataObjectHolder = new EnquiryResultAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        holder.lpiTv.setText(logRegisterQuery.getLpi_no());
        holder.jhTv.setText(logRegisterQuery.getCoupe_no());
        holder.specTv.setText(logRegisterQuery.getSpecies_code());
        holder.pmTv.setText(logRegisterQuery.getProperty_mark());
        holder.length.setText(logRegisterQuery.getLength().toString());
        holder.diameter.setText(logRegisterQuery.getDiameter().toString());

        int passColor = Color.parseColor(Consts.PASS_GREEN);
        int failColor = Color.parseColor(Consts.FAIL_RED);
        int neutralColor = Color.parseColor(Consts.NEUTRAL_WHITE);

        InspectUpload inspectUpload = inspectUploadLists.get(position);

        if (inspectUpload.getSpecies_chk()!= null){
            if (inspectUpload.getSpecies_chk().equals("P")){ // if inspection pass
                holder.specContainer.setBackgroundColor(passColor); //green
            }else if (inspectUpload.getSpecies_chk().equals("F")){// if fail
                holder.specContainer.setBackgroundColor(failColor);//red
            }else {
                holder.specContainer.setBackgroundColor(neutralColor);// white if no inspection data
            }
        }


        if (inspectUpload.getJh_hammer_chk() != null){

            if (inspectUpload.getJh_hammer_chk().equals("P")){
                holder.jhContainer.setBackgroundColor(passColor);
            }else if (inspectUpload.getJh_hammer_chk().equals("F")){
                holder.jhContainer.setBackgroundColor(failColor);
            }else {
                holder.jhContainer.setBackgroundColor(neutralColor);
            }
        }


        if (inspectUpload.getPro_mark_chk() != null){
            if (inspectUpload.getPro_mark_chk().equals("P")){
                holder.pmContainer.setBackgroundColor(passColor);
            }else if (inspectUpload.getPro_mark_chk().equals("F")){
                holder.pmContainer.setBackgroundColor(failColor);
            } else {
                holder.pmContainer.setBackgroundColor(neutralColor);
            }
        }


        if (inspectUpload.getDiameter_chk() != null){
            if (inspectUpload.getDiameter_chk().equals("P")){
                holder.diameterContainer.setBackgroundColor(passColor);
            }else if (inspectUpload.getDiameter_chk().equals("F")){
                holder.diameterContainer.setBackgroundColor(failColor);
            }else {
                holder.diameterContainer.setBackgroundColor(neutralColor);
            }
        }


        if (inspectUpload.getLength_chk() != null){
            if (inspectUpload.getLength_chk().equals("P")){
                holder.lengthContainer.setBackgroundColor(passColor);
            }else if (inspectUpload.getLength_chk().equals("F")){
                holder.lengthContainer.setBackgroundColor(failColor);
            }else {
                holder.lengthContainer.setBackgroundColor(neutralColor);
            }
        }


        if (inspectUpload.getLpi_chk() != null){
            if (inspectUpload.getLpi_chk().equals("P")){
                holder.lpiContainer.setBackgroundColor(passColor);
            }else if (inspectUpload.getLpi_chk().equals("F")){
                holder.lpiContainer.setBackgroundColor(failColor);
            }else {
                holder.lpiContainer.setBackgroundColor(neutralColor);
            }
        }

    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public int getItemCount() {
        return inspectUploadLists.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView lpiTv, longTv, latTv, jhTv, specTv, pmTv, length, diameter,
                userIdTv, remarkTv;
        LinearLayout lpiContainer, jhContainer, specContainer, pmContainer, lengthContainer,diameterContainer;

        public DataObjectHolder(View itemView) {
            super(itemView);
            lpiTv = itemView.findViewById(R.id.lpi_tv);
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
            List<InspectUpload> tempList = new ArrayList<>();

            inspectUploadLists = filterLists;

            if (charSequence.equals("pass")){

                for (InspectUpload inspectUpload: inspectUploadLists){

                    if (!inspectUpload.getLpi_chk().equals("F")
                            && !inspectUpload.getLength_chk().equals("F")
                            && !inspectUpload.getDiameter_chk().equals("F")
                            && !inspectUpload.getPro_mark_chk().equals("F")
                            && !inspectUpload.getJh_hammer_chk().equals("F")
                            && !inspectUpload.getSpecies_chk().equals("F")){

                        tempList.add(inspectUpload);
                    }
                }
                filterResults.values = tempList;
                filterResults.count = tempList.size();

            } else if(charSequence.equals("fail")){

                for (InspectUpload inspectUpload: inspectUploadLists){

                    if (inspectUpload.getLpi_chk().equals("F")
                            || inspectUpload.getLength_chk().equals("F")
                            || inspectUpload.getDiameter_chk().equals("F")
                            || inspectUpload.getPro_mark_chk().equals("F")
                            || inspectUpload.getJh_hammer_chk().equals("F")
                            || inspectUpload.getSpecies_chk().equals("F")){

                        tempList.add(inspectUpload);
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

            inspectUploadLists = (List<InspectUpload>) filterResults.values;
            notifyDataSetChanged();

        }
    };

}
