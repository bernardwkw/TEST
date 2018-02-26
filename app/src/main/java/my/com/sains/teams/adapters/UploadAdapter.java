package my.com.sains.teams.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.db.MyInspectUpload;
import my.com.sains.teams.modal.UploadModal;

/**
 * Created by User on 21/12/2017.
 */

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.DataObjectHolder> {

    private List<UploadModal> mDataset;
    private static MyClickListener myClickListener;

    public UploadAdapter(List<UploadModal> uploadModalList){

        mDataset = new ArrayList<>();
        mDataset.addAll(uploadModalList);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_upload_cardview, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    static float progress;

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

        final UploadModal uploadModal = mDataset.get(position);
//        int dataNumber = uploadModal.getMyInspectUploadList().size();
//        int totalLogs = uploadModal.getMobileDocMember().getTotal_logs();
//        progress = ((float) dataNumber/(float) totalLogs)*1000;

        holder.summaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onItemClick(uploadModal.getMobileDocMember().getExch_id(), view);

            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mDataset.get(position).setChecked(b);
            }
        });

        progress = uploadModal.getSpecCheckProgress();
        if(progress >= 100){

            progress = 100;
            holder.checkBox.setChecked(true);
        }

        holder.licenseeTv.setText(uploadModal.getMobileDocMember().getName());
        holder.logProgressTv.setText(uploadModal.getMyInspectUploadList().size()+"/"+uploadModal.getMobileDocMember().getTotal_logs());
        holder.progressBar.setProgress((int) progress);
        holder.specCheckProgressTv.setText(""+(int) progress + " %");
        holder.fractionTv.setText(uploadModal.getSpecChecked()+" / "+ uploadModal.getTotalSpecCheck());
        if (uploadModal.getMyInspectUploadList().size()>0){
            holder.startDateTv.setText(uploadModal.getMyInspectUploadList().get(0).getInspect_date_time());
//            holder.lastDateTV.setText(uploadModal.getMyInspectUploadList()
//                    .get(uploadModal.getMyInspectUploadList().size()-1).getInspect_date_time());
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView logProgressTv, licenseeTv, coupeTv, startDateTv, lastDateTV, fractionTv, specCheckProgressTv;
        ImageButton summaryBtn;
        ProgressBar progressBar;
        CheckBox checkBox;
        CardView cardView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            logProgressTv = itemView.findViewById(R.id.log_progress_tv);
            specCheckProgressTv = itemView.findViewById(R.id.percentage_tv);
            fractionTv = itemView.findViewById(R.id.fraction_tv);
            licenseeTv = itemView.findViewById(R.id.licensee_tv);
            coupeTv = itemView.findViewById(R.id.coupe_tv);
            startDateTv = itemView.findViewById(R.id.start_date_tv);
//            lastDateTV = itemView.findViewById(R.id.last_date_tv);
            summaryBtn = itemView.findViewById(R.id.summary_btn);
            progressBar = itemView.findViewById(R.id.upload_progress);
            checkBox = itemView.findViewById(R.id.checkbox);
            cardView = itemView.findViewById(R.id.upload_card_view);
//            summaryBtn.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

//            if(progress == 100){
//            }

            if (progress < 100 && !checkBox.isChecked()){

                myClickListener.onCheckClick(checkBox);
            }else {
                checkBox.setChecked(false);
            }

//            if (checkBox.isChecked()){
//
//
//                checkBox.setChecked(false);
//            }else {
//                checkBox.setChecked(true);
//            }
        }
    }

    public List<MyInspectUpload> getCheckedData(){
        List<MyInspectUpload> myInspectUploadList = new ArrayList<>();


        for (UploadModal uploadModal: mDataset){

            if(uploadModal.isChecked()){
                for (MyInspectUpload tmp: uploadModal.getMyInspectUploadList()){
                    myInspectUploadList.add(tmp);
                }
            }
        }
        return myInspectUploadList;
    }


    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(String exchId, View v);
        void onCheckClick(CheckBox checkBox);
    }
}
