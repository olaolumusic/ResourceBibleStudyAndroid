package org.resourcecenterint.resourcebiblestudyandroid;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.resourcecenterint.resourcebiblestudyandroid.fragments.BibleFragment;
import org.resourcecenterint.resourcebiblestudyandroid.fragments.DailyScriptureFragment;

public class BibleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayHomeAsUpEnabled(true);

        BiblePagerAdapter adapter = new BiblePagerAdapter(getSupportFragmentManager());
        final ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    static class BiblePagerAdapter extends FragmentStatePagerAdapter {

        public BiblePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {

                fragment = new DailyScriptureFragment();
            }
            if (position == 1) {
                fragment = new BibleFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String title = "";
            switch(position){
                case 0:
                    title = "Study Plan";
                    break;
                case 1:
                    title = "Bible";
                    break;

            }
            return title;
        }
    }
}
