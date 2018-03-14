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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.db.LogRegister;
import my.com.sains.teams.utils.Consts;

/**
 * Created by User on 4/1/2018.
 */

public class DownloadSummaryAdapter extends RecyclerView.Adapter<DownloadSummaryAdapter.DataObjectHolder>
        implements Filterable{

    private List<LogRegister> logRegisterList;
    private List<LogRegister> filterLists;

    public DownloadSummaryAdapter(List<LogRegister> logRegisterList){

        this.logRegisterList = logRegisterList;
        this.filterLists = logRegisterList;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_summary_downloaded, parent, false);
        DownloadSummaryAdapter.DataObjectHolder dataObjectHolder = new DownloadSummaryAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        LogRegister logRegister = logRegisterList.get(position);
        holder.lpiTv.setText(logRegister.getLpi_no());
        holder.coupeTv.setText(logRegister.getCoupe_no());
        holder.blockTv.setText(logRegister.getBlock_no());
        holder.campTv.setText(logRegister.getCamp_code());
        holder.specTv.setText(logRegister.getSpecies_code());
        holder.pmTv.setText(logRegister.getPro_mark_reg_no());
        holder.length.setText(logRegister.getLength().toString());
        holder.diameter.setText(logRegister.getDiameter().toString());

        int selectedColor = Color.parseColor(Consts.SELECTED_PINK);
        int unselectedColor = Color.parseColor(Consts.UNSELECTED_GREY);

        String specCheck = logRegister.getSpec_check();
        if(specCheck != null){
            Log.e("specCheck", specCheck+": "+position);
            if (logRegister.getSpec_check().equals("Y")){
                holder.cardView.setCardBackgroundColor(selectedColor);
            }else {
                holder.cardView.setCardBackgroundColor(unselectedColor);
            }
        }else {
            holder.cardView.setCardBackgroundColor(unselectedColor);
        }


    }

    @Override
    public int getItemCount() {
        return logRegisterList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView lpiTv, coupeTv, blockTv, campTv, specTv, pmTv, length, diameter;

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
        }

    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            List<LogRegister> tempList = new ArrayList<>();

            logRegisterList = filterLists;

            if(charSequence != null && charSequence.length()>0){
                for (LogRegister logRegister: logRegisterList){


                    if (logRegister.getSpecies_code().toUpperCase().contains(charSequence)
                            ||logRegister.getCoupe_no().toUpperCase().contains(charSequence)
                            ||logRegister.getBlock_no().toUpperCase().contains(charSequence)
                            ||logRegister.getCamp_code().toUpperCase().contains(charSequence)
                            ||logRegister.getLpi_no().toUpperCase().contains(charSequence)
                            ||logRegister.getLog_serial_no().toUpperCase().contains(charSequence)
                            ||logRegister.getSpecies_code().toUpperCase().contains(charSequence)
                            ||logRegister.getPro_mark_reg_no().toUpperCase().contains(charSequence)){

                        tempList.add(logRegister);
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

            logRegisterList = (List<LogRegister>) filterResults.values;
            notifyDataSetChanged();

        }
    };
}
