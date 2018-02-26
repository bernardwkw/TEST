package my.com.sains.teams.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import my.com.sains.teams.R;
import my.com.sains.teams.adapters.ViewPagerAdapter;
import my.com.sains.teams.fragments.DownloadFragment;
import my.com.sains.teams.fragments.UploadFragment;

public class SyncActivity extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    ViewPagerAdapter adapter;

    //Fragments

    UploadFragment uploadFragment;
    DownloadFragment downloadFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        viewPager = findViewById(R.id.viewpager);
//        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        downloadFragment=new DownloadFragment();
        uploadFragment=new UploadFragment();
        adapter.addFragment(downloadFragment,"Download");
        adapter.addFragment(uploadFragment,"Upload");
        viewPager.setAdapter(adapter);
    }
}
