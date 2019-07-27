package com.aymanshehri.whenimthere;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.aymanshehri.whenimthere.services.CheckConnectionAsync;
import com.aymanshehri.whenimthere.services.MyFirebaseGetter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.aymanshehri.whenimthere.ui.main.SectionsPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.view_pager)
    MyViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.rl_check_connection)
    RelativeLayout checkConnectionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //region AdMob
        //code to load ads
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)//todo remove this before submit
                .build();
        adView.loadAd(adRequest);
        //endregion

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);

        tabs.setupWithViewPager(viewPager);
    }

    public void onStart() {
        super.onStart();
        CheckConnectionAsync checkConnectionAsync = new CheckConnectionAsync(this, checkConnectionLayout);
        checkConnectionAsync.execute();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (MyFirebaseGetter.getCurrentUser() == null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (tabs.getSelectedTabPosition() == 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else
            super.onBackPressed();
    }
}