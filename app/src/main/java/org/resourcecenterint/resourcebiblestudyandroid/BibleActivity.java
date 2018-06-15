package org.resourcecenterint.resourcebiblestudyandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.resourcecenterint.resourcebiblestudyandroid.fragments.BibleFragment;
import org.resourcecenterint.resourcebiblestudyandroid.fragments.DailyScriptureFragment;

public class BibleActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (menuItem.getTitle().toString().toLowerCase())
                {
                    case "discussion forum":
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        break;

                    default:
                        Toast.makeText(BibleActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        BiblePagerAdapter adapter = new BiblePagerAdapter(getSupportFragmentManager());
        final ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        int count =  getFragmentManager().getBackStackEntryCount();
        if (count == 0 )
            super.onBackPressed();
        else
            getFragmentManager().popBackStack();
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
