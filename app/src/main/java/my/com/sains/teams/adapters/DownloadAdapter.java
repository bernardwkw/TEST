package my.com.sains.teams.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.db.MobileDoc;
import my.com.sains.teams.modal.DownloadModal;

/**
 * Created by User on 21/12/2017.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DataObjectHolder> {

    private List<DownloadModal> mDataset;
    private static MyClickListener myClickListener;

    public DownloadAdapter(List<DownloadModal> downloadModalList){

        mDataset = new ArrayList<>();
        mDataset.addAll(downloadModalList);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_download_cardview, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }


    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

        DownloadModal downloadModal = mDataset.get(position);

        MobileDoc mobileDoc = downloadModal.getMobileDoc();

        holder.totalLogsTv.setText(mobileDoc.getTotal_logs().toString());
        holder.batchNo.setText(mobileDoc.getBatch_no());
        holder.accountCodeTv.setText(mobileDoc.getAcct_code());
        holder.licenseeTv.setText(mobileDoc.getName());
        holder.docTypeTv.setText(mobileDoc.getDoc_type());
        holder.summaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onItemClick(mDataset.get(position).getMobileDoc().getExch_id(), view);
            }
        });

        if (downloadModal.isDownloaded()){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ddffe3"));
            holder.isDownloaded = true;
        }else {
            holder.summaryBtn.setVisibility(View.GONE);
            holder.isDownloaded = false;
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    mDataset.get(position).setSelected(b);
                    Log.e("clicked", Boolean.toString(b));
                }
            });

            holder.checkBox.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        CardView cardView;
        TextView totalLogsTv, licenseeTv, batchNo, accountCodeTv,docTypeTv;
        CheckBox checkBox;
        ImageButton summaryBtn;
        Boolean isDownloaded = true;

        public DataObjectHolder(View itemView) {
            super(itemView);
            totalLogsTv = itemView.findViewById(R.id.total_log_tv);
            licenseeTv = itemView.findViewById(R.id.licensee_tv);
            batchNo = itemView.findViewById(R.id.batch_tv);////batch_no-->ref_no
            accountCodeTv = itemView.findViewById(R.id.account_code_tv);
            summaryBtn = itemView.findViewById(R.id.summary_btn);
            checkBox = itemView.findViewById(R.id.checkbox);
            cardView = itemView.findViewById(R.id.download_card_view);
            docTypeTv = itemView.findViewById(R.id.doc_type_tv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(!isDownloaded){
                if (checkBox.isChecked()){
                    checkBox.setChecked(false);
                }else {
                    checkBox.setChecked(true);
                }
            }

        }
    }


    public String getSelectedExchId(){

        Log.e("enter", "getCheckedExch");
        String exchId = "";
        for (DownloadModal downloadModal: mDataset){

            if (downloadModal.isSelected()){

                if (exchId.equals("")){
                    exchId = exchId + downloadModal.getMobileDoc().getExch_id();
                }else {
                    exchId =exchId + ","  + downloadModal.getMobileDoc().getExch_id();
                }
            }
        }

        return exchId;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(String exchId, View v);
    }

}
