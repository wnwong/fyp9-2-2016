package com.example.user.secondhandtradingplatform;

import android.content.Intent;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.secondhandtradingplatform.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import RealmModel.RealmGadget;
import adapter.TradeHistoryAdapter;
import io.realm.Realm;
import server.GetTradeCallback;
import server.ServerRequests;
import user.PersonalDetailsFragment;
import user.ProcessingTradeFragment;
import user.TradeHistoryFragment;
import user.UserLocalStore;

public class UserProfile extends AppCompatActivity implements PersonalDetailsFragment.PersonalDetailsFragmentListener{
    public static final String IMAGE_ADDRESS = "http://php-etrading.rhcloud.com/pictures/";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    ProgressBar progressBar;
    ImageView profile_pic;
    UserLocalStore userLocalStore;
    ServerRequests serverRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        userLocalStore = new UserLocalStore(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        profile_pic = (ImageView) findViewById(R.id.profile_pic);

        Picasso.with(this).load(IMAGE_ADDRESS+"SE215.jpg").transform(new CircleTransform()).into(profile_pic);

        serverRequests = new ServerRequests(this);
        serverRequests.fetchTradeDataInBackground(new GetTradeCallback() {
            @Override
            public void done(List<RealmGadget> realmGadgets) {
                Realm realm = Realm.getInstance(getApplicationContext());
                for (int i = 0; i < realmGadgets.size(); i++) {
                    RealmGadget currentGadget = realmGadgets.get(i);
                    RealmGadget toEdit = realm.where(RealmGadget.class).equalTo("product_id", currentGadget.getProduct_id()).findFirst();
                    realm.beginTransaction();
                    toEdit.setAvailability(currentGadget.getAvailability());
                    toEdit.setBuyer(currentGadget.getBuyer());
                    toEdit.setBuyer_location(currentGadget.getBuyer_location());
                    toEdit.setTrade_date(currentGadget.getTrade_date());
                    toEdit.setTrade_time(currentGadget.getTrade_time());
                    toEdit.setRating(currentGadget.getRating());
                    toEdit.setSeller_phone(currentGadget.getSeller_phone());
                    toEdit.setBuyer_phone(currentGadget.getBuyer_phone());
                    toEdit.setRating(currentGadget.getRating());
                    realm.commitTransaction();
                }
                Message message = new Message();
                message.what = 1;
                Message message1 = new Message();
                message1.what = 1;
                TradeHistoryFragment.mHandler.sendMessage(message);
                ProcessingTradeFragment.mHandler.sendMessage(message1);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void send() {
        startActivity(new Intent(this, Login.class));
        finish();
        userLocalStore.clearUserData();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new PersonalDetailsFragment();
            } else if (position == 1) {
                return new ProcessingTradeFragment();
            } else {
                return new TradeHistoryFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_personal_detail);
                case 1:
                    return getString(R.string.title_fragment_processing_trade);
                case 2:
                    return getString(R.string.title_fragment_trade_history);
            }
            return null;
        }
    }
}
