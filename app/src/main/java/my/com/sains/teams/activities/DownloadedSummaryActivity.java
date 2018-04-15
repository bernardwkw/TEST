package my.com.sains.teams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.adapters.DownloadSummaryAdapter;
import my.com.sains.teams.db.DaoSession;
import my.com.sains.teams.db.LogRegister;
import my.com.sains.teams.db.LogRegisterDao;
import my.com.sains.teams.utils.Consts;

public class DownloadedSummaryActivity extends AppCompatActivity {

    private DaoSession daoSession;
    private List<LogRegister> logRegisterList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_summary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String exchId = i.getStringExtra(Consts.EXCH_ID);

        Log.e("exch ID", exchId);

        if (exchId != null){
            daoSession = ((App) getApplication()).getDaoSession();

            LogRegisterDao logRegisterDao = daoSession.getLogRegisterDao();

            Query<LogRegister> logRegisterQuery = logRegisterDao.queryBuilder()
                    .orderDesc(LogRegisterDao.Properties.Spec_check)// rearrange spec_checked log show on top
                    .where(LogRegisterDao.Properties.Exch_id.eq(exchId)).build();

            logRegisterList = logRegisterQuery.list();

            mRecyclerView =  findViewById(R.id.downloaded_summary_recycle_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getApplication());
            mRecyclerView.setLayoutManager(mLayoutManager);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter = new DownloadSummaryAdapter(logRegisterList);
                    mRecyclerView.setAdapter(mAdapter);

                }
            });

//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    ((UploadSummaryAdapter)mAdapter).getFilter().filter(newText.toUpperCase());
//                    Log.e("text ", newText);
//                    return false;
//                }
//            });
            //searchView.setQuery("", false);

        }else {

            Toast.makeText(getApplicationContext(), "EXCHANGE ID is NULL", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_view, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((DownloadSummaryAdapter)mAdapter).getFilter().filter(newText.toUpperCase());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
